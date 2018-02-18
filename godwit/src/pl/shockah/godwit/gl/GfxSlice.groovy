package pl.shockah.godwit.gl

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.Shape

import javax.annotation.Nonnull

@CompileStatic
class GfxSlice extends Gfx {
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