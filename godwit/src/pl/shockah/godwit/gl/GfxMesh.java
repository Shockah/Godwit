package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class GfxMesh implements Renderable {
	@Delegate
	@Nonnull public final Mesh mesh;

	public final Texture texture;

	@Nonnull private final SpriteBatch batch;

	public GfxMesh(@Nonnull Mesh mesh, @Nonnull Texture texture, @Nonnull SpriteBatch batch) {
		this.mesh = mesh;
		this.texture = texture;
		this.batch = batch;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!v.equals(Vec2.zero))
			throw new IllegalArgumentException("Translating meshes isn't supported.");
		texture.bind();
		mesh.render(batch.getShader(), GL20.GL_TRIANGLES, 0, mesh.getNumVertices());
	}

	public static class RenderGroup extends pl.shockah.godwit.RenderGroup {
		@Nullable private GfxMesh mesh;

		@Override
		public void onRemovedFromHierarchy() {
			super.onRemovedFromHierarchy();
			clear();
		}

		public void clear() {
			if (mesh != null) {
				mesh.dispose();
				mesh = null;
			}
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			if (mesh == null) {
				CacheableSpriteBatch batch = gfx.getSpriteBatch();
				gfx.prepareSprites();
				batch.flush();
				super.render(gfx, v);
				mesh = new GfxMesh(batch.getMeshCopy(), batch.getLastTexture(), batch);
			} else {
				mesh.render(gfx, v);
			}
		}
	}
}