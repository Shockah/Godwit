package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.AndroidBackButtonService;
import pl.shockah.godwit.platform.AndroidImagePickerService;
import pl.shockah.godwit.platform.AndroidShareService;
import pl.shockah.godwit.platform.AndroidWebViewService;
import pl.shockah.godwit.platform.BackButtonService;
import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.SafeAreaService;
import pl.shockah.godwit.platform.ShareService;
import pl.shockah.godwit.platform.WebViewService;
import pl.shockah.unicorn.func.Func0;

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
		Godwit.getInstance().platformServiceProvider.register(BackButtonService.class, new AndroidBackButtonService(activity));
		Godwit.getInstance().platformServiceProvider.register(ShareService.class, new AndroidShareService(activity));
		Godwit.getInstance().platformServiceProvider.register(WebViewService.class, new AndroidWebViewService(activity));
		Godwit.getInstance().platformServiceProvider.register(SafeAreaService.class, new SafeAreaService());
	}
}