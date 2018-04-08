package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;

public class DesktopWebViewService implements WebViewService {
	@Override
	public void show(@Nonnull String url) {
		Gdx.net.openURI(url);
	}
}