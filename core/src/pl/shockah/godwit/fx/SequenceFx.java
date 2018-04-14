package pl.shockah.godwit.fx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.godwit.fx.ease.Easing;

public abstract class SequenceFx<F extends Fx> implements Fx {
	@Nonnull protected final List<F> fxes;

	@Getter(lazy = true)
	private final float duration = calculateDuration();

	@SafeVarargs
	public SequenceFx(F... fxes) {
		this(Arrays.asList(fxes));
	}

	public SequenceFx(@Nonnull List<? extends F> fxes) {
		this.fxes = Collections.unmodifiableList(new ArrayList<>(fxes));
	}

	private float calculateDuration() {
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

	@Nonnull protected FxResult<F> getFx(float f, boolean includingEqual) {
		float fStart = 0f;
		for (int i = 0; i < fxes.size(); i++) {
			F fx = fxes[i];
			float duration = fx.getDuration();
			if (includingEqual) {
				if (f - fStart <= duration)
					return new FxResult<>(fx, i, fStart, fStart + fx.getDuration());
			} else {
				if (f - fStart < duration)
					return new FxResult<>(fx, i, fStart, fStart + fx.getDuration());
			}
			fStart += duration;
		}
		int lastIndex = fxes.size() - 1;
		F last = fxes[lastIndex];
		return new FxResult<>(last, lastIndex, fStart - last.getDuration(), fStart);
	}

	@Nonnull protected List<F> getFxesToFinish(float f, float previous) {
		FxResult<F> resultPrevious = getFx(previous, true);
		FxResult<F> result = getFx(f, false);

		List<F> results = new ArrayList<>();
		if (resultPrevious.index != result.index) {
			if (f > previous) {
				for (int i = resultPrevious.index; i < result.index; i++) {
					results.add(fxes[i]);
				}
			} else if (f < previous) {
				for (int i = resultPrevious.index; i > result.index; i--) {
					results.add(fxes[i]);
				}
			}
		}
		return results;
	}

	protected static final class FxResult<F extends Fx> {
		public final F fx;
		public final int index;
		public final float a;
		public final float b;

		FxResult(F fx, int index, float a, float b) {
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