package pl.shockah.godwit.test.desktop

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

class TestStarter {
	companion object {
		@Suppress("UNCHECKED_CAST")
		@JvmStatic
		fun main(args: Array<String>) {
			val config = Lwjgl3ApplicationConfiguration()
			val listener = (Class.forName(args[0]) as Class<out ApplicationListener>).newInstance()
			Lwjgl3Application(listener, config)
		}
	}
}