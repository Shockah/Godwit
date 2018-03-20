package pl.shockah.godwit.operation;

import javax.annotation.Nonnull;

import pl.shockah.func.Func1;
import pl.shockah.func.Func2;

public final class FuncOperation<Input, Output> extends AbstractOperation<Input, Output> {
	@Nonnull private final Func2<FuncOperation<Input, Output>, Input, Output> func;

	public FuncOperation(@Nonnull Func1<Input, Output> func) {
		this(1f, func);
	}

	public FuncOperation(@Nonnull Func2<FuncOperation<Input, Output>, Input, Output> func) {
		this(1f, func);
	}

	public FuncOperation(float weight, @Nonnull Func1<Input, Output> func) {
		this(weight, (operation, input) -> func.call(input));
	}

	public FuncOperation(float weight, @Nonnull Func2<FuncOperation<Input, Output>, Input, Output> func) {
		super(weight);
		this.func = func;
	}

	@Override
	public void setProgress(float progress) {
		super.setProgress(progress);
	}

	@Override
	@Nonnull protected Output execute(@Nonnull Input input) {
		return func.call(this, input);
	}
}