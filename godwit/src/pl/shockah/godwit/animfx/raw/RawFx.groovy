package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.Fx
import pl.shockah.godwit.animfx.RawToObjectBridgeFx
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.animfx.object.ObjectFx

import javax.annotation.Nonnull

@CompileStatic
trait RawFx extends Fx {
	abstract void update(float f, float previous)

	void finish(float f, float previous) {
		update(f, previous)
	}

	RawFx repeat(int count) {
		return new SequenceRawFx((1..count).collect { this } as List<RawFx>)
	}

	public <T> ObjectFx<T> asObject(Class<T> clazz) {
		return new RawToObjectBridgeFx<T>(this)
	}

	RawFx withMethod(@Nonnull Easing method) {
		this.method = method
		return this
	}

	RawFx additive() {
		return new AdditiveRawFx(this)
	}
}