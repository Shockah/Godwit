package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.Viewport
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
trait Gfx {
	@Nonnull abstract SpriteBatch getSpriteBatch()

	@Nonnull abstract ShapeRenderer getShapeRenderer()

	@Nullable abstract Viewport getViewport()

	abstract void setViewport(@Nullable Viewport viewport)

	@Nonnull abstract OrthographicCamera getCamera()

	abstract void setCamera(@Nonnull OrthographicCamera camera)

	@Nonnull abstract Vec2 getOffset()

	abstract void setOffset(@Nonnull Vec2 offset)

	abstract int getWidth()

	abstract int getHeight()

	@Nonnull Vec2 getSize() {
		return new Vec2(width, height)
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

	abstract void internalEndTick()

	abstract void prepareContext()

	abstract void prepareSprites()

	abstract void prepareShapes(@Nonnull ShapeType type)

	abstract void draw(@Nonnull Sprite sprite, float x, float y)

	void draw(@Nonnull Sprite sprite, @Nonnull Vec2 pos) {
		draw(sprite, pos.x, pos.y)
	}

	void draw(@Nonnull Surface surface, float x, float y) {
		draw(surface.sprite, x, y)
	}

	void draw(@Nonnull Surface surface, @Nonnull Vec2 pos) {
		draw(surface.sprite, pos.x, pos.y)
	}

	abstract void drawFilled(@Nonnull Shape.Filled shape, float x, float y)

	void drawFilled(@Nonnull Shape.Filled shape) {
		drawFilled(shape, 0f, 0f)
	}

	void drawFilled(@Nonnull Shape.Filled shape, @Nonnull Vec2 pos) {
		drawFilled(shape, pos.x, pos.y)
	}

	abstract void drawOutline(@Nonnull Shape.Outline shape, float x, float y)

	void drawOutline(@Nonnull Shape.Outline shape) {
		drawOutline(shape, 0f, 0f)
	}

	void drawOutline(@Nonnull Shape.Outline shape, @Nonnull Vec2 pos) {
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
		viewport.setScreenPosition(offset.x as int, -offset.y as int)
		viewport.apply()
		camera.update()
		updateCombinedCamera(camera.combined)
	}

	abstract void updateCombinedCamera(@Nonnull Matrix4 matrix)
}