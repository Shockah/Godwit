package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public class SpriteSheet {
	@Nonnull public final Texture texture;
	public final int columns;
	public final int rows;
	public final int spacing;
	public final int margin;
	public final int frameCount;

	@Getter(lazy = true)
	private final IVec2 frameSize = calculateFrameSize();

	@Nonnull private final TextureRegion[][] cache;

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows) {
		this(texture, columns, rows, 0, 0);
	}

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows, int spacing) {
		this(texture, columns, rows, spacing, 0);
	}

	public SpriteSheet(@Nonnull Texture texture, int columns, int rows, int spacing, int margin) {
		this.texture = texture;
		this.columns = columns;
		this.rows = rows;
		this.spacing = spacing;
		this.margin = margin;
		frameCount = columns * rows;
		cache = new TextureRegion[columns][rows];
	}

	@Nonnull private IVec2 calculateFrameSize() {
		IVec2 textureSize = new ImmutableVec2(texture.getWidth(), texture.getHeight());
		IVec2 blankSpace = new ImmutableVec2(margin) * 2 + new ImmutableVec2(spacing) * new ImmutableVec2(columns - 1, rows - 1);
		return (textureSize - blankSpace) / new ImmutableVec2(columns, rows);
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
			IVec2 position = new ImmutableVec2(margin) + new ImmutableVec2(column, row) * (size + new ImmutableVec2(spacing)) - new ImmutableVec2(spacing);
			region = new TextureRegion(texture, (int)position.x(), (int)(position.y() + size.y()), (int)size.x(), (int)-size.y());
			cache[column][row] = region;
		}
		return region;
	}
}