package pl.shockah.godwit;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
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
	@Nullable private Rectangle cachedBoundingBox;

	@Nonnull public Rectangle getBoundingBox() {
		if (cachedBoundingBox == null) {
			Vector3 vec = new Vector3(viewport.getScreenX(), viewport.getScreenY(), 0);
			camera.unproject(vec);

			float x1 = vec.x;
			float y1 = vec.y;

			vec.set(viewport.getScreenX() + viewport.getScreenWidth(), viewport.getScreenY() + viewport.getScreenHeight(), 0);
			camera.unproject(vec);

			float x2 = vec.x;
			float y2 = vec.y;

			float x = Math.min(x1, x2);
			float y = Math.min(y1, y2);
			float w = Math.max(x1, x2) - x;
			float h = Math.max(y1, y2) - y;

			cachedBoundingBox = new Rectangle(x, y, w, h);
		}
		return cachedBoundingBox;
	}

	@Override
	public void onAddedToHierarchy() {
		super.onAddedToHierarchy();
		for (Entity entity : getParentTree()) {
			if (entity instanceof CameraGroup && entity != this)
				throw new IllegalStateException("CameraGroups can't be nested.");
		}
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
			updateOnScreenSizeChange(newWidth, newHeight, centerViewport);
			lastWidth = newWidth;
			lastHeight = newHeight;
			lastCenterViewport = centerViewport;
		}
	}

	protected void updateOnScreenSizeChange(int width, int height, boolean centerViewport) {
		updateCameraOnScreenSizeChange(width, height, centerViewport);
		viewport.update(width, height, centerViewport);
		cachedBoundingBox = null;
	}

	protected void updateCameraOnScreenSizeChange(int width, int height, boolean centerViewport) {
		if (camera instanceof OrthographicCamera)
			((OrthographicCamera)camera).setToOrtho(Godwit.getInstance().yPointingDown, width, height);
	}

	public final void updateCamera() {
		camera.update();
		cachedBoundingBox = null;
	}

	@Nonnull public Vec2 getCameraPosition() {
		return new Vec2(camera.position.x, camera.position.y);
	}

	public void setCameraPosition(@Nonnull IVec2 v) {
		camera.position.x = v.x();
		camera.position.y = v.y();
		updateCamera();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		updateViewportIfNeeded(gfx);
		viewport.apply();
		gfx.getSpriteBatch().setProjectionMatrix(camera.combined);
		gfx.getShapeRenderer().setProjectionMatrix(camera.combined);
		super.render(gfx, v);
	}
}