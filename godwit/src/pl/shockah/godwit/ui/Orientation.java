package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public enum Orientation {
	Horizontal(new ImmutableVec2(1, 0)),
	Vertical(new ImmutableVec2(0, 1));

	@Nonnull public final IVec2 vector;

	Orientation(@Nonnull IVec2 vector) {
		this.vector = vector;
	}

	@Nonnull public Orientation getPerpendicular() {
		switch (this) {
			case Horizontal:
				return Vertical;
			case Vertical:
				return Horizontal;
		}
		throw new IllegalStateException();
	}
}