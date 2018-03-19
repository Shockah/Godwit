package pl.shockah.godwit;

import org.robovm.apple.uikit.UIViewController;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;
import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.IosImagePickerService;

@UtilityClass
public final class GodwitIosSetup {
	public static void setup(@Nonnull UIViewController controller) {
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new IosImagePickerService(controller));
	}
}