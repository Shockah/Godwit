package pl.shockah.godwit.operation;

import javax.annotation.Nonnull;

public class ChainOperation<Input, IntermediateOutput, Output> implements Operation<Input, Output> {
	@Nonnull private final Operation<Input, IntermediateOutput> firstOperation;
	@Nonnull private final Operation<IntermediateOutput, Output> secondOperation;

	private boolean executing = false;
	private final Object lock = new Object();

	public ChainOperation(@Nonnull Operation<Input, IntermediateOutput> firstOperation, @Nonnull Operation<IntermediateOutput, Output> secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}

	@Override
	public float getWeight() {
		return firstOperation.getWeight() + secondOperation.getWeight();
	}

	@Override
	public float getProgress() {
		float totalWeight = firstOperation.getWeight() + secondOperation.getWeight();
		return (firstOperation.getProgress() * firstOperation.getWeight() + secondOperation.getProgress() * secondOperation.getWeight()) / totalWeight;
	}

	@Override
	@Nonnull public String getProgressDescription() {
		return firstOperation.getProgress() < 1f ? firstOperation.getProgressDescription() : secondOperation.getProgressDescription();
	}

	@Override
	@Nonnull public Output run(@Nonnull Input input) {
		synchronized (lock) {
			if (executing)
				throw new IllegalStateException("Operation is already being executed.");
			executing = true;
		}
		IntermediateOutput intermediateOutput = firstOperation.run(input);
		Output output = secondOperation.run(intermediateOutput);
		synchronized (lock) {
			executing = false;
		}
		return output;
	}
}