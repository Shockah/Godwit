package pl.shockah.godwit.platform;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;

import com.badlogic.gdx.graphics.Pixmap;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class AndroidImagePickerService extends ImagePickerService {
	@Nonnull private final WeakReference<FragmentActivity> activityRef;

	public AndroidImagePickerService(@Nonnull FragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
	}

	@Nonnull private FragmentActivity getActivity() {
		FragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	public void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> delegate) {
		getActivity().runOnUiThread(() -> {
			PickImageDialog.build(new PickSetup().setSystemDialog(true))
					.setOnPickResult(result -> {
						Bitmap bitmap = result.getBitmap();
						int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
						bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
						for (int i = 0; i < pixels.length; i++) {
							int pixel = pixels[i];
							pixels[i] = (pixel << 8) | ((pixel >> 24) & 0xFF);
						}
						Pixmap pixmap = new Pixmap(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
						pixmap.getPixels().asIntBuffer().put(pixels);
						delegate.call(pixmap);
					}).show(getActivity());
		});
	}
}