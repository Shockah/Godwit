package pl.shockah.godwit.tree

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.min

open class TreeNodeGame(
		private val initialStageFactory: () -> Stage
) : ApplicationAdapter() {
	private lateinit var backingStage: Stage

	var maximumDeltaTime: Float? = 1f / 15f

	var stage: Stage
		get() = backingStage
		set(new) {
			backingStage = new
			Gdx.input.inputProcessor = new
		}

	constructor(viewport: Viewport = ScreenViewport()) : this({ Stage(viewport) })

	override fun create() {
		stage = initialStageFactory()
	}

	override fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height, true)
	}

	override fun render() {
		val gdxDelta = Gdx.graphics.deltaTime
		val delta = maximumDeltaTime?.let { min(gdxDelta, it) } ?: gdxDelta

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.update(delta)
		stage.draw()
	}
}