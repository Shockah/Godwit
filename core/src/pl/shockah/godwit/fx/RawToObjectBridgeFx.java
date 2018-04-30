package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;
import pl.shockah.unicorn.ease.Easing;

public class RawToObjectBridgeFx<T> implements ObjectFx<T> {
	public final RawFx fx;

	public RawToObjectBridgeFx(RawFx fx) {
		this.fx = fx;
	}

	@Override
	public float getDuration() {
		return fx.getDuration();
	}

	@Override
	@Nonnull public Easing getMethod() {
		return fx.getMethod();
	}

	@Override
	public void setMethod(@Nonnull Easing method) {
		fx.setMethod(method);
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		fx.update(f, previous);
	}

	@Override
	public void finish(@Nonnull T object, float f, float previous) {
		fx.finish(f, previous);
	}
}