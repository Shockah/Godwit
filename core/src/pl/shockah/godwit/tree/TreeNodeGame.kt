package pl.shockah.godwit.tree

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.GodwitApplicationAdapter
import pl.shockah.godwit.LateInitAwaitable
import kotlin.math.min

open class TreeNodeGame(
		private val initialStageFactory: () -> Stage
) : GodwitApplicationAdapter() {
	companion object {
		operator fun invoke(stageLayerFactory: () -> StageLayer): TreeNodeGame {
			return TreeNodeGame { Stage(stageLayerFactory()) }
		}
	}

	var stage: Stage by LateInitAwaitable { _, _, new ->
		Gdx.input.inputProcessor = new
	}

	var maximumDeltaTime: Float? = 1f / 15f

	constructor(viewport: Viewport = ScreenViewport()) : this({ Stage(StageLayer(viewport)) })

	override fun create() {
		stage = initialStageFactory()
	}

	override fun resize(width: Int, height: Int) {
		stage.resize(width, height)
	}

	override fun render() {
		val gdxDelta = Gdx.graphics.deltaTime
		val delta = maximumDeltaTime?.let { min(gdxDelta, it) } ?: gdxDelta

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.update(delta)
		stage.draw()
	}
}