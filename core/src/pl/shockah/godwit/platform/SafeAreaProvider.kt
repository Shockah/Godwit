package pl.shockah.godwit.platform

import pl.shockah.godwit.geom.EdgeInsets

interface SafeAreaProvider : PlatformProvider {
	val safeAreaInsets: EdgeInsets
		get() = EdgeInsets()
}