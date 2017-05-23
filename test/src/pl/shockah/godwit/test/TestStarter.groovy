package pl.shockah.godwit.test

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import groovy.transform.CompileStatic
import pl.shockah.godwit.GodwitAdapter
import pl.shockah.godwit.State

@CompileStatic
final class TestStarter {
	static void main(String[] args) {
		Class<? extends State> clazz = Class.forName("${TestStarter.package.name}.${args[0]}State")
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		State state = clazz.newInstance()
		if (state instanceof Configurable)
			(state as Configurable).configure(config)
		new LwjglApplication(new GodwitAdapter(state), config);
	}
}