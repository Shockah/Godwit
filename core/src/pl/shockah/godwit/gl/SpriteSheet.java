package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.jay.JSONObject;

public class SpriteSheet {
	@Nonnull public final Texture texture;
	@Nullable public final JSONObject json;
	public final int columns;
	public final int rows;
	public final int spacing;
	public final int margin;
	public final int frameCount;

	@Getter(lazy = true)
	private final IVec2 frameSize = calculateFrameSize(texture);

	@Nonnull private final TextureRegion[][] cache;

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows) {
		this(texture, columns, rows, 0, 0);
	}

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows, int spacing) {
		this(texture, columns, rows, spacing, 0);
	}

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows, int spacing, int margin) {
		this(texture, null, columns, rows, spacing, margin);
	}

	public SpriteSheet(@Nonnull Texture texture, @Nullable JSONObject json, int columns, int rows, int spacing, int margin) {
		this.texture = texture;
		this.json = json;
		this.columns = columns;
		this.rows = rows;
		this.spacing = spacing;
		this.margin = margin;
		frameCount = columns * rows;
		cache = new TextureRegion[columns][rows];
	}

	@Nonnull private IVec2 calculateFrameSize(@Nonnull Texture texture) {
		IVec2 textureSize = new Vec2(texture.getWidth(), texture.getHeight());
		IVec2 blankSpace = new Vec2(margin, margin) * 2 + new Vec2(spacing, spacing) * new Vec2(columns - 1, rows - 1);
		return (textureSize - blankSpace) / new Vec2(columns, rows);
	}

	@Nonnull public TextureRegion get(int frameIndex) {
		return get(frameIndex % columns, frameIndex / columns);
	}

	@Nonnull public TextureRegion get(int column, int row) {
		if (column < 0 || column >= columns)
			throw new ArrayIndexOutOfBoundsException();
		if (row < 0 || row >= rows)
			throw new ArrayIndexOutOfBoundsException();

		TextureRegion region = cache[column][row];
		if (region == null) {
			IVec2 size = getFrameSize();
			IVec2 position = new Vec2(margin) + new Vec2(column, row) * (size + new Vec2(spacing)) - new Vec2(spacing);
			region = new TextureRegion(texture, (int)position.x(), (int)position.y(), (int)size.x(), (int)size.y());
			//region = new TextureRegion(texture, (int)position.x(), (int)(position.y() + size.y()), (int)size.x(), (int)-size.y());
			cache[column][row] = region;
		}
		return region;
	}
}