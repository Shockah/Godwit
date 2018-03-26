package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BackButtonService implements PlatformService {
	@Nullable public Delegate getDelegate() {
		return null;
	}

	public void setDelegate(@Nonnull Delegate delegate) {
	}

	public void onBackButton() {
		Delegate delegate = getDelegate();
		if (delegate != null)
			delegate.onBackButton();
		else
			runDefaultBackButtonAction();
	}

	protected void runDefaultBackButtonAction() {
	}

	public interface Delegate {
		void onBackButton();
	}
}