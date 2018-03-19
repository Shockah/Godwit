package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;

import org.robovm.apple.uikit.UIViewController;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.IosImagePickerService;

public class PlatformGodwitAdapter extends GodwitAdapter {
	public PlatformGodwitAdapter(@Nonnull State initialState) {
		super(initialState);
	}

	@Override
	public void create() {
		super.create();

		UIViewController controller = ((IOSApplication)Gdx.app).getUIViewController();
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new IosImagePickerService(controller));
	}
}