package pl.shockah.godwit.test

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
interface Configurable {
	void configure(@Nonnull Lwjgl3ApplicationConfiguration config)
}