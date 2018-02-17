package pl.shockah.godwit.gl

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2

import javax.annotation.Nonnull

@CompileStatic
abstract trait Renderable {
	float depth = 0f

	void onRender(@Nonnull Gfx gfx) {
		onRender(gfx, 0f, 0f)
	}

	void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 offset) {
		onRender(gfx, offset.x, offset.y)
	}

	abstract void onRender(@Nonnull Gfx gfx, float x, float y)
}