package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.animfx.object.ObjectFx
import pl.shockah.godwit.animfx.raw.RawFx

import javax.annotation.Nonnull

@CompileStatic
class RawToObjectBridgeFx<T> implements ObjectFx<T> {
	final RawFx fx

	RawToObjectBridgeFx(RawFx fx) {
		this.fx = fx
	}

	@Override
	float getDuration() {
		return fx.duration
	}

	@Override
	Easing getMethod() {
		return fx.method
	}

	@Override
	void setMethod(@Nonnull Easing method) {
		fx.method = method
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		fx.update(f, previous)
	}

	@Override
	void finish(@Nonnull T object, float f, float previous) {
		fx.finish(f, previous)
	}
}