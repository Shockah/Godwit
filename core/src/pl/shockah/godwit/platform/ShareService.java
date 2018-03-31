package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Pixmap;

import javax.annotation.Nonnull;

public abstract class ShareService implements PlatformService {
	public abstract void share(@Nonnull Pixmap pixmap);
}