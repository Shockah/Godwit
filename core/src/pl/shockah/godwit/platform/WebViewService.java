package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;

public interface WebViewService extends PlatformService {
	void show(@Nonnull String url);
}