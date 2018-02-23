package pl.shockah.godwit.fx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.godwit.fx.ease.Easing;
import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.object.SequenceObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;
import pl.shockah.godwit.fx.raw.SequenceRawFx;

public abstract class SequenceFx<F extends Fx> implements Fx {
	@Nonnull protected final List<F> fxes;

	@SafeVarargs
	public SequenceFx(F... fxes) {
		this(Arrays.asList(fxes));
	}

	public SequenceFx(@Nonnull List<? extends F> fxes) {
		this.fxes = new ArrayList<>(fxes);
	}

	public static SequenceRawFx ofRaw(RawFx... fxes) {
		return new SequenceRawFx(fxes);
	}

	public static SequenceRawFx ofRaw(List<? extends RawFx> fxes) {
		return new SequenceRawFx(fxes);
	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T2> SequenceObjectFx<T2> ofObject(ObjectFx<? extends T2>... fxes) {
		return new SequenceObjectFx<>((ObjectFx<T2>[]) fxes);
	}

	@SuppressWarnings("unchecked")
	public static <T2> SequenceObjectFx<T2> ofObject(List<? extends ObjectFx<? extends T2>> fxes) {
		return new SequenceObjectFx<>((List<? extends ObjectFx<T2>>) fxes);
	}

	@Override
	public float getDuration() {
		return (float)StreamSupport.stream(fxes).mapToDouble(Fx::getDuration).sum();
	}

	@Override
	@Nonnull public final Easing getMethod() {
		return Easing.linear;
	}

	@Override
	public void setMethod(@Nonnull Easing method) {
		throw new UnsupportedOperationException();
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