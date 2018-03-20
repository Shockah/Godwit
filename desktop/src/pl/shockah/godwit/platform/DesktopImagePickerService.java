package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import net.spookygames.gdx.nativefilechooser.NativeFileChooserCallback;
import net.spookygames.gdx.nativefilechooser.NativeFileChooserConfiguration;
import net.spookygames.gdx.nativefilechooser.desktop.DesktopFileChooser;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class DesktopImagePickerService extends ImagePickerService {
	@Override
	public void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> delegate) {
		NativeFileChooserConfiguration configuration = new NativeFileChooserConfiguration();
		configuration.directory = Gdx.files.absolute(System.getProperty("user.home"));
		configuration.mimeFilter = "image/*";
		configuration.nameFilter = (dir, name) -> name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".bmp");
		configuration.title = "Choose an image";

		new DesktopFileChooser().chooseFile(configuration, new NativeFileChooserCallback() {
			@Override
			public void onFileChosen(FileHandle file) {
				byte[] bytes = file.readBytes();
				delegate.call(new Pixmap(bytes, 0, bytes.length));
			}

			@Override
			public void onCancellation() {
			}

			@Override
			public void onError(Exception exception) {
				throw new RuntimeException(exception);
			}
		});
	}
}