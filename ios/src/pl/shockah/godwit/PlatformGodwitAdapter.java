package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;

import org.robovm.apple.uikit.UIViewController;

import javax.annotation.Nonnull;

import pl.shockah.func.Func0;
import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.IosImagePickerService;
import pl.shockah.godwit.platform.IosShareService;
import pl.shockah.godwit.platform.IosWebViewService;
import pl.shockah.godwit.platform.ShareService;
import pl.shockah.godwit.platform.WebViewService;

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

		UIViewController controller = ((IOSApplication)Gdx.app).getUIViewController();
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new IosImagePickerService(controller));
		Godwit.getInstance().platformServiceProvider.register(ShareService.class, new IosShareService(controller));
		Godwit.getInstance().platformServiceProvider.register(WebViewService.class, new IosWebViewService(controller));
	}
}