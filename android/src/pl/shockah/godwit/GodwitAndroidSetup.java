package pl.shockah.godwit;

import android.support.v4.app.FragmentManager;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;
import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.ImagePickerService;

@UtilityClass
public final class GodwitAndroidSetup {
	public static void setup(@Nonnull FragmentManager fragmentManager) {
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new AndroidImagePickerService(fragmentManager));
	}
}