package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public interface Renderable {
	void render(@Nonnull Gfx gfx, @Nonnull IVec2 v);

	default void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new Vec2(x, y));
	}

	default void render(@Nonnull Gfx gfx) {
		render(gfx, Vec2.zero);
	}
}