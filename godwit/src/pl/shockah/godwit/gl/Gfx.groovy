package pl.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class Gfx {
	@Nonnull final SpriteBatch sprites = new SpriteBatch()
	@Nonnull final ShapeRenderer shapes = new ShapeRenderer()

	@Nonnull OrthographicCamera camera = new OrthographicCamera()
	@Nullable Viewport viewport
	@Nonnull Vec2 offset = new Vec2()

	private boolean spritesMode = false
	@Nonnull private Color color = Color.BLACK
	@Nullable private ShapeType shapesMode
	@Nullable private BlendMode blendMode

	Gfx() {
		viewport = new ScreenViewport(camera)
	}

	int getWidth() {
		return Gdx.graphics.width
	}

	int getHeight() {
		return Gdx.graphics.height
	}

	@Nonnull final Vec2 getSize() {
		return new Vec2(width, height)
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

	void prepareShapes(@Nonnull ShapeType type) {
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

	protected void internalEndTick() {
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

	void draw(@Nonnull Sprite sprite, float x, float y) {
		prepareSprites()
		sprite.draw(sprites, x, y)
	}

	void draw(@Nonnull Sprite sprite, @Nonnull Vec2 pos) {
		prepareSprites()
		sprite.draw(sprites, pos.x, pos.y)
	}

	void draw(@Nonnull Surface surface, float x, float y) {
		draw(surface.sprite, x, y)
	}

	void draw(@Nonnull Surface surface, @Nonnull Vec2 pos) {
		draw(surface.sprite, pos.x, pos.y)
	}

	void draw(@Nonnull Shape shape, boolean filled) {
		prepareContext()
		shape.draw(this, filled, 0f, 0f)
	}

	void draw(@Nonnull Shape shape, boolean filled, float x, float y) {
		prepareContext()
		shape.draw(this, filled, x, y)
	}

	void draw(@Nonnull Shape shape, boolean filled, @Nonnull Vec2 pos) {
		prepareContext()
		shape.draw(this, filled, pos.x, pos.y)
	}

	void drawPoint(float x, float y) {
		prepareShapes(ShapeType.Point)
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
		viewport.setScreenPosition(offset.x as int, -offset.y as int)
		viewport.apply()
		camera.update()
		updateCombinedCamera(camera.combined)
	}

	protected final void updateCombinedCamera(@Nonnull Matrix4 matrix) {
		sprites.projectionMatrix = matrix
		shapes.projectionMatrix = matrix
	}
}