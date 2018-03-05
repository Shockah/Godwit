package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;

public class Surface extends GfxImpl implements Renderable {
	@Nonnull public final FrameBuffer fbo;
	@Nonnull public final TextureRegion region;

	@Nonnull public static Surface create(int width, int height) {
		return create(width, height, false);
	}

	@Nonnull public static Surface create(int width, int height, boolean depth) {
		GfxContextManager.bindSurface(null);
		return new Surface(width, height, depth);
	}

	protected Surface(int width, int height, boolean depth) {
		fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, depth, false);
		region = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, width, height);
		region.flip(false, true);
	}

	@Override
	public int getWidth() {
		return fbo.getWidth();
	}

	@Override
	public int getHeight() {
		return fbo.getHeight();
	}

	@Override
	protected void prepareContext() {
		GfxContextManager.bindSurface(this);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareSprites();
		gfx.getSpriteBatch().draw(region, v.x(), v.y());
	}
}