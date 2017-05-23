package pl.shockah.godwit.test

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import groovy.transform.CompileStatic

@CompileStatic
interface Configurable {
	void configure(LwjglApplicationConfiguration config)
}