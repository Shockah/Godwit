package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;

public class DesktopWebViewService implements WebViewService {
	@Override
	public void show(@Nonnull String url) {
		Gdx.net.openURI(url);
	}

	@Override
	public void openFacebook(@Nonnull String pageId, @Nonnull String pageUniqueUrl) {
		Gdx.net.openURI(String.format("https://facebook.com/%s", pageUniqueUrl));
	}
}