package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class GfxMesh implements Renderable {
	@Delegate
	@Nonnull public final Mesh mesh;

	@Nonnull private final SpriteBatch batch;

	public GfxMesh(@Nonnull Mesh mesh, @Nonnull SpriteBatch batch) {
		this.mesh = mesh;
		this.batch = batch;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!v.equals(Vec2.zero))
			throw new IllegalArgumentException("Translating meshes isn't supported.");
		mesh.render(batch.getShader(), GL20.GL_TRIANGLES, 0, mesh.getNumVertices());
	}

	public static class RenderGroup extends pl.shockah.godwit.RenderGroup {
		@Nullable private GfxMesh mesh;

		public void clear() {
			mesh = null;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			if (mesh == null) {
				gfx.prepareSprites();
				gfx.getSpriteBatch().flush();
				super.render(gfx, v);
				mesh = new GfxMesh(gfx.getSpriteBatch().getMeshCopy(), gfx.getSpriteBatch());
			} else {
				mesh.render(gfx, v);
			}
		}
	}
}