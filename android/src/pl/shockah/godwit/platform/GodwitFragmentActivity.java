package pl.shockah.godwit.platform;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class GodwitFragmentActivity extends FragmentActivity {
	@Nonnull private final List<Object> cachedResultCodes = new ArrayList<>();
	@Nonnull private final Map<Integer, PermissionResponseDelegate> awaiting = new HashMap<>();

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

	public interface PermissionResponseDelegate {
		void response(boolean granted);
	}
}