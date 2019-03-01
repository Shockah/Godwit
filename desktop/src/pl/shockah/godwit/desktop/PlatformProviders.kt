package pl.shockah.godwit.desktop

import pl.shockah.godwit.GodwitApplication
import pl.shockah.godwit.platform.SafeAreaProvider

object PlatformProviders {
	fun register(adapter: GodwitApplication) {
		adapter.registerPlatformProvider<SafeAreaProvider>(object : SafeAreaProvider { })
	}
}