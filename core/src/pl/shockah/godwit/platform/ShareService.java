package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Pixmap;

import javax.annotation.Nonnull;

public interface ShareService extends PlatformService {
	void share(@Nonnull Pixmap pixmap);
}