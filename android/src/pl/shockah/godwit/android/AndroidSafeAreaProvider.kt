package pl.shockah.godwit.android

import android.app.Activity
import android.support.v4.view.ViewCompat
import pl.shockah.godwit.geom.EdgeInsets
import pl.shockah.godwit.platform.SafeAreaProvider

class AndroidSafeAreaProvider(
		activity: Activity
) : AndroidProvider(activity), SafeAreaProvider {
	override var safeAreaInsets = EdgeInsets()
		private set

	init {
		ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView.rootView) { _, insets ->
			insets.displayCutout?.let { cutout ->
				safeAreaInsets = EdgeInsets(
						top = cutout.safeInsetTop.toFloat(),
						right = cutout.safeInsetRight.toFloat(),
						bottom = cutout.safeInsetBottom.toFloat(),
						left = cutout.safeInsetLeft.toFloat()
				)
			}
			return@setOnApplyWindowInsetsListener insets
		}
	}
}