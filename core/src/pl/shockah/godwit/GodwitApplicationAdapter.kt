package pl.shockah.godwit

import com.badlogic.gdx.ApplicationAdapter
import pl.shockah.godwit.platform.PlatformProvider
import kotlin.reflect.KClass

open class GodwitApplicationAdapter : ApplicationAdapter() {
	@PublishedApi
	internal val platformProviders = mutableMapOf<KClass<out PlatformProvider>, PlatformProvider>()

	inline fun <reified T : PlatformProvider> getPlatformProvider(): T {
		return platformProviders[T::class]!! as T
	}

	inline fun <reified T : PlatformProvider> registerPlatformProvider(provider: T) {
		platformProviders[T::class] = provider
	}
}