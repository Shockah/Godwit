package pl.shockah.godwit.fx.raw;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.SequenceFx;

public class SequenceRawFx extends SequenceFx<RawFx> implements RawFx {
	public SequenceRawFx(RawFx... fxes) {
		super(fxes);
	}

	public SequenceRawFx(@Nonnull List<? extends RawFx> fxes) {
		super(fxes);
	}

	protected void update(float f, float previous, boolean finish) {
		if (fxes.isEmpty())
			return;

		float duration = getDuration();
		f *= duration;
		previous *= duration;

		for (RawFx fx : getFxesToFinish(f, previous)) {
			float value = f > previous ? 1f : 0f;
			fx.finish(value, value);
		}

		FxResult<RawFx> result = getFx(f);
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
}