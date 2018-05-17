package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Vec2;

public abstract class AbstractRenderable implements Renderable {
	public final void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new Vec2(x, y));
	}

	public final void render(@Nonnull Gfx gfx) {
		render(gfx, Vec2.zero);
	}
}