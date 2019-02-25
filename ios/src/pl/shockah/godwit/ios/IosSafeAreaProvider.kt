package pl.shockah.godwit.ios

import org.robovm.apple.foundation.Foundation
import org.robovm.apple.uikit.UIApplication
import pl.shockah.godwit.geom.EdgeInsets
import pl.shockah.godwit.platform.SafeAreaProvider

class IosSafeAreaProvider : SafeAreaProvider {
	override val safeAreaInsets: EdgeInsets
		get() {
			if (Foundation.getMajorSystemVersion() < 11)
				return EdgeInsets()

			val view = UIApplication.getSharedApplication().keyWindow.rootViewController.view
			val edgeInsets = view.safeAreaInsets
			return EdgeInsets(
					top = (edgeInsets.top * view.contentScaleFactor).toFloat(),
					bottom = (edgeInsets.bottom * view.contentScaleFactor).toFloat(),
					left = (edgeInsets.left * view.contentScaleFactor).toFloat(),
					right = (edgeInsets.right * view.contentScaleFactor).toFloat()
			)
		}
}