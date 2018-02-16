package pl.shockah.godwit

import com.badlogic.gdx.Gdx
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
	protected boolean isFirstTick = true
	boolean waitForDeltaToStabilize = true
	boolean renderFirstTickWhenWaitingForDeltaToStabilize = false
	@Nonnull protected final List<Float> deltas = []

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
		if (waitForDeltaToStabilize) {
			deltas.add(Gdx.graphics.deltaTime)
			if (isFirstTick) {
				isFirstTick = false
				if (renderFirstTickWhenWaitingForDeltaToStabilize) {
					runStateCreation()
					runRender()
				}
				return
			} else {
				runRender()
				if (deltas.size() >= 5) {
					float min = deltas.min() as float
					float max = deltas.max() as float
					float average = (deltas.sum() as float) / deltas.size() as float
					deltas.remove(0)
					if (min == 0f)
						return

					float difference = max - min as float
					float min2 = Math.min(difference, average)
					float max2 = Math.max(difference, average)
					if (min2 / max2 > 0.2f)
						return
					waitForDeltaToStabilize = false
				} else {
					return
				}
			}
		}

		runStateCreation()
		runUpdate()
		runRender()
	}

	private void runStateCreation() {
		if (newState) {
			state?.destroy()
			state = newState
			newState = null
			gfx.resetCamera()
			state.create()
		}
	}

	private void runUpdate() {
		state?.update()
	}

	private void runRender() {
		GfxContextManager.bindSurface(null)
		gfx.updateCamera()
		gfx.clear(Color.BLACK)
		gfx.blendMode = BlendMode.Normal

		state?.render(gfx)

		gfx.endTick()
		GfxContextManager.bindSurface(null)
	}
}