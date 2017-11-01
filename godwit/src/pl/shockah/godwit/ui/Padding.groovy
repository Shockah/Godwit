package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
class Padding {
	float left
	float right
	float top
	float bottom

	Padding() {
		this(0f)
	}

	Padding(float padding) {
		this(padding, padding)
	}

	Padding(float horizontal, float vertical) {
		this(horizontal, horizontal, vertical, vertical)
	}

	Padding(float left, float right, float top, float bottom) {
		this.left = left
		this.right = right
		this.top = top
		this.bottom = bottom
	}

	@Nonnull Vec2 getTopLeftVector() {
		return new Vec2(left, top)
	}

	@Nonnull Vec2 getBottomRightVector() {
		return new Vec2(right, bottom)
	}

	@Nonnull Vec2 getVector() {
		return topLeftVector + bottomRightVector
	}
}