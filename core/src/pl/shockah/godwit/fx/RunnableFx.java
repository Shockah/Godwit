package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

public class RunnableFx extends FxImpl {
	@Nonnull
	protected final Runnable action;

	public RunnableFx(@Nonnull Runnable action) {
		super(0f);
		this.action = action;
	}

	@Override
	public final void update(float f, float previous) {
	}

	@Override
	public void finish(float f, float previous) {
		action.run();
	}
}