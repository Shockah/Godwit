package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;

public final class GfxContextManager {
	@Nullable
	private static Surface boundSurface;

	@Nonnull
	public static Gfx getCurrentGfx() {
		return boundSurface != null ? boundSurface : Godwit.getInstance().gfx;
	}

	public static void bindSurface(@Nullable Surface surface) {
		if (boundSurface == surface)
			return;

		getCurrentGfx().flush();

		if (surface == null) {
			if (boundSurface != null)
				boundSurface.fbo.end();
			boundSurface = null;
		} else {
			surface.fbo.begin();
			boundSurface = surface;
		}
	}
}