package pl.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class GfxImpl implements Gfx {
	@Nonnull OrthographicCamera camera = new OrthographicCamera()
	@Nullable Viewport viewport
	@Nonnull Vec2 offset = new Vec2()

	private boolean spritesMode = false
	@Nonnull private final SpriteBatch sprites = new SpriteBatch()
	@Nonnull private final ShapeRenderer shapes = new ShapeRenderer()
	@Nonnull private Color color = Color.BLACK
	@Nullable private ShapeRenderer.ShapeType shapesMode
	@Nullable private BlendMode blendMode

	GfxImpl() {
		viewport = new ScreenViewport(camera)
	}

	@Override
	SpriteBatch getSpriteBatch() {
		return sprites
	}

	@Override
	ShapeRenderer getShapeRenderer() {
		return shapes
	}

	int getWidth() {
		return Gdx.graphics.width
	}

	int getHeight() {
		return Gdx.graphics.height
	}

	void prepareContext() {
		GfxContextManager.bindSurface(null)
	}

	void prepareSprites() {
		prepareContext()
		if (spritesMode)
			return
		if (shapesMode) {
			shapes.end()
			shapesMode = null
		}
		sprites.begin()
		spritesMode = true
	}

	void prepareShapes(@Nonnull ShapeRenderer.ShapeType type) {
		prepareContext()
		if (shapesMode == type)
			return
		if (spritesMode) {
			sprites.end()
			spritesMode = false
		}
		if (shapesMode)
			shapes.end()
		shapes.begin(type)
		shapesMode = type
	}

	@Override
	BlendMode getBlendMode() {
		return null
	}

	void setBlendMode(@Nonnull BlendMode mode) {
		prepareContext()
		if (blendMode == mode)
			return
		if (blendMode)
			blendMode.end()
		blendMode = mode
		mode.begin()
	}

	void endTick() {
		prepareContext()
		internalEndTick()
	}

	void internalEndTick() {
		if (spritesMode) {
			sprites.end()
			spritesMode = false
		}
		if (shapesMode) {
			shapes.end()
			shapesMode = null
		}
	}

	@Nonnull Color getColor() {
		return color
	}

	void setColor(@Nonnull Color c) {
		prepareContext()
		color = c
		sprites.color = c
		shapes.color = c
	}

	void setColor(float r, float g, float b, float a) {
		prepareContext()
		color = new Color(r, g, b, a)
		sprites.setColor(r, g, b, a)
		shapes.setColor(r, g, b, a)
	}

	void setColor(float r, float g, float b) {
		prepareContext()
		color = new Color(r, g, b, 1f)
		sprites.setColor(r, g, b, 1f)
		shapes.setColor(r, g, b, 1f)
	}

	void draw(@Nonnull Renderable renderable, float x, float y) {
		renderable.onRender(this, x, y)
	}

	void draw(@Nonnull Renderable renderable, @Nonnull Vec2 pos) {
		renderable.onRender(this, pos.x, pos.y)
	}

	void drawFilled(@Nonnull Shape.Filled shape) {
		prepareContext()
		shape.drawFilled(this, 0f, 0f)
	}

	void drawFilled(@Nonnull Shape.Filled shape, float x, float y) {
		prepareContext()
		shape.drawFilled(this, x, y)
	}

	void drawFilled(@Nonnull Shape.Filled shape, @Nonnull Vec2 pos) {
		prepareContext()
		shape.drawFilled(this, pos)
	}

	void drawOutline(@Nonnull Shape.Outline shape) {
		prepareContext()
		shape.drawOutline(this, 0f, 0f)
	}

	void drawOutline(@Nonnull Shape.Outline shape, float x, float y) {
		prepareContext()
		shape.drawOutline(this, x, y)
	}

	void drawOutline(@Nonnull Shape.Outline shape, @Nonnull Vec2 pos) {
		prepareContext()
		shape.drawOutline(this, pos)
	}

	void drawPoint(float x, float y) {
		prepareShapes(ShapeRenderer.ShapeType.Point)
		shapes.point(x, y, 0f)
	}

	void drawPoint(@Nonnull Vec2 pos) {
		drawPoint(pos.x, pos.y)
	}

	void clear() {
		clear(color)
	}

	void clear(@Nonnull Color c) {
		prepareContext()
		Gdx.gl20.glClearColor(c.r, c.g, c.b, c.a)
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT | (Gdx.graphics.bufferFormat.coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0) as int)
	}

	void updateCamera() {
		internalEndTick()
		Gfx.super.updateCamera()
	}

	void updateCombinedCamera(@Nonnull Matrix4 matrix) {
		sprites.projectionMatrix = matrix
		shapes.projectionMatrix = matrix
	}
}