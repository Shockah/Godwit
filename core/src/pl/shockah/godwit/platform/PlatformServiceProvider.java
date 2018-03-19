package pl.shockah.godwit.platform;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public final class PlatformServiceProvider {
	@Nonnull private final Map<Class<? extends PlatformService>, PlatformService> services = new HashMap<>();

	public <T extends PlatformService> void register(@Nonnull Class<T> clazz, @Nonnull T service) {
		services.put(clazz, service);
	}

	@SuppressWarnings("unchecked")
	@Nonnull public <T extends PlatformService> T get(@Nonnull Class<T> clazz) {
		return (T)services.get(clazz);
	}
}