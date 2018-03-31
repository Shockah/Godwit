package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.IVec2;

public class Surface extends GfxImpl implements Renderable, Disposable {
	@Nonnull public final FrameBuffer fbo;
	@Nonnull public final TextureRegion region;

	@Getter
	private boolean disposed = false;

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
		region.flip(false, Godwit.getInstance().yPointingDown);
	}

	@Override
	public int getWidth() {
		if (disposed)
			throw new IllegalStateException("Surface is disposed.");
		return fbo.getWidth();
	}

	@Override
	public int getHeight() {
		if (disposed)
			throw new IllegalStateException("Surface is disposed.");
		return fbo.getHeight();
	}

	@Override
	protected void prepareContext() {
		if (disposed)
			throw new IllegalStateException("Surface is disposed.");
		GfxContextManager.bindSurface(this);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (disposed)
			throw new IllegalStateException("Surface is disposed.");
		gfx.prepareSprites();
		gfx.getSpriteBatch().draw(region, v.x(), v.y());
	}

	@Override
	public void dispose() {
		if (disposed)
			return;
		disposed = true;
		fbo.dispose();
	}

//	@Override
//	public void updateCombinedCamera(@Nonnull Matrix4 matrix) {
//		matrix = new Matrix4();
//		matrix.setToOrtho2D(0f, 0f, getWidth(), getHeight());
//		super.updateCombinedCamera(matrix);
//	}
}