package pl.shockah.godwit;

import javax.annotation.Nonnull;

import pl.shockah.godwit.platform.ImagePickerService;
import pl.shockah.godwit.platform.IosImagePickerService;

public class PlatformSpecificGodwitAdapter extends GodwitAdapter {
	public PlatformSpecificGodwitAdapter(@Nonnull State initialState) {
		super(initialState);
	}

	@Override
	public void create() {
		super.create();
		Godwit.getInstance().platformServiceProvider.register(ImagePickerService.class, new IosImagePickerService());
	}
}