package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public abstract class ImagePickerService implements PlatformService {
	public abstract void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> delegate);

	public final void getTextureViaImagePicker(@Nonnull Action1<Texture> delegate) {
		getPixmapViaImagePicker(pixmap -> {
			Gdx.app.postRunnable(() -> {
				Texture texture = new Texture(pixmap);
				pixmap.dispose();
				delegate.call(texture);
			});
		});
	}
}