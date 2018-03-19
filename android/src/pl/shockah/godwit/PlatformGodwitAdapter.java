package pl.shockah.godwit;

import android.support.v4.app.FragmentActivity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.ImagePickerService;

public class PlatformGodwitAdapter extends GodwitAdapter {
	public PlatformGodwitAdapter(@Nonnull State initialState) {
		super(initialState);
	}

	@Override
	public void create() {
		super.create();

		FragmentActivity activity = ((AndroidFragmentApplication)Gdx.app).getActivity();
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new AndroidImagePickerService(activity));
	}
}