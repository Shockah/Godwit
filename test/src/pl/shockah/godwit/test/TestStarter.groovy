package pl.shockah.godwit.test

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import groovy.transform.CompileStatic
import pl.shockah.godwit.GodwitAdapter
import pl.shockah.godwit.State

@CompileStatic
final class TestStarter {
	static void main(String[] args) {
		Class<? extends State> clazz = (Class<? extends State>)Class.forName(args[0])
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		State state = clazz.newInstance()
		if (state instanceof Configurable)
			(state as Configurable).configure(config)
		new Lwjgl3Application(new GodwitAdapter(state), config);
	}
}