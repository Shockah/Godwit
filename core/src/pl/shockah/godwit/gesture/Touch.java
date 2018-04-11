package pl.shockah.godwit.gesture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public final class Touch {
	public final int pointer;
	@Nonnull public final List<Point> points = new ArrayList<>();

	@Getter
	@Nullable private ContinuousGestureRecognizer continuousRecognizer = null;

	@Getter
	private boolean finished = false;

	public Touch(int pointer) {
		this.pointer = pointer;
	}

	public void addPoint(float x, float y) {
		addPoint(new Vec2(x, y));
	}

	public void addPoint(@Nonnull IVec2 position) {
		if (position instanceof Vec2)
			addPoint((Vec2)position);
		else
			addPoint(position.getImmutableCopy());
	}

	public void addPoint(@Nonnull Vec2 position) {
		if (finished)
			return;
		points.add(new Point(position));
	}

	public void finish() {
		finished = true;
	}

	public void setContinuousRecognizer(@Nullable ContinuousGestureRecognizer recognizer) {
		continuousRecognizer = recognizer;
		if (recognizer != null) {
			for (ContinuousGestureRecognizer continuousRecognizer : new ArrayList<>(Godwit.getInstance().inputManager.gestureManager.currentContinuousRecognizers)) {
				if (continuousRecognizer == recognizer)
					continue;
				continuousRecognizer.onTouchUsedByContinuousRecognizer(this);
			}
		}
	}

	public static final class Point {
		@Nonnull public final Vec2 position;
		@Nonnull public final Date date;

		public Point(@Nonnull Vec2 position) {
			this(position, new Date());
		}

		public Point(@Nonnull Vec2 position, @Nonnull Date date) {
			this.position = position;
			this.date = date;
		}
	}
}