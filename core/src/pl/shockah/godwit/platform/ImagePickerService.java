package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public interface ImagePickerService extends PlatformService {
	void openImagePicker(@Nonnull Action1<Texture> delegate);
}