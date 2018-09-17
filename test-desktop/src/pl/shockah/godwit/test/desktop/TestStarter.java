package pl.shockah.godwit.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.GodwitLogger;
import pl.shockah.godwit.PlatformGodwitAdapter;
import pl.shockah.godwit.entity.EntityTreeManager;
import pl.shockah.godwit.entity.State;

public final class TestStarter {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		new Lwjgl3Application(new PlatformGodwitAdapter(new EntityTreeManager(() -> {
			try {
				return ((Class<? extends State>)Class.forName(args[0])).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		})) {
			@Override
			public void create() {
				super.create();
				GodwitLogger.Level.Debug.setGdxLevel();

				Godwit godwit = Godwit.getInstance();
				godwit.waitForDeltaToStabilize = false;
			}
		}, config);
	}
}