package pl.shockah.godwit.gl

import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
final class GfxContextManager {
	@Nullable private static Surface boundSurface

	@Nonnull static Gfx getCurrentGfx() {
		return boundSurface ?: Godwit.instance.gfx
	}

	static void bindSurface(@Nullable Surface surface) {
		if (boundSurface == surface)
			return

		currentGfx.internalEndTick()

		if (surface) {
			surface.fbo.begin()
			boundSurface = surface
		} else {
			boundSurface.fbo.end()
			boundSurface = null
		}

		currentGfx.updateCamera()
	}
}