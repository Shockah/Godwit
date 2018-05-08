package pl.shockah.godwit.fx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.unicorn.ease.Easing;

public class SequenceFx implements Fx {
	@Nonnull protected final List<Fx> fxes;

	@Getter(lazy = true)
	private final float duration = calculateDuration(fxes);

	public SequenceFx(Fx... fxes) {
		this(Arrays.asList(fxes));
	}

	public SequenceFx(@Nonnull List<Fx> fxes) {
		this.fxes = Collections.unmodifiableList(new ArrayList<>(fxes));
	}

	private float calculateDuration(@Nonnull List<Fx> fxes) {
		return (float)StreamSupport.stream(fxes).mapToDouble(Fx::getDuration).sum();
	}

	@Override
	@Nonnull public final Easing getMethod() {
		return Easing.linear;
	}

	@Override
	public void setMethod(@Nonnull Easing method) {
		throw new UnsupportedOperationException("Cannot set an Easing method on a SequenceFx.");
	}

	@Nonnull protected FxResult getFx(float f, boolean includingEqual) {
		float fStart = 0f;
		for (int i = 0; i < fxes.size(); i++) {
			Fx fx = fxes.get(i);
			float duration = fx.getDuration();
			if (includingEqual) {
				if (f - fStart <= duration)
					return new FxResult(fx, i, fStart, fStart + fx.getDuration());
			} else {
				if (f - fStart < duration)
					return new FxResult(fx, i, fStart, fStart + fx.getDuration());
			}
			fStart += duration;
		}
		int lastIndex = fxes.size() - 1;
		Fx last = fxes.get(lastIndex);
		return new FxResult(last, lastIndex, fStart - last.getDuration(), fStart);
	}

	@Nonnull protected List<Fx> getFxesToFinish(float f, float previous) {
		FxResult resultPrevious = getFx(previous, true);
		FxResult result = getFx(f, false);

		List<Fx> results = new ArrayList<>();
		if (resultPrevious.index != result.index) {
			if (f > previous) {
				for (int i = resultPrevious.index; i < result.index; i++) {
					results.add(fxes.get(i));
				}
			} else if (f < previous) {
				for (int i = resultPrevious.index; i > result.index; i--) {
					results.add(fxes.get(i));
				}
			}
		}
		return results;
	}

	protected void update(float f, float previous, boolean finish) {
		if (fxes.isEmpty())
			return;

		float duration = getDuration();
		f *= duration;
		previous *= duration;

		for (Fx fx : getFxesToFinish(f, previous)) {
			float value = f > previous ? 1f : 0f;
			fx.finish(value, value);
		}

		FxResult result = getFx(f, false);
		if (finish)
			result.fx.finish(result.getFxF(f), result.getFxF(previous));
		else
			result.fx.update(result.getEased(f), result.getEased(previous));
	}

	@Override
	public void update(float f, float previous) {
		update(f, previous, false);
	}

	@Override
	public void finish(float f, float previous) {
		update(f, previous, true);
	}

	protected static final class FxResult {
		public final Fx fx;
		public final int index;
		public final float a;
		public final float b;

		FxResult(Fx fx, int index, float a, float b) {
			this.fx = fx;
			this.index = index;
			this.a = a;
			this.b = b;
		}

		public float getFxF(float f) {
			return Math.min(Math.max((f - a) / (b - a), 0f), 1f);
		}

		public float getEased(float f) {
			return fx.getMethod().ease(getFxF(f));
		}
	}
}