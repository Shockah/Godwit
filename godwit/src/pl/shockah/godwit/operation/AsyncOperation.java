package pl.shockah.godwit.operation;

import com.badlogic.gdx.utils.async.ThreadUtils;

import javax.annotation.Nonnull;

public class AsyncOperation<Input, Output> implements Runnable {
	@Nonnull public final Operation<Input, Output> operation;
	public final Input input;

	private Output output;

	@Nonnull private final Object lock = new Object();
	private boolean started = false;
	private boolean finished = false;

	public AsyncOperation(@Nonnull Operation<Input, Output> operation, Input input) {
		this.operation = operation;
		this.input = input;
	}

	public boolean isStarted() {
		synchronized (lock) {
			return started;
		}
	}

	public boolean isFinished() {
		synchronized (lock) {
			return finished;
		}
	}

	public Output waitAndGetOutput() {
		while (true) {
			synchronized (lock) {
				if (finished)
					return output;
			}
			ThreadUtils.yield();
		}
	}

	public Output getOutput() {
		synchronized (lock) {
			return output;
		}
	}

	@Override
	public void run() {
		synchronized (lock) {
			if (started)
				throw new IllegalStateException("AsyncOperation can only be started once.");
			started = true;
		}
		Output output = operation.run(input);
		synchronized (lock) {
			this.output = output;
			finished = true;
		}
	}
}