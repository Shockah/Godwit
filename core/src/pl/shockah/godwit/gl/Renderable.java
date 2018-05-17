package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;

public interface Renderable {
	void render(@Nonnull Gfx gfx, @Nonnull IVec2 v);

	void render(@Nonnull Gfx gfx, float x, float y);

	void render(@Nonnull Gfx gfx);
}