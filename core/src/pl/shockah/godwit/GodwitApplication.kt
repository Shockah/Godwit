package pl.shockah.godwit

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.platform.PlatformProvider
import kotlin.reflect.KClass

open class GodwitApplication : ApplicationAdapter() {
	companion object {
		val self: GodwitApplication
			get() = Gdx.app.applicationListener as GodwitApplication
	}

	private val mutablePpi = MutableVec2()
	val ppi: Vec2 = mutablePpi

	@PublishedApi
	internal val platformProviders = mutableMapOf<KClass<out PlatformProvider>, PlatformProvider>()

	inline fun <reified T : PlatformProvider> getPlatformProvider(): T {
		return platformProviders[T::class]!! as T
	}

	inline fun <reified T : PlatformProvider> registerPlatformProvider(provider: T) {
		platformProviders[T::class] = provider
	}

	override fun render() {
		mutablePpi.x = Gdx.graphics.ppiX
		mutablePpi.y = Gdx.graphics.ppiY
	}
}