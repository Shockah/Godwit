package pl.shockah.godwit.operation;

import javax.annotation.Nonnull;

public final class OperationResult<Input, Output> {
	@Nonnull public final Operation<Input, Output> operation;
	public final Input input;
	public final Output output;

	public OperationResult(@Nonnull Operation<Input, Output> operation, Input input, Output output) {
		this.operation = operation;
		this.input = input;
		this.output = output;
	}
}