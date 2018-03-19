package pl.shockah.godwit.platform;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.v4.app.FragmentActivity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class AndroidImagePickerService implements ImagePickerService {
	@Nonnull private final WeakReference<FragmentActivity> activityRef;

	public AndroidImagePickerService(@Nonnull FragmentActivity activityRef) {
		this.activityRef = new WeakReference<>(activityRef);
	}

	@Nonnull private FragmentActivity getActivity() {
		FragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	public void openImagePicker(@Nonnull Action1<Texture> delegate) {
		PickImageDialog.build(new PickSetup())
				.setOnPickResult(result -> {
					Bitmap bitmap = result.getBitmap();
					Gdx.app.postRunnable(() -> {
						Texture texture = new Texture(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
						GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getTextureObjectHandle());
						GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
						GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
						bitmap.recycle();
						delegate.call(texture);
					});
				}).show(getActivity());
	}
}