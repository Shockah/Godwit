package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.ImmutableVec2

import javax.annotation.Nonnull

@CompileStatic
enum Orientation {
	Horizontal(new ImmutableVec2(1, 0)),
	Vertical(new ImmutableVec2(0, 1))

	@Nonnull final IVec2 vector

	private Orientation(@Nonnull IVec2 vector) {
		this.vector = vector
	}

	@Nonnull Orientation getPerpendicular() {
		switch (this) {
			case Horizontal:
				return Vertical
			case Vertical:
				return Horizontal
		}
		throw new IllegalStateException()
	}
}