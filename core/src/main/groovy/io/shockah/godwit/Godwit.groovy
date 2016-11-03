package io.shockah.godwit

import com.badlogic.gdx.graphics.OrthographicCamera
import groovy.transform.CompileStatic
import io.shockah.godwit.gl.Gfx
import io.shockah.godwit.gl.GfxContextManager

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
final class Godwit {
    private static Godwit instance

    protected State state, newState
    protected EntityGroup<Entity> entities
    protected OrthographicCamera camera = new OrthographicCamera()
    final Gfx gfx = new Gfx()

    static Godwit getInstance() {
        if (!instance)
            instance = new Godwit()
        instance
    }

    OrthographicCamera getCamera() {
        camera
    }

    void setState() {
        newState = state
    }

    void tick() {
        if (newState) {
            if (entities)
                entities.destroy()
            if (state)
                state.destroy()
            entities = new EntityGroup<>()
            entities.create()
            state = newState
            newState = null
            state.create()
        }

        if (state) {
            state.preUpdate()
            entities.update()
            state.postUpdate()
        }

        GfxContextManager.bindSurface(null)
        gfx.updateCamera()
        gfx.clear(Color.BLACK)
        gfx.setBlendMode(BlendMode.Normal)

        if (state) {
            entities.render(gfx)
            state.render(gfx)
        }

        gfx.endTick()
        GfxContextManager.bindSurface(null)
    }
}