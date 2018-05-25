package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;

public interface WebViewService extends PlatformService {
	void show(@Nonnull String url);

	void openFacebook(@Nonnull String pageId, @Nonnull String pageUniqueUrl);

	void openInstagram(@Nonnull String pageUniqueUrl);
}