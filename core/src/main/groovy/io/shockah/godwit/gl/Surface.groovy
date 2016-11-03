package io.shockah.godwit.gl

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import groovy.transform.CompileStatic
import io.shockah.godwit.Godwit

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class Surface extends Gfx {
    protected final Sprite sprite
    protected final FrameBuffer fbo
    protected final TextureRegion region

    static Surface create(int width, int height) {
        GfxContextManager.bindSurface(null)
        new Surface(width, height)
    }

    protected Surface(int width, int height) {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false, false)
        region = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, width, height)
        region.flip(false, true)
        sprite = new TextureRegionSprite(region)
    }

    @Override
    void prepareContext() {
        GfxContextManager.bindSurface(this)
    }

    @Override
    void updateCamera() {
        def camera = Godwit.instance.camera
        camera.viewportWidth = region.regionWidth
        camera.viewportHeight = region.regionHeight
        camera.position.set(camera.viewportWidth * 0.5f as float, camera.viewportHeight * 0.5f as float, 0f)
        camera.update()
        updateCombinedCamera(camera.combined)
    }

    Sprite getSprite() {
        sprite
    }

    FrameBuffer getFbo() {
        fbo
    }

    TextureRegion getTextureRegion() {
        region
    }
}