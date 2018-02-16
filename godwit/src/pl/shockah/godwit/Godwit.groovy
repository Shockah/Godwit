package pl.shockah.godwit

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.BlendMode
import pl.shockah.godwit.gl.GfxContextManager
import pl.shockah.godwit.gl.GfxImpl

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
final class Godwit {
	@Nullable private static Godwit instance

	@Nullable protected State state
	@Nullable protected State newState
	@Nonnull final GfxImpl gfx = new GfxImpl()
	@Nonnull final AssetManager assetManager = new AssetManager()

	@Nonnull static Godwit getInstance() {
		if (!instance)
			instance = new Godwit()
		return instance
	}

	@Nullable State getState() {
		return state
	}

	void setState(@Nullable State state) {
		newState = state
	}

	void tick() {
		if (newState) {
			state?.destroy()
			state = newState
			newState = null
			gfx.resetCamera()
			state.create()
		}

		state?.update()

		GfxContextManager.bindSurface(null)
		gfx.updateCamera()
		gfx.clear(Color.BLACK)
		gfx.blendMode = BlendMode.Normal

		state?.render(gfx)

		gfx.endTick()
		GfxContextManager.bindSurface(null)
	}
}