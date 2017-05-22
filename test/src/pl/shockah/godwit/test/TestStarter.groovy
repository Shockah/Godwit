package pl.shockah.godwit.test

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State

@CompileStatic
final class TestStarter extends ApplicationAdapter {
	private final State state

	static void main(String[] args) {
		Class<? extends State> clazz = Class.forName("${TestStarter.package.name}.${args[0]}State")
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TestStarter(clazz.newInstance()), config);
	}

	TestStarter(State state) {
		this.state = state
	}

	@Override
	void create() {
		Godwit.instance.state = state
	}

	@Override
	void render() {
		Godwit.instance.tick()
	}

	@Override
	void resize(int width, int height) {
		Godwit.instance.gfx.updateCamera()
	}
}