package pl.shockah.godwit.operation;

import com.badlogic.gdx.math.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;

public abstract class AbstractOperation<Input, Output> implements Operation<Input, Output> {
	@Getter
	public final float weight;

	private float progress = 0f;
	@Nullable private String progressDescription = null;

	private boolean executing = false;
	private final Object lock = new Object();

	public AbstractOperation() {
		this(1f);
	}

	public AbstractOperation(float weight) {
		this.weight = weight;
	}

	public float getProgress() {
		synchronized (lock) {
			return progress;
		}
	}

	@Override
	@Nonnull public String getProgressDescription() {
		return progressDescription != null ? progressDescription : String.format("%.1f%%", progress * 100f);
	}

	protected void setProgress(float progress) {
		setProgress(progress, null);
	}

	protected void setProgress(float progress, @Nullable String description) {
		synchronized (lock) {
			this.progress = MathUtils.clamp(progress, 0f, 1f);
			progressDescription = description;
		}
	}

	@Override
	@Nonnull public final Output run(@Nonnull Input input) {
		synchronized (lock) {
			if (executing)
				throw new IllegalStateException("Operation is already being executed.");
			executing = true;
			progress = 0f;
		}
		Output output = execute(input);
		synchronized (lock) {
			executing = false;
			progress = 1f;
		}
		return output;
	}

	@Nonnull protected abstract Output execute(@Nonnull Input input);

	// Retrolambda hates generic default methods

	@Override
	public <T> ChainOperation<Input, Output, T> chain(@Nonnull Operation<Output, T> operation) {
		return ChainOperation.chain(this, operation);
	}

	@Override
	public <T> ChainOperation<Input, OperationResult<Input, Output>, T> chainResult(@Nonnull Operation<OperationResult<Input, Output>, T> operation) {
		return ChainOperation.chainResult(this, operation);
	}
}