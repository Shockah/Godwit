package pl.shockah.godwit.fx.object;

import javax.annotation.Nonnull;

public class ObjectFuncFx<T> extends ObjectFxImpl<T> {
	@Nonnull public final Func2<T> func;

	public ObjectFuncFx(float duration, @Nonnull Func<T> func) {
		this(duration, (object, f, previous) -> func.call(object, f));
	}

	public ObjectFuncFx(float duration, @Nonnull Func2<T> func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		func.call(object, f, previous);
	}

	public interface Func<T> {
		void call(@Nonnull T object, float f);
	}

	public interface Func2<T> {
		void call(@Nonnull T object, float f, float previous);
	}
}