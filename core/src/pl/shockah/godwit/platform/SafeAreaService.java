package pl.shockah.godwit.platform;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ui.Padding;

public class SafeAreaService implements PlatformService {
	@Nonnull
	public Padding getSafeAreaPadding() {
		return new Padding();
	}
}