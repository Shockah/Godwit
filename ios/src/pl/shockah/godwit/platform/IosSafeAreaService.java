package pl.shockah.godwit.platform;

import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIEdgeInsets;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIWindow;

import javax.annotation.Nonnull;

import java8.util.stream.RefStreams;
import pl.shockah.godwit.ui.Padding;

public class IosSafeAreaService extends SafeAreaService {
	@Override
	@Nonnull public Padding getSafeAreaPadding() {
		int[] version = RefStreams.of(UIDevice.getCurrentDevice().getSystemVersion().split("\\."))
				.mapToInt(Integer::parseInt)
				.toArray();

		if (version.length >= 1 && version[0] >= 11) {
			UIWindow window = UIApplication.getSharedApplication().getKeyWindow();
			UIEdgeInsets insets = window.getSafeAreaInsets();

			float scale = (float)UIScreen.getMainScreen().getScale();
			return new Padding(
					(float)insets.getLeft() * scale,
					(float)insets.getRight() * scale,
					(float)insets.getTop() * scale,
					(float)insets.getBottom() * scale
			);
		} else {
			return super.getSafeAreaPadding();
		}
	}
}