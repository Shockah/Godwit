package pl.shockah.godwit.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import javax.annotation.Nonnull;

public interface Configurable {
	void configure(@Nonnull Lwjgl3ApplicationConfiguration config);
}