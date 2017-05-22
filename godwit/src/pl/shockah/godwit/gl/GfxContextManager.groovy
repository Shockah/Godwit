package pl.shockah.godwit.gl

import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit

@CompileStatic
final class GfxContextManager {
    private static Surface boundSurface

    static Gfx getCurrentGfx() {
        return boundSurface ?: Godwit.instance.gfx
    }

    static void bindSurface(Surface surface) {
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