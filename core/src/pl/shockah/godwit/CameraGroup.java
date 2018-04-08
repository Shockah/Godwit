package pl.shockah.godwit;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;

public class CameraGroup extends RenderGroup {
	@Getter
	@Nonnull private Camera camera = new OrthographicCamera();

	@Getter
	@Nonnull private Viewport viewport = new ScreenViewport(camera);

	public boolean centerViewport = false;

	private int lastWidth = 0;
	private int lastHeight = 0;
	private boolean lastCenterViewport = centerViewport;

	@Nonnull public Rectangle getBoundingBox() {
		return new Rectangle(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
	}

	public void setCamera(@Nonnull Camera camera) {
		this.camera = camera;
		viewport.setCamera(camera);
		lastWidth = 0;
		lastHeight = 0;
	}

	public void setViewport(@Nonnull Viewport viewport) {
		this.viewport = viewport;
		lastWidth = 0;
		lastHeight = 0;
	}

	protected void updateViewportIfNeeded(@Nonnull Gfx gfx) {
		int newWidth = gfx.getWidth();
		int newHeight = gfx.getHeight();

		if (newWidth != lastWidth || newHeight != lastHeight || lastCenterViewport != centerViewport) {
			lastWidth = newWidth;
			lastHeight = newHeight;
			lastCenterViewport = centerViewport;
			viewport.update(newWidth, newHeight, centerViewport);
		}
	}

	protected void updateCamera() {
		if (camera instanceof OrthographicCamera)
			((OrthographicCamera)camera).setToOrtho(Godwit.getInstance().yPointingDown, lastWidth, lastHeight);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		updateViewportIfNeeded(gfx);
		updateCamera();
		viewport.apply();
		gfx.getSpriteBatch().setProjectionMatrix(camera.combined);
		gfx.getShapeRenderer().setProjectionMatrix(camera.combined);
		super.render(gfx, v);
	}
}