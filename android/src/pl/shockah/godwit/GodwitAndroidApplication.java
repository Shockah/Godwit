package pl.shockah.godwit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.ImagePickerService;

public class GodwitAndroidApplication extends AndroidFragmentApplication {
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new AndroidImagePickerService(getActivity()));
	}
}