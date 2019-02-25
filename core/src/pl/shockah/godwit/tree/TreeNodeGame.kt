package pl.shockah.godwit.tree

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.GodwitApplicationAdapter
import kotlin.math.min

open class TreeNodeGame(
		private val initialStageFactory: () -> Stage
) : GodwitApplicationAdapter() {
	companion object {
		operator fun invoke(stageLayerFactory: () -> StageLayer): TreeNodeGame {
			return TreeNodeGame { Stage(stageLayerFactory()) }
		}
	}

	private lateinit var backingStage: Stage

	var maximumDeltaTime: Float? = 1f / 15f

	var stage: Stage
		get() = backingStage
		set(new) {
			backingStage = new
			Gdx.input.inputProcessor = new
		}

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