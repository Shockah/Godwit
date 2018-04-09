package pl.shockah.godwit;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class GodwitFragmentActivity extends FragmentActivity {
	@Nonnull private final List<Object> cachedResultCodes = new ArrayList<>();
	@Nonnull private final SparseArray<PermissionResponseDelegate> awaiting = new SparseArray<>();

	public int getResultCode(@Nonnull Object obj) {
		if (!cachedResultCodes.contains(obj))
			cachedResultCodes.add(obj);
		return cachedResultCodes.indexOf(obj) + 1;
	}

	public void requestPermissions(@Nonnull String[] permissions, @Nonnull PermissionResponseDelegate responseDelegate) {
		awaiting.put(getResultCode(permissions), responseDelegate);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		PermissionResponseDelegate delegate = awaiting.get(requestCode);
		if (delegate == null)
			return;
		awaiting.remove(requestCode);

		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				delegate.response(false);
				return;
			}
		}
		delegate.response(true);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Gdx.gl20 == null)
			return;

		Godwit godwit = Godwit.getInstance();
		godwit.setAssetManager(godwit.getAssetManagerFactory().call());
	}

	public interface PermissionResponseDelegate {
		void response(boolean granted);
	}
}