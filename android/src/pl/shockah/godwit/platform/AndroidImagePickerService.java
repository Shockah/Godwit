package pl.shockah.godwit.platform;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class AndroidImagePickerService implements ImagePickerService {
	@Nonnull private final WeakReference<FragmentManager> managerRef;

	public AndroidImagePickerService(@Nonnull FragmentManager manager) {
		this.managerRef = new WeakReference<>(manager);
	}

	@Nonnull private FragmentManager getManager() {
		FragmentManager manager = managerRef.get();
		if (manager == null)
			throw new IllegalStateException("Lost manager.");
		return manager;
	}

	@Override
	public void openImagePicker(@Nonnull Action1<Texture> delegate) {
		PickImageDialog.build(new PickSetup())
				.setOnPickResult(result -> {
					Bitmap bitmap = result.getBitmap();
					Gdx.app.postRunnable(() -> {
						int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
						bitmap.getPixels(pixels, 0, 4, 0, 0, bitmap.getWidth(), bitmap.getHeight());
						for (int i = 0; i< pixels.length; i++) {
							int pixel = pixels[i];
							pixels[i] = (pixel << 8) | ((pixel >> 24) & 0xFF);
						}
						Pixmap pixmap = new Pixmap(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
						pixmap.getPixels().asIntBuffer().put(pixels);
						Texture texture = new Texture(pixmap);
						pixmap.dispose();
						delegate.call(texture);
					});
				}).show(getManager());
	}
}