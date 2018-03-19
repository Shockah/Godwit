package pl.shockah.godwit;

import android.support.v4.app.FragmentActivity;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;
import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.ImagePickerService;

@UtilityClass
public final class GodwitAndroidSetup {
	public static void setup(@Nonnull FragmentActivity activity) {
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new AndroidImagePickerService(activity));
	}
}