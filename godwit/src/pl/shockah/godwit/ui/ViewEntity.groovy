package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.Entity
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
class ViewEntity extends Entity {
	@Nonnull final View view

	ViewEntity(@Nonnull View view) {
		this.view = view
	}

	@Override
	void onUpdate() {
		super.onUpdate()
		view.onUpdate()
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)
		view.onRender(gfx, x, y)
	}
}