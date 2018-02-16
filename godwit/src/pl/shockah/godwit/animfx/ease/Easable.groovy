package pl.shockah.godwit.animfx.ease

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
interface Easable<T> {
	@Nonnull T ease(@Nonnull T other, float f)
}