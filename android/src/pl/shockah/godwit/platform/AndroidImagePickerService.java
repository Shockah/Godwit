package pl.shockah.godwit.platform;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.badlogic.gdx.graphics.Pixmap;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class AndroidImagePickerService extends ImagePickerService {
	@Nonnull private final WeakReference<GodwitFragmentActivity> activityRef;

	public AndroidImagePickerService(@Nonnull GodwitFragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
	}

	@Nonnull private GodwitFragmentActivity getActivity() {
		GodwitFragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Nonnull private PermissionState mapNativeState(int state) {
		switch (state) {
			case PackageManager.PERMISSION_GRANTED:
				return PermissionState.Authorized;
			case PackageManager.PERMISSION_DENIED:
				return PermissionState.Unknown;
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	@Nonnull public PermissionState getPermissionState(@Nonnull Source source) {
		switch (source) {
			case Library:
				return mapNativeState(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE));
			case Camera:
				return mapNativeState(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA));
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isCameraAvailable() {
		return false;
	}

	@Override
	public void requestPermission(@Nonnull Source source, @Nonnull Action1<PermissionState> newStateDelegate) {
		PermissionState permissionState = getPermissionState(source);
		if (permissionState == PermissionState.Authorized) {
			newStateDelegate.call(permissionState);
			return;
		}

		switch (source) {
			case Library:
				getActivity().requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, granted -> {
					if (granted)
						newStateDelegate.call(PermissionState.Authorized);
					else
						newStateDelegate.call(PermissionState.Denied);
				});
				break;
			case Camera:
				getActivity().requestPermissions(new String[] { Manifest.permission.CAMERA }, granted -> {
					if (granted)
						newStateDelegate.call(PermissionState.Authorized);
					else
						newStateDelegate.call(PermissionState.Denied);
				});
				break;
		}
	}

	@Override
	public void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> pixmapDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate) {
		getActivity().runOnUiThread(() -> {
			PickImageDialog.build(new PickSetup().setSystemDialog(true))
					.setOnPickResult(result -> {
						Bitmap bitmap = result.getBitmap();
						Pixmap pixmap = new Pixmap(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
						pixmap.setBlending(Pixmap.Blending.None);
						for (int y = 0; y < pixmap.getHeight(); y++) {
							for (int x = 0; x < pixmap.getWidth(); x++) {
								int argb = bitmap.getPixel(x, y);
								pixmap.drawPixel(x, y, (argb << 8) | (argb >> 24));
							}
						}
						pixmapDelegate.call(pixmap);
						bitmap.recycle();
					}).show(getActivity());
		});
	}
}