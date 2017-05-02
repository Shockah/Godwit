package pl.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit

@CompileStatic
class Surface extends Gfx {
    protected final Sprite sprite
    protected final FrameBuffer fbo
    protected final TextureRegion region

    static Surface create(int width, int height) {
        GfxContextManager.bindSurface(null)
        return new Surface(width, height)
    }

    protected Surface(int width, int height) {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false, false)
        region = new TextureRegion(fbo.colorBufferTexture, 0, 0, width, height)
        region.flip(false, true)
        sprite = new TextureRegionSprite(region)
    }

    @Override
    int getWidth() {
        return fbo.width
    }

    @Override
    int getHeight() {
        return fbo.height
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
        Gdx.gl20.glViewport(0, 0, region.regionWidth, region.regionHeight)
    }

    Sprite getSprite() {
        return sprite
    }

    FrameBuffer getFbo() {
        return fbo
    }

    TextureRegion getTextureRegion() {
        return region
    }
}