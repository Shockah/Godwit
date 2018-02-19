package pl.shockah.godwit.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import pl.shockah.godwit.GodwitAdapter;
import pl.shockah.godwit.State;

public final class TestStarter {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			Class<? extends State> clazz = (Class<? extends State>)Class.forName(args[0]);
			Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
			State state = clazz.newInstance();
			if (state instanceof Configurable)
				((Configurable)state).configure(config);
			new Lwjgl3Application(new GodwitAdapter(state), config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}