package pl.shockah.godwit.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.PlatformGodwitAdapter;
import pl.shockah.godwit.State;

public final class TestStarter {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		new Lwjgl3Application(new PlatformGodwitAdapter(() -> {
			try {
				return ((Class<? extends State>)Class.forName(args[0])).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}) {
			@Override
			public void create() {
				super.create();

				Godwit godwit = Godwit.getInstance();
				godwit.waitForDeltaToStabilize = false;
				godwit.renderFirstTickWhenWaitingForDeltaToStabilize = true;
			}
		}, config);
	}
}