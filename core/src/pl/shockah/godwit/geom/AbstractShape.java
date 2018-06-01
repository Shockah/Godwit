package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.polygon.Polygonable;

public abstract class AbstractShape implements Shape {
	@Nonnull
	@Override
	public final AbstractShape translate(float x, float y) {
		translate(new Vec2(x, y));
		return this;
	}

	@Override
	public final boolean collides(@Nonnull Shape shape) {
		return collides(shape, false);
	}

	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (secondTry) {
			if (this instanceof Polygonable && shape instanceof Polygonable)
				return ((Polygonable)this).asPolygon().collides((Polygonable)shape);
			else
				throw new UnsupportedOperationException(String.format("%s --><-- %s collision isn't implemented.", getClass().getSimpleName(), shape.getClass().getSimpleName()));
		} else {
			return shape.collides(this, true);
		}
	}
}