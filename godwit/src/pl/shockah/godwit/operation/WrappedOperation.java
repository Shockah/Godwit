package pl.shockah.godwit.operation;

import javax.annotation.Nonnull;

public abstract class WrappedOperation<Input, Output, WrappedInput, WrappedOutput> implements Operation<Input, Output> {
	@Nonnull public final Operation<WrappedInput, WrappedOutput> wrapped;

	public WrappedOperation(@Nonnull Operation<WrappedInput, WrappedOutput> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public float getProgress() {
		return wrapped.getProgress();
	}

	@Override
	@Nonnull public String getProgressDescription() {
		return wrapped.getProgressDescription();
	}

	@Override
	public float getWeight() {
		return wrapped.getWeight();
	}
}