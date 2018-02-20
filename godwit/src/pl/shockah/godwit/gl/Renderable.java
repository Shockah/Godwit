package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public interface Renderable {
	void render(@Nonnull Gfx gfx, @Nonnull IVec2 v);

	default void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new ImmutableVec2(x, y));
	}

	default void render(@Nonnull Gfx gfx) {
		render(gfx, ImmutableVec2.zero);
	}
}