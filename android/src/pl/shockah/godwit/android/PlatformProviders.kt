package pl.shockah.godwit.android

import android.app.Activity
import com.badlogic.gdx.Gdx
import pl.shockah.godwit.GodwitApplication
import pl.shockah.godwit.platform.SafeAreaProvider

object PlatformProviders {
	fun register(adapter: GodwitApplication) {
		val activity = Gdx.app as Activity
		adapter.registerPlatformProvider<SafeAreaProvider>(AndroidSafeAreaProvider(activity))
	}
}