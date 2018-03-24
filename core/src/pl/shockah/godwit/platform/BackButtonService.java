package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BackButtonService implements PlatformService {
	public boolean isAvailable() {
		return false;
	}

	@Nullable public Delegate getDelegate() {
		return null;
	}

	public void setDelegate(@Nonnull Delegate delegate) {
	}

	public void onBackButton() {
		if (isAvailable()) {
			Delegate delegate = getDelegate();
			if (delegate != null)
				delegate.onBackButton();
		}
	}

	public interface Delegate {
		void onBackButton();
	}
}