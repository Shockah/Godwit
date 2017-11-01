package pl.shockah.godwit.gl

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
abstract trait Renderable {
	float depth = 0f

	float getDepth() {
		return this.depth
	}

	void setDepth(float depth) {
		this.depth = depth
	}

	void onRender(@Nonnull Gfx gfx) {
		onRender(gfx, 0f, 0f)
	}

	void onRender(@Nonnull Gfx gfx, @Nonnull Vec2 offset) {
		onRender(gfx, offset.x, offset.y)
	}

	abstract void onRender(@Nonnull Gfx gfx, float x, float y)
}