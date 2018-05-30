package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.func.Action1;

public abstract class ImagePickerService implements PlatformService {
	@Nonnull
	public abstract PermissionState getPermissionState(@Nonnull Source source);

	public abstract boolean isCameraAvailable();

	public abstract void requestPermission(@Nonnull Source source, @Nonnull Action1<PermissionState> newStateDelegate);

	public abstract void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> pixmapDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate);

	public final void getTextureViaImagePicker(@Nonnull Action1<Texture> textureDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate) {
		getPixmapViaImagePicker(pixmap -> {
			Gdx.app.postRunnable(() -> {
				Texture texture = new Texture(pixmap);
				pixmap.dispose();
				textureDelegate.call(texture);
			});
		}, permissionExceptionDelegate);
	}

	public enum Source {
		Camera, Library;
	}

	public enum PermissionState {
		Unknown, Denied, Authorized;
	}

	public class PermissionException extends Exception {
		@Nonnull
		public final Source source;

		public PermissionException(@Nonnull Source source) {
			this.source = source;
		}
	}
}