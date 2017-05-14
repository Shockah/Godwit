package pl.shockah.godwit

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.BlendMode
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxContextManager

@CompileStatic
final class Godwit {
	private static Godwit instance

	protected State state
	protected State newState
	protected OrthographicCamera camera = new OrthographicCamera()
	final Gfx gfx = new Gfx()

	static Godwit getInstance() {
		if (!instance)
			instance = new Godwit()
		return instance
	}

	OrthographicCamera getCamera() {
		return camera
	}

	State getState() {
		return state
	}

	void setState(State state) {
		newState = state
	}

	void tick() {
		if (newState) {
			if (state)
				state.destroy()
			state = newState
			newState = null
			state.create()
		}

		if (state)
			state.update()

		GfxContextManager.bindSurface(null)
		gfx.updateCamera()
		gfx.clear(Color.BLACK)
		gfx.blendMode = BlendMode.Normal

		if (state)
			state.render(gfx)

		gfx.endTick()
		GfxContextManager.bindSurface(null)
	}
}