package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Vec2;

public enum Orientation {
	Horizontal(new Vec2(1, 0)),
	Vertical(new Vec2(0, 1));

	@Nonnull
	public final Vec2 vector;

	Orientation(@Nonnull Vec2 vector) {
		this.vector = vector;
	}

	@Nonnull
	public Orientation getPerpendicular() {
		switch (this) {
			case Horizontal:
				return Vertical;
			case Vertical:
				return Horizontal;
		}
		throw new IllegalStateException();
	}
}