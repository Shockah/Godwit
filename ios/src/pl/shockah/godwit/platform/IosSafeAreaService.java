package pl.shockah.godwit.platform;

import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIEdgeInsets;
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
			return new Padding((float)insets.getLeft(), (float)insets.getRight(), (float)insets.getTop(), (float)insets.getBottom());
		} else {
			return super.getSafeAreaPadding();
		}
	}
}