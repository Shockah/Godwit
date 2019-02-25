package pl.shockah.godwit.desktop

import pl.shockah.godwit.GodwitApplicationAdapter
import pl.shockah.godwit.platform.SafeAreaProvider

object PlatformProviders {
	fun register(adapter: GodwitApplicationAdapter) {
		adapter.registerPlatformProvider<SafeAreaProvider>(object : SafeAreaProvider { })
	}
}