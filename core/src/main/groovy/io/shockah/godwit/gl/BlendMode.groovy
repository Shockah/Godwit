package io.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
abstract class BlendMode {
    public static final BlendMode Normal = [
            begin: {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
            },
            end: {
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
    ] as BlendMode

    protected abstract void begin()

    protected abstract void end()
}