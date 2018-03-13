package pl.shockah.godwit.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.RenderGroupEntity;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class MaskEntity extends RenderGroupEntity {
	@Nullable public Entity mask;

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		setupMasking(gfx);
		renderMask(gfx, v + position);
		gfx.end();
		useMasking(gfx);
		super.render(gfx, v);
		gfx.end();
		disableMasking(gfx);
	}

	protected final void setupMasking(@Nonnull Gfx gfx) {
		gfx.prepareContext();
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		Gdx.gl.glColorMask(false, false, false, false);
	}

	protected final void useMasking(@Nonnull Gfx gfx) {
		gfx.prepareContext();
		Gdx.gl.glColorMask(true, true, true, true);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
	}

	protected final void disableMasking(@Nonnull Gfx gfx) {
		gfx.prepareContext();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}

	public final void renderMask(@Nonnull Gfx gfx) {
		renderMask(gfx, Vec2.zero);
	}

	public final void renderMask(@Nonnull Gfx gfx, float x, float y) {
		renderMask(gfx, new Vec2(x, y));
	}

	public void renderMask(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (mask != null)
			mask.render(gfx, v);
	}
}