package pl.shockah.godwit.test

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import groovy.transform.CompileStatic

@CompileStatic
interface Configurable {
	void configure(Lwjgl3ApplicationConfiguration config)
}