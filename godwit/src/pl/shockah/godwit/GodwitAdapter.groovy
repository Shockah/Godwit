package pl.shockah.godwit

import com.badlogic.gdx.ApplicationAdapter
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class GodwitAdapter extends ApplicationAdapter {
	@Nonnull final State initialState

	GodwitAdapter(@Nonnull State initialState) {
		this.initialState = initialState
	}

	@Override
	void create() {
		Godwit.instance.state = initialState
	}

	@Override
	void resize(int width, int height) {
		Godwit.instance.gfx.camera.setToOrtho(true, width, height)
		Godwit.instance.gfx.viewport.update(width, height, false)
	}

	@Override
	void render() {
		Godwit.instance.tick()
	}
}