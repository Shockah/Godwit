package io.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class TextureRegionSprite extends Sprite {
    final TextureRegion region

    public TextureRegionSprite(TextureRegion region) {
        this.region = region
    }

    @Override
    protected void internalDraw(SpriteBatch sb, float x, float y) {
        sb.draw(region, x, y)
    }
}