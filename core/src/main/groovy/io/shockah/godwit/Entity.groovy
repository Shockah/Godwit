package io.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class Entity {
    protected float depth = 0f
    protected EntityGroup<? extends Entity> group
    @PackageScope boolean created = false, destroyed = false

    final def isCreated() {
        created
    }

    final def isDestroyed() {
        destroyed
    }

    @PackageScope setCreated(boolean created) {
        this.created = created
    }

    @PackageScope setDestroyed(boolean destroyed) {
        this.destroyed = destroyed
    }

    @PackageScope final void create() {
        if (created || destroyed)
            return
        created = true
        onCreate()
    }

    final void create(Godwit godwit) {
        if (created || destroyed)
            return
        assert godwit, "No Godwit instance provided."
        create(godwit.state)
    }

    final void create(EntityGroup<? extends Entity> group) {
        if (created || destroyed)
            return
        assert group, "No EntityGroup provided."
        group.markCreate(this)
    }

    final void destroy() {
        if (!created || destroyed)
            return
        beforeDestroy()
        if (!group || group.destroyed) {
            destroyed = false
            onDestroy()
        } else {
            group.markDestroy(this)
        }
    }

    final void update() {
        if (!created || destroyed)
            return
        onUpdate()
    }

    final void render(Gfx gfx) {
        if (!created || destroyed)
            return
        onRender(gfx)
    }

    protected void onCreate() {
    }

    protected void beforeDestroy() {
    }

    protected void onDestroy() {
    }

    protected void onUpdate() {
    }

    protected void onRender(Gfx gfx) {
    }
}