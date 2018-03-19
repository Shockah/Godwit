package pl.shockah.godwit;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.DesktopImagePickerService;
import pl.shockah.godwit.platform.ImagePickerService;

public class PlatformGodwitAdapter extends GodwitAdapter {
	public PlatformGodwitAdapter(@Nonnull State initialState) {
		super(initialState);
	}

	@Override
	public void create() {
		super.create();

		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new DesktopImagePickerService());
	}
}