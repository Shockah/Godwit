package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.GodwitFragmentActivity;

public class AndroidBackButtonService extends BackButtonService {
	@Nonnull
	private final WeakReference<GodwitFragmentActivity> activityRef;

	@Nonnull
	private final LinkedList<Delegate> delegates = new LinkedList<>();

	public AndroidBackButtonService(@Nonnull GodwitFragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
		Gdx.input.setCatchBackKey(true);
	}

	@Nonnull
	private GodwitFragmentActivity getActivity() {
		GodwitFragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	public void pushDelegate(@Nonnull Delegate delegate) {
		delegates.push(delegate);
	}

	@Override
	public void popDelegate() {
		delegates.pop();
	}

	@Override
	public void removeDelegate(@Nonnull Delegate delegate) {
		delegates.remove(delegate);
	}

	@Nullable
	@Override
	protected Delegate getTopDelegate() {
		return delegates.isEmpty() ? null : delegates.peek();
	}

	@Override
	protected void runDefaultBackButtonAction() {
		getActivity().runOnUiThread(getActivity()::onBackPressed);
	}
}