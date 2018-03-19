package pl.shockah.godwit.fx.object;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.SequenceFx;

public class SequenceObjectFx<T> extends SequenceFx<ObjectFx<T>> implements ObjectFx<T> {
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public SequenceObjectFx(ObjectFx<? super T>... fxes) {
		super((ObjectFx<T>[])fxes);
	}

	@SuppressWarnings("unchecked")
	public SequenceObjectFx(@Nonnull List<? extends ObjectFx<? super T>> fxes) {
		super((List<? extends ObjectFx<T>>)fxes);
	}

	protected void update(@Nonnull T object, float f, float previous, boolean finish) {
		if (fxes.isEmpty())
			return;

		float duration = getDuration();
		f *= duration;
		previous *= duration;

		for (ObjectFx<T> fx : getFxesToFinish(f, previous)) {
			float value = f > previous ? 1f : 0f;
			fx.finish(object, value, value);
		}

		FxResult<ObjectFx<T>> result = getFx(f, false);
		if (finish)
			result.fx.finish(object, result.getFxF(f), result.getFxF(previous));
		else
			result.fx.update(object, result.getEased(f), result.getEased(previous));
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		update(object, f, previous, false);
	}

	@Override
	public void finish(@Nonnull T object, float f, float previous) {
		update(object, f, previous, true);
	}
}