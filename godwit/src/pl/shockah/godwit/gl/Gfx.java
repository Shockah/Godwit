package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.func.Action0;
import pl.shockah.func.Action1;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Shape;

public abstract class Gfx {
	@Nonnull public abstract SpriteBatch getSpriteBatch();

	@Nonnull public abstract ShapeRenderer getShapeRenderer();

	@Nullable public abstract Viewport getViewport();
	public abstract void setViewport(@Nullable Viewport viewport);

	@Nonnull public abstract OrthographicCamera getCamera();
	public abstract void setCamera(@Nonnull OrthographicCamera camera);

	@Nonnull public abstract IVec2 getCameraPosition();
	public abstract void setCameraPosition(@Nonnull IVec2 position);

	public abstract void resetCamera();

	public abstract int getWidth();
	public abstract int getHeight();

	@Nonnull public final IVec2 getSize() {
		return new ImmutableVec2(getWidth(), getHeight());
	}

	@Nullable public abstract BlendMode getBlendMode();
	public abstract void setBlendMode(@Nullable BlendMode blendMode);

	@Nonnull public abstract Color getColor();
	public abstract void setColor(@Nonnull Color color);

	public final void setColor(float r, float g, float b, float a) {
		setColor(new Color(r, g, b, a));
	}

	public final void setColor(float r, float g, float b) {
		setColor(new Color(r, g, b, 1f));
	}

	public final void withColor(@Nonnull Color color, @Nonnull Action0 f) {
		Color old = getColor();
		setColor(color);
		f.call();
		setColor(old);
	}

	public final void withColor(@Nonnull Color color, @Nonnull Action1<Gfx> f) {
		Color old = getColor();
		setColor(color);
		f.call(this);
		setColor(old);
	}

	protected abstract void internalEndTick();

	protected abstract void prepareContext();

	public abstract void prepareSprites();

	public final void prepareSprites(@Nonnull Action0 f) {
		prepareSprites();
		f.call();
	}

	public final void prepareSprites(@Nonnull Action1<Gfx> f) {
		prepareSprites();
		f.call(this);
	}

	public abstract void prepareShapes(@Nonnull ShapeRenderer.ShapeType type);

	public final void prepareShapes(@Nonnull ShapeRenderer.ShapeType type, @Nonnull Action0 f) {
		prepareShapes(type);
		f.call();
	}

	public final void prepareShapes(@Nonnull ShapeRenderer.ShapeType type, @Nonnull Action1<Gfx> f) {
		prepareShapes(type);
		f.call(this);
	}

	public void draw(@Nonnull Renderable renderable, @Nonnull IVec2 v) {
		renderable.render(this, v);
	}

	public void draw(@Nonnull Renderable renderable, float x, float y) {
		draw(renderable, new ImmutableVec2(x, y));
	}

	public void draw(@Nonnull Renderable renderable) {
		draw(renderable, new ImmutableVec2());
	}

	public <S extends Shape & Shape.Filled> void drawFilled(@Nonnull S shape, @Nonnull IVec2 v) {
		shape.drawFilled(this, v);
	}

	public <S extends Shape & Shape.Filled> void drawFilled(@Nonnull S shape, float x, float y) {
		drawFilled(shape, new ImmutableVec2(x, y));
	}

	public <S extends Shape & Shape.Filled> void drawFilled(@Nonnull S shape) {
		drawFilled(shape, new ImmutableVec2());
	}

	public <S extends Shape & Shape.Outline> void drawOutline(@Nonnull S shape, @Nonnull IVec2 v) {
		shape.drawOutline(this, v);
	}

	public <S extends Shape & Shape.Outline> void drawOutline(@Nonnull S shape, float x, float y) {
		drawOutline(shape, new ImmutableVec2(x, y));
	}

	public <S extends Shape & Shape.Outline> void drawOutline(@Nonnull S shape) {
		drawOutline(shape, new ImmutableVec2());
	}

	public abstract void drawPoint(@Nonnull IVec2 v);

	public final void drawPoint(float x, float y) {
		drawPoint(new ImmutableVec2(x, y));
	}

	public abstract void clear(@Nonnull Color color);

	public final void clear() {
		clear(getColor());
	}

	public void updateCamera() {
		Viewport viewport = getViewport();
		if (viewport != null)
			viewport.apply();
		OrthographicCamera camera = getCamera();
		camera.update();
		updateCombinedCamera(camera.combined);
	}

	public abstract void updateCombinedCamera(@Nonnull Matrix4 matrix);
}