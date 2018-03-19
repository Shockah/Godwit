package pl.shockah.godwit.operation;

import javax.annotation.Nonnull;

public interface Operation<Input, Output> {
	float getProgress();
	@Nonnull String getProgressDescription();
	float getWeight();
	@Nonnull Output run(Input input);

	@Nonnull default <T> ChainOperation<Input, Output, T> chain(@Nonnull Operation<Output, T> operation) {
		return ChainOperation.chain(this, operation);
	}

	@Nonnull default <T> ChainOperation<Input, OperationResult<Input, Output>, T> chainResult(@Nonnull Operation<OperationResult<Input, Output>, T> operation) {
		return ChainOperation.chainResult(this, operation);
	}

	@Nonnull default AsyncOperation<Input, Output> async(@Nonnull Input input) {
		return new AsyncOperation<>(this, input);
	}
}