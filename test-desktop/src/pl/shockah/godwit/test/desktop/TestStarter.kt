package pl.shockah.godwit.test.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import pl.shockah.godwit.GodwitApplicationAdapter
import pl.shockah.godwit.desktop.PlatformProviders

class TestStarter {
	companion object {
		@Suppress("UNCHECKED_CAST")
		@JvmStatic
		fun main(args: Array<String>) {
			val config = Lwjgl3ApplicationConfiguration()
			val adapter = (Class.forName(args[0]) as Class<out GodwitApplicationAdapter>).newInstance()
			PlatformProviders.register(adapter)
			Lwjgl3Application(adapter, config)
		}
	}
}