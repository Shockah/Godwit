package pl.shockah.godwit.platform;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

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
		FileHandle handle = Gdx.files.local("share-tmp.png");
		PixmapIO.writePNG(handle, pixmap);

		Uri contentUri = FileProvider.getUriForFile(getActivity(), "pl.shockah.godwit.android", handle.file());

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
		shareIntent.setType("image/png");
		shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		getActivity().startActivity(Intent.createChooser(shareIntent, "Share"));
	}
}