package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import javax.annotation.Nonnull;

import pl.shockah.func.Func0;
import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.GodwitFragmentActivity;
import pl.shockah.godwit.platform.ImagePickerService;

public class PlatformGodwitAdapter extends GodwitAdapter {
	public PlatformGodwitAdapter(@Nonnull State initialState) {
		super(initialState);
	}

	public PlatformGodwitAdapter(@Nonnull Func0<State> initialState) {
		super(initialState);
	}

	@Override
	public void create() {
		super.create();

		GodwitFragmentActivity activity = (GodwitFragmentActivity)((AndroidFragmentApplication)Gdx.app).getActivity();
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new AndroidImagePickerService(activity));
	}
}