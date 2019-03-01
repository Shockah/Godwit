package pl.shockah.godwit.ios

import pl.shockah.godwit.GodwitApplication
import pl.shockah.godwit.platform.SafeAreaProvider

object PlatformProviders {
	fun register(adapter: GodwitApplication) {
		adapter.registerPlatformProvider<SafeAreaProvider>(IosSafeAreaProvider())
	}
}