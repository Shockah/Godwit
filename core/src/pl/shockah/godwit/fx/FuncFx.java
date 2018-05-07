package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

public class FuncFx extends FxImpl {
	@Nonnull public final Func2 func;

	public FuncFx(float duration, @Nonnull Func func) {
		this(duration, (f, previous) -> func.call(f));
	}

	public FuncFx(float duration, @Nonnull Func2 func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(float f, float previous) {
		func.call(f, previous);
	}

	public interface Func {
		void call(float f);
	}

	public interface Func2 {
		void call(float f, float previous);
	}
}