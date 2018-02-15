package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

@CompileStatic
interface Easable<T> {
	T ease(T other, float f)
}