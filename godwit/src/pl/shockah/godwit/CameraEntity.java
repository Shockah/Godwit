package pl.shockah.godwit;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;

public class CameraEntity extends RenderGroupEntity {
	@Nonnull protected final OrthographicCamera camera = new OrthographicCamera();

	protected boolean centeredCamera = true;

	@Nonnull public IVec2 getCameraPosition() {
		return new Vec2(camera.position.x, camera.position.y);
	}

	public void setCameraPosition(@Nonnull IVec2 position) {
		centeredCamera = false;
		camera.position.x = position.x();
		camera.position.y = position.y();
		updateCamera();
	}

	protected void recenterCameraPosition() {
		Gfx gfx = Godwit.getInstance().gfx;
		camera.position.x = gfx.getWidth() * 0.5f;
		camera.position.y = gfx.getHeight() * 0.5f;
	}

	public void resetCamera() {
		centeredCamera = true;
		recenterCameraPosition();
		updateCamera();
	}

	protected void updateCamera() {
		Gfx gfx = Godwit.getInstance().gfx;
		Viewport viewport = gfx.getViewport();
		if (viewport != null)
			viewport.apply();
		camera.update();
		updateCombinedCamera(camera.combined);
	}

	protected void updateCombinedCamera(@Nonnull Matrix4 matrix) {
		Gfx gfx = Godwit.getInstance().gfx;
		gfx.getSpriteBatch().setProjectionMatrix(matrix);
		gfx.getShapeRenderer().setProjectionMatrix(matrix);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		updateCamera();
		camera.setToOrtho(Godwit.getInstance().yPointingDown, gfx.getWidth(), gfx.getHeight());
		super.render(gfx, v);
	}
}