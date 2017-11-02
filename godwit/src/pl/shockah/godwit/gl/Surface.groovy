package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class Surface extends GfxImpl implements Renderable {
	@Nonnull final FrameBuffer fbo
	@Nonnull final TextureRegion region

	@Nonnull static Surface create(int width, int height) {
		GfxContextManager.bindSurface(null)
		return new Surface(width, height)
	}

	protected Surface(int width, int height) {
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false, false)
		region = new TextureRegion(fbo.colorBufferTexture, 0, 0, width, height)
		region.flip(false, true)
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
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		gfx.prepareSprites()
		gfx.spriteBatch.draw(region, x, y)
	}
}