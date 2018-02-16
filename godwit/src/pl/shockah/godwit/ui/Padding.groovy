package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.ImmutableVec2

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
final class Padding {
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

	@Nonnull IVec2 getTopLeftVector() {
		return new ImmutableVec2(left, top)
	}

	@Nonnull IVec2 getBottomRightVector() {
		return new ImmutableVec2(right, bottom)
	}

	@Nonnull IVec2 getVector() {
		return topLeftVector + bottomRightVector
	}
}