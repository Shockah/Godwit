package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BackButtonService implements PlatformService {
	public abstract void pushDelegate(@Nonnull Delegate delegate);

	public abstract void popDelegate();

	public abstract void removeDelegate(@Nonnull Delegate delegate);

	@Nullable
	protected abstract Delegate getTopDelegate();

	public final void onBackButton() {
		Delegate delegate = getTopDelegate();
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