package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.geom.Vec2;

@EqualsAndHashCode
public final class Padding {
	@Nonnull
	public Unit left = Unit.Zero;

	@Nonnull
	public Unit right = Unit.Zero;

	@Nonnull
	public Unit top = Unit.Zero;

	@Nonnull
	public Unit bottom = Unit.Zero;

	public Padding() {
		this(Unit.Zero);
	}

	public Padding(float padding) {
		this(padding, padding);
	}

	public Padding(@Nonnull Unit padding) {
		this(padding, padding);
	}

	public Padding(float horizontal, float vertical) {
		this(horizontal, horizontal, vertical, vertical);
	}

	public Padding(@Nonnull Unit horizontal, @Nonnull Unit vertical) {
		this(horizontal, horizontal, vertical, vertical);
	}

	public Padding(float left, float right, float top, float bottom) {
		this(new Unit.Pixels(left), new Unit.Pixels(right), new Unit.Pixels(top), new Unit.Pixels(bottom));
	}

	public Padding(@Nonnull Unit left, @Nonnull Unit right, @Nonnull Unit top, @Nonnull Unit bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	@Nonnull
	public Vec2 getTopLeftVector() {
		return new Vec2(left.getPixels(), top.getPixels());
	}

	@Nonnull
	public Vec2 getBottomRightVector() {
		return new Vec2(right.getPixels(), bottom.getPixels());
	}

	@Nonnull
	public Vec2 getVector() {
		return getTopLeftVector().add(getBottomRightVector());
	}
}