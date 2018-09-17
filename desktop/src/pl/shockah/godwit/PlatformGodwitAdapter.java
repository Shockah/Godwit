package pl.shockah.godwit;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.DesktopImagePickerService;
import pl.shockah.godwit.platform.DesktopWebViewService;
import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.SafeAreaService;
import pl.shockah.godwit.platform.WebViewService;

public class PlatformGodwitAdapter extends GodwitAdapter {
	public PlatformGodwitAdapter(@Nonnull EntityManager manager) {
		super(manager);
	}

	@Override
	public void create() {
		super.create();

		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new DesktopImagePickerService());
		Godwit.getInstance().platformServiceProvider.register(WebViewService.class, new DesktopWebViewService());
		Godwit.getInstance().platformServiceProvider.register(SafeAreaService.class, new SafeAreaService());
	}
}