package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.godwit.GodwitFragmentActivity;

public class AndroidBackButtonService extends BackButtonService {
	@Nonnull
	private final WeakReference<GodwitFragmentActivity> activityRef;

	@Getter @Setter
	@Nullable private Delegate delegate;

	public AndroidBackButtonService(@Nonnull GodwitFragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
		Gdx.input.setCatchBackKey(true);
	}

	@Nonnull private GodwitFragmentActivity getActivity() {
		GodwitFragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	protected void runDefaultBackButtonAction() {
		getActivity().runOnUiThread(getActivity()::onBackPressed);
	}
}