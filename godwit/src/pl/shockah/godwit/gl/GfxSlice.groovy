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
	@Nonnull @Delegate final Gfx wrapped
	@Nonnull final Rectangle bounds

	GfxSlice(@Nonnull Gfx wrapped, @Nonnull Rectangle bounds) {
		this.wrapped = wrapped
		this.bounds = bounds
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
}