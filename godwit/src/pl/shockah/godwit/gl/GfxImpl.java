package pl.shockah.godwit.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public class GfxImpl extends Gfx {
	protected boolean centeredCamera = true;

	private boolean spritesMode = false;
	@Nullable private ShapeRenderer.ShapeType shapesMode = null;

	@Getter @Setter
	@Nonnull protected OrthographicCamera camera = new OrthographicCamera();

	@Getter @Setter
	@Nullable protected Viewport viewport;

	@Getter
	@Nonnull private final SpriteBatch spriteBatch = new SpriteBatch();

	@Getter
	@Nonnull private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	@Getter
	@Nullable private BlendMode blendMode = null;

	@Getter
	@Nonnull private Color color = Color.BLACK;

	@Override
	@Nonnull public IVec2 getCameraPosition() {
		return new ImmutableVec2(camera.position.x, camera.position.y);
	}

	@Override
	public void setCameraPosition(@Nonnull IVec2 position) {
		centeredCamera = false;
		camera.position.x = position.x();
		camera.position.y = position.y();
		updateCamera();
	}

	private void recenterCameraPosition() {
		camera.position.x = getWidth() * 0.5f;
		camera.position.y = getHeight() * 0.5f;
	}

	@Override
	public void resetCamera() {
		centeredCamera = true;
		recenterCameraPosition();
		updateCamera();
	}

	@Override
	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	@Override
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	@Override
	public void setBlendMode(@Nullable BlendMode blendMode) {
		prepareContext();
		if (this.blendMode == blendMode)
			return;
		if (this.blendMode != null)
			this.blendMode.end();
		this.blendMode = blendMode;
		if (blendMode != null)
			blendMode.begin();
	}

	@Override
	public void setColor(@Nonnull Color color) {
		prepareContext();
		this.color = color;
		spriteBatch.setColor(color);
		shapeRenderer.setColor(color);
	}

	@Override
	protected void internalEndTick() {
		if (spritesMode) {
			spriteBatch.end();
			spritesMode = false;
		}
		if (shapesMode != null) {
			shapeRenderer.end();
			shapesMode = null;
		}
	}

	@Override
	protected void prepareContext() {
		GfxContextManager.bindSurface(null);
	}

	@Override
	public void prepareSprites() {
		prepareContext();
		if (spritesMode)
			return;
		if (shapesMode != null) {
			shapeRenderer.end();
			shapesMode = null;
		}
		spriteBatch.begin();
		spritesMode = true;
	}

	@Override
	public void prepareShapes(@Nonnull ShapeRenderer.ShapeType type) {
		prepareContext();
		if (shapesMode == type)
			return;
		if (spritesMode) {
			spriteBatch.end();
			spritesMode = false;
		}
		if (shapesMode != null)
			shapeRenderer.end();
		shapeRenderer.begin(type);
		shapesMode = type;
	}

	public void endTick() {
		prepareContext();
		internalEndTick();
	}

	@Override
	public void drawPoint(@Nonnull IVec2 v) {
		prepareShapes(ShapeRenderer.ShapeType.Point);
		shapeRenderer.point(v.x(), v.y(), 0f);
	}

	@Override
	public void clear(@Nonnull Color color) {
		prepareContext();
		Gdx.gl20.glClearColor(color.r, color.g, color.b, color.a);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
	}

	@Override
	public void updateCamera() {
		internalEndTick();
		if (centeredCamera)
			recenterCameraPosition();
		super.updateCamera();
	}

	@Override
	public void updateCombinedCamera(@Nonnull Matrix4 matrix) {
		spriteBatch.setProjectionMatrix(matrix);
		shapeRenderer.setProjectionMatrix(matrix);
	}
}