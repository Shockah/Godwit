package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.gl.Gfx;

public class ComplexShape<S extends Shape> extends AbstractShape {
	public enum Operation {
		Union, Intersection, Difference;
	}

	@Nonnull
	public final S left;

	@Nonnull
	public final S right;

	@Nonnull
	public final Operation operation;

	public ComplexShape(@Nonnull S left, @Nonnull S right, @Nonnull Operation operation) {
		this.left = left;
		this.right = right;
		this.operation = operation;
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public ComplexShape<S> copy() {
		return new ComplexShape<>((S)left.copy(), (S)right.copy(), operation);
	}

	@Nonnull
	@Override
	public Rectangle getBoundingBox() {
		switch (operation) {
			case Union: {
				Rectangle leftRectangle = left.getBoundingBox();
				Rectangle rightRectangle = right.getBoundingBox();

				float minX = Math.min(leftRectangle.position.x, rightRectangle.position.x);
				float minY = Math.min(leftRectangle.position.y, rightRectangle.position.y);
				float maxX = Math.max(leftRectangle.position.x + leftRectangle.size.x, rightRectangle.position.x + rightRectangle.size.x);
				float maxY = Math.max(leftRectangle.position.y + leftRectangle.size.y, rightRectangle.position.y + rightRectangle.size.y);

				return new Rectangle(minX, minY, maxX - minX, maxY - minY);
			}
			default:
				throw new UnsupportedOperationException();
		}
	}

	@Nonnull
	@Override
	public ComplexShape<S> translate(@Nonnull IVec2 v) {
		left.translate(v);
		right.translate(v);
		return this;
	}

	@Nonnull
	@Override
	public ComplexShape<S> mirror(boolean horizontally, boolean vertically) {
		left.mirror(horizontally, vertically);
		right.mirror(horizontally, vertically);
		return this;
	}

	@Nonnull
	@Override
	public ComplexShape<S> scale(float scale) {
		left.scale(scale);
		right.scale(scale);
		return this;
	}

	public static class Filled<S extends Shape.Filled> extends ComplexShape<S> implements Shape.Filled {
		public Filled(@Nonnull S left, @Nonnull S right, @Nonnull Operation operation) {
			super(left, right, operation);
		}

		@Override
		public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			switch (operation) {
				case Union: {
					left.drawFilled(gfx, v);
					right.drawFilled(gfx, v);
				} break;
				default:
					throw new UnsupportedOperationException();
			}
		}

		@Override
		public boolean contains(@Nonnull IVec2 v) {
			switch (operation) {
				case Union:
					return left.contains(v) || right.contains(v);
				case Intersection:
					return left.contains(v) && right.contains(v);
				case Difference:
					return left.contains(v) && !right.contains(v);
				default:
					throw new UnsupportedOperationException();
			}
		}
	}

	public static class Outline<S extends Shape.Outline> extends ComplexShape<S> implements Shape.Outline {
		public Outline(@Nonnull S left, @Nonnull S right, @Nonnull Operation operation) {
			super(left, right, operation);
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			switch (operation) {
				case Union: {
					left.drawOutline(gfx, v);
					right.drawOutline(gfx, v);
				} break;
				default:
					throw new UnsupportedOperationException();
			}
		}
	}

	public static class Closed<S extends Shape.Filled & Shape.Outline> extends Filled<S> implements Shape.Outline {
		public Closed(@Nonnull S left, @Nonnull S right, @Nonnull Operation operation) {
			super(left, right, operation);
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			switch (operation) {
				case Union: {
					left.drawOutline(gfx, v);
					right.drawOutline(gfx, v);
				} break;
				default:
					throw new UnsupportedOperationException();
			}
		}
	}
}