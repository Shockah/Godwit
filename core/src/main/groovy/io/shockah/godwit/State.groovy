package io.shockah.godwit

import groovy.transform.CompileStatic
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class State {
    final void create() {
        onCreate()
    }

    final void destroy() {
        onDestroy()
    }

    final void preUpdate() {
        onPreUpdate()
    }

    final void postUpdate() {
        onPostUpdate()
    }

    final void render(Gfx gfx) {
        onRender(gfx)
    }

    protected void onCreate() {
    }

    protected void onDestroy() {
    }

    protected void onPreUpdate() {
    }

    protected void onPostUpdate() {
    }

    protected void onRender(Gfx gfx) {
    }
}