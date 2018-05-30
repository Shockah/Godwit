package pl.shockah.godwit;

import com.badlogic.gdx.InputMultiplexer;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class BaseInputManager<T> {
	@Nonnull
	protected abstract List<T> getProcessors();

	@Nonnull
	protected final InputMultiplexer multiplexer = new InputMultiplexer();

	public void addProcessor(@Nonnull T processor) {
		getProcessors().add(processor);
		resetupMultiplexer();
	}

	public void removeProcessor(@Nonnull T processor) {
		getProcessors().remove(processor);
		resetupMultiplexer();
	}

	protected abstract void resetupMultiplexer();
}