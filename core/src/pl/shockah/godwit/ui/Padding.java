package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

@EqualsAndHashCode
public final class Padding {
	public float left;
	public float right;
	public float top;
	public float bottom;

	public Padding() {
		this(0f);
	}

	public Padding(float padding) {
		this(padding, padding);
	}

	public Padding(float horizontal, float vertical) {
		this(horizontal, horizontal, vertical, vertical);
	}

	public Padding(float left, float right, float top, float bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	@Nonnull public IVec2 getTopLeftVector() {
		return new Vec2(left, top);
	}

	@Nonnull public IVec2 getBottomRightVector() {
		return new Vec2(right, bottom);
	}

	@Nonnull public IVec2 getVector() {
		return getTopLeftVector().add(getBottomRightVector());
	}
}