package pl.shockah.godwit.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import javax.annotation.Nonnull;

public abstract class BlendMode {
	@Nonnull public static final BlendMode normal = new BlendMode() {
		@Override
		void begin() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		@Override
		void end() {
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	};

	abstract void begin();
	abstract void end();
}