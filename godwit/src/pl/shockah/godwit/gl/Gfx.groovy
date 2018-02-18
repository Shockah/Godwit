package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.Viewport
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.*

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
abstract class Gfx {
	@Nonnull abstract SpriteBatch getSpriteBatch()

	@Nonnull abstract ShapeRenderer getShapeRenderer()

	@Nullable abstract Viewport getViewport()

	abstract void setViewport(@Nullable Viewport viewport)

	@Nonnull abstract OrthographicCamera getCamera()

	abstract void setCamera(@Nonnull OrthographicCamera camera)

	@Nonnull abstract IVec2 getCameraPosition()

	abstract void setCameraPosition(@Nonnull IVec2 position)

	abstract void resetCamera()

	abstract int getWidth()

	abstract int getHeight()

	@Nonnull IVec2 getSize() {
		return new ImmutableVec2(width, height)
	}

	@Nonnull abstract BlendMode getBlendMode()

	abstract void setBlendMode(@Nonnull BlendMode blendMode)

	@Nonnull abstract Color getColor()

	abstract void setColor(@Nonnull Color c)

	void setColor(float r, float g, float b, float a) {
		setColor(new Color(r, g, b, a))
	}

	void setColor(float r, float g, float b) {
		setColor(new Color(r, g, b, 1.0f))
	}

	void withColor(@Nonnull Color c, @DelegatesTo(Gfx) @Nonnull Closure closure) {
		Color oldColor = color
		color = c
		with(closure)
		color = oldColor
	}

	abstract void internalEndTick()

	abstract void prepareContext()

	abstract void prepareSprites()

	void prepareSprites(@DelegatesTo(SpriteBatch) @Nonnull Closure closure) {
		prepareSprites()
		spriteBatch.with(closure)
	}

	abstract void prepareShapes(@Nonnull ShapeType type)

	void prepareShapes(@Nonnull ShapeType type, @DelegatesTo(ShapeRenderer) @Nonnull Closure closure) {
		prepareShapes(type)
		shapeRenderer.with(closure)
	}

	abstract void draw(@Nonnull Renderable renderable, float x, float y)

	void draw(@Nonnull Renderable renderable, @Nonnull IVec2 pos) {
		draw(renderable, pos.x, pos.y)
	}

	abstract void drawFilled(@Nonnull Shape.Filled shape, float x, float y)

	void drawFilled(@Nonnull Shape.Filled shape) {
		drawFilled(shape, 0f, 0f)
	}

	void drawFilled(@Nonnull Shape.Filled shape, @Nonnull IVec2 pos) {
		drawFilled(shape, pos.x, pos.y)
	}

	abstract void drawOutline(@Nonnull Shape.Outline shape, float x, float y)

	void drawOutline(@Nonnull Shape.Outline shape) {
		drawOutline(shape, 0f, 0f)
	}

	void drawOutline(@Nonnull Shape.Outline shape, @Nonnull IVec2 pos) {
		drawOutline(shape, pos.x, pos.y)
	}

	abstract void drawPoint(float x, float y)

	void drawPoint(@Nonnull Vec2 pos) {
		drawPoint(pos.x, pos.y)
	}

	void clear() {
		clear(color)
	}

	abstract void clear(@Nonnull Color c)

	void updateCamera() {
		viewport.apply()
		camera.update()
		updateCombinedCamera(camera.combined)
	}

	abstract void updateCombinedCamera(@Nonnull Matrix4 matrix)
}