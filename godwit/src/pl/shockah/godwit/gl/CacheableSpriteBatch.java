package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import lombok.Getter;

public class CacheableSpriteBatch extends SpriteBatch {
	@Getter(lazy = true)
	@Nonnull private final Field meshField = getInitialMeshField();

	public CacheableSpriteBatch() {
		super(1000, null);
	}

	public CacheableSpriteBatch(int size) {
		super(size, null);
	}

	public CacheableSpriteBatch(int size, ShaderProgram defaultShader) {
		super(size, defaultShader);
	}

	@Nonnull private Field getInitialMeshField() {
		try {
			Field field = SpriteBatch.class.getDeclaredField("mesh");
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull public Mesh getMeshCopy() {
		try {
			return ((Mesh)getMeshField().get(this)).copy(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}