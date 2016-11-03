package io.shockah.godwit.gl

import groovy.transform.CompileStatic
import io.shockah.godwit.Godwit

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
final class GfxContextManager {
    private static Surface boundSurface

    static Gfx getCurrentGfx() {
        boundSurface ?: Godwit.instance.gfx
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