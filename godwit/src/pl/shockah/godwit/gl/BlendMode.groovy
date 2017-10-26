package pl.shockah.godwit.gl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class BlendMode {
	@Nonnull static final BlendMode Normal = new BlendMode() {
		@Override
		protected void begin() {
			Gdx.gl.glEnable(GL20.GL_BLEND)
			Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
		}

		@Override
		protected void end() {
			Gdx.gl.glDisable(GL20.GL_BLEND)
		}
	}

	protected abstract void begin()

	protected abstract void end()
}