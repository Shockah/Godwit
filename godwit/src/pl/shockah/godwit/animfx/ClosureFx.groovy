package pl.shockah.godwit.animfx

import javax.annotation.Nonnull

interface ClosureFx extends Fx {
	@Nonnull Closure getClosure()
}