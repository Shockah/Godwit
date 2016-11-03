package io.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic
import io.shockah.godwit.geom.Vec2

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
abstract class Sprite {
    Vec2 offset = Vec2.Zero

    protected final void draw(SpriteBatch sb, Vec2 pos) {
        draw(sb, pos.x, pos.y)
    }

    protected final void draw(SpriteBatch sb, float x, float y) {
        internalDraw(sb, x + offset.x as float, y + offset.y as float)
    }

    protected abstract void internalDraw(SpriteBatch sb, float x, float y)
}