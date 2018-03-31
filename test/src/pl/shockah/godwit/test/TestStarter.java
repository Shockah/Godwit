package pl.shockah.godwit.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import pl.shockah.godwit.PlatformGodwitAdapter;
import pl.shockah.godwit.State;

public final class TestStarter {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		try {
			Class<? extends State> clazz = (Class<? extends State>)Class.forName(args[0]);

			try {
				Method method = clazz.getDeclaredMethod("configure", Lwjgl3ApplicationConfiguration.class);
				if (Modifier.isStatic(method.getModifiers()))
					method.invoke(null, config);
			} catch (NoSuchMethodException ignored) {
			}

			new Lwjgl3Application(new PlatformGodwitAdapter(() -> {
				try {
					return clazz.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}), config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}