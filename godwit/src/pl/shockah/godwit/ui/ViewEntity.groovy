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
	protected void onUpdate() {
		super.onUpdate()
		view.onUpdate()
	}

	@Override
	void onRender(@Nonnull Gfx gfx) {
		super.onRender(gfx)
		view.onRender(gfx)
	}
}