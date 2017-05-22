package pl.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Matrix4
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2

@CompileStatic
class Gfx {
	final SpriteBatch sprites = new SpriteBatch()
	final ShapeRenderer shapes = new ShapeRenderer()

	private boolean spritesMode = false
	private Color color = Color.BLACK
	private ShapeRenderer.ShapeType shapesMode
	private BlendMode blendMode

	int getWidth() {
		return Gdx.graphics.width
	}

	int getHeight() {
		return Gdx.graphics.height
	}

	final Vec2 getSize() {
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

	void prepareShapes(ShapeType type) {
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

	void setBlendMode(BlendMode mode) {
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

	Color getColor() {
		return color
	}

	void setColor(Color c) {
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

	void draw(Sprite sprite, float x, float y) {
		prepareSprites()
		sprite.draw(sprites, x, y)
	}

	void draw(Sprite sprite, Vec2 pos) {
		prepareSprites()
		sprite.draw(sprites, pos.x, pos.y)
	}

	void draw(Surface surface, float x, float y) {
		surface.draw(surface.sprite, x, y)
	}

	void draw(Surface surface, Vec2 pos) {
		surface.draw(surface.sprite, pos.x, pos.y)
	}

	void draw(Shape shape, boolean filled) {
		prepareContext()
		shape.draw(this, filled, 0f, 0f)
	}

	void draw(Shape shape, boolean filled, float x, float y) {
		prepareContext()
		shape.draw(this, filled, x, y)
	}

	void draw(Shape shape, boolean filled, Vec2 pos) {
		prepareContext()
		shape.draw(this, filled, pos.x, pos.y)
	}

	void drawPoint(float x, float y) {
		prepareShapes(ShapeType.Point)
		shapes.point(x, y, 0f)
	}

	void drawPoint(Vec2 pos) {
		drawPoint(pos.x, pos.y)
	}

	void clear() {
		clear(color)
	}

	void clear(Color c) {
		prepareContext()
		Gdx.gl20.glClearColor(c.r, c.g, c.b, c.a)
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT | (Gdx.graphics.bufferFormat.coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0) as int)
	}

	void updateCamera() {
		def camera = Godwit.instance.camera
		camera.viewportWidth = Gdx.graphics.width
		camera.viewportHeight = -Gdx.graphics.height
		camera.position.set(camera.viewportWidth * 0.5f as float, -camera.viewportHeight * 0.5f as float, 0f)
		camera.update()
		updateCombinedCamera(camera.combined)
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
	}

	protected final void updateCombinedCamera(Matrix4 matrix) {
		sprites.projectionMatrix = matrix
		shapes.projectionMatrix = matrix
	}
}