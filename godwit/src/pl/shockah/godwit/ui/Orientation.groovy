package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
enum Orientation {
	Horizontal(new Vec2(1, 0)),
	Vertical(new Vec2(0, 1))

	@Nonnull final Vec2 vector

	private Orientation(@Nonnull Vec2 vector) {
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