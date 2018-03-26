package pl.shockah.godwit.platform;

import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.godwit.GodwitFragmentActivity;

public class AndroidShareService extends ShareService {
	@Nonnull private final WeakReference<GodwitFragmentActivity> activityRef;

	public AndroidShareService(@Nonnull GodwitFragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
	}

	@Nonnull private GodwitFragmentActivity getActivity() {
		GodwitFragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	public void share(@Nonnull Pixmap pixmap) {
		File cachePath = new File(getActivity().getCacheDir(), "shared-images");
		cachePath.mkdirs();
		FileHandle handle = new FileHandle(new File(cachePath, "shared-tmp.png"));
		PixmapIO.writePNG(handle, pixmap);

		Uri contentUri = FileProvider.getUriForFile(getActivity(), String.format("%s.%s", getActivity().getPackageName(), "pl.shockah.godwit.android.shareprovider"), handle.file());

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
		shareIntent.setDataAndType(contentUri, "image/png");
		shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		getActivity().startActivity(Intent.createChooser(shareIntent, "Choose an app"));
	}

	public static class FileProvider extends android.support.v4.content.FileProvider {
	}
}