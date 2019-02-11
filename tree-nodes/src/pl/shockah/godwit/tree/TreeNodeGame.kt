package pl.shockah.godwit.tree

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport

open class TreeNodeGame(
		private val initialStageFactory: () -> Stage
) : ApplicationAdapter() {
	private lateinit var backingStage: Stage

	var stage: Stage
		get() = backingStage
		set(new) {
			backingStage = new
//			Gdx.input.inputProcessor = new
		}

	constructor(viewport: Viewport = ScreenViewport()) : this({ Stage(viewport) })

	override fun create() {
		stage = initialStageFactory()
	}

	override fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height, true)
	}

	override fun render() {
		val delta = Gdx.graphics.deltaTime
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.update(delta)
		stage.draw()
	}
}