package pl.shockah.godwit.fx.object;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.SequenceFx;

public class SequenceObjectFx<T> extends SequenceFx<ObjectFx<T>> implements ObjectFx<T> {
	@SafeVarargs
	public SequenceObjectFx(ObjectFx<T>... fxes) {
		super(fxes);
	}

	public SequenceObjectFx(@Nonnull List<? extends ObjectFx<T>> fxes) {
		super(fxes);
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

		FxResult<ObjectFx<T>> result = getFx(f);
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