package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.Viewport
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class GfxSlice implements Gfx {
	@Nonnull final Gfx wrapped
	@Nonnull final Rectangle bounds

	GfxSlice(@Nonnull Gfx wrapped, @Nonnull Rectangle bounds) {
		this.wrapped = wrapped
		this.bounds = bounds
	}

	@Override
	SpriteBatch getSpriteBatch() {
		return wrapped.spriteBatch
	}

	@Override
	ShapeRenderer getShapeRenderer() {
		return wrapped.shapeRenderer
	}

	@Override
	Viewport getViewport() {
		return wrapped.viewport
	}

	@Override
	void setViewport(@Nullable Viewport viewport) {
		wrapped.viewport = viewport
	}

	@Override
	OrthographicCamera getCamera() {
		return wrapped.camera
	}

	@Override
	void setCamera(@Nonnull OrthographicCamera camera) {
		wrapped.camera = camera
	}

	@Override
	Vec2 getOffset() {
		return wrapped.offset
	}

	@Override
	void setOffset(@Nonnull Vec2 offset) {
		wrapped.offset = offset
	}

	@Override
	int getWidth() {
		return bounds.size.x as int
	}

	@Override
	int getHeight() {
		return bounds.size.y as int
	}

	@Override
	BlendMode getBlendMode() {
		return wrapped.blendMode
	}

	@Override
	void setBlendMode(@Nonnull BlendMode blendMode) {
		wrapped.blendMode = blendMode
	}

	@Override
	Color getColor() {
		return wrapped.color
	}

	@Override
	void setColor(@Nonnull Color c) {
		wrapped.color = c
	}

	@Override
	void internalEndTick() {
		wrapped.internalEndTick()
	}

	@Override
	void prepareContext() {
		wrapped.prepareContext()
	}

	@Override
	void prepareSprites() {
		wrapped.prepareSprites()
	}

	@Override
	void prepareShapes(@Nonnull ShapeRenderer.ShapeType type) {
		wrapped.prepareShapes(type)
	}

	@Override
	void draw(@Nonnull Renderable renderable, float x, float y) {
		wrapped.draw(renderable, (x + bounds.position.x) as float, (y + bounds.position.y) as float)
	}

	@Override
	void drawFilled(@Nonnull Shape.Filled shape, float x, float y) {
		wrapped.drawFilled(shape, (x + bounds.position.x) as float, (y + bounds.position.y) as float)
	}

	@Override
	void drawOutline(@Nonnull Shape.Outline shape, float x, float y) {
		wrapped.drawOutline(shape, (x + bounds.position.x) as float, (y + bounds.position.y) as float)
	}

	@Override
	void drawPoint(float x, float y) {
		wrapped.drawPoint((x + bounds.position.x) as float, (y + bounds.position.y) as float)
	}

	@Override
	void clear(@Nonnull Color c) {
		wrapped.clear(c)
	}

	@Override
	void updateCombinedCamera(@Nonnull Matrix4 matrix) {
		wrapped.updateCombinedCamera(matrix)
	}
}