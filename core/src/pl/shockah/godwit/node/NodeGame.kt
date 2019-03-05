package pl.shockah.godwit.node

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.GodwitApplication
import pl.shockah.godwit.LateInitAwaitable
import kotlin.math.min

open class NodeGame(
		private val initialStageFactory: () -> Stage
) : GodwitApplication() {
	companion object {
		operator fun invoke(stageLayerFactory: () -> StageLayer): NodeGame {
			return NodeGame { Stage(stageLayerFactory()) }
		}
	}

	var stage: Stage by LateInitAwaitable { _, _, new ->
		Gdx.input.inputProcessor = new
	}

	var maximumDeltaTime: Float? = 1f / 15f

	constructor(viewport: Viewport = ScreenViewport()) : this({ Stage(StageLayer(viewport)) })

	override fun create() {
		super.create()
		stage = initialStageFactory()
	}

	override fun resize(width: Int, height: Int) {
		super.resize(width, height)
		stage.resize(width, height)
	}

	override fun render() {
		super.render()

		val gdxDelta = Gdx.graphics.deltaTime
		val delta = maximumDeltaTime?.let { min(gdxDelta, it) } ?: gdxDelta

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.update(delta)
		stage.draw()
	}
}