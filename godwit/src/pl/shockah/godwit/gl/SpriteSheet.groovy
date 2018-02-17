package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class SpriteSheet {
	@Nonnull final Texture texture
	final int columns
	final int rows
	final int spacing
	final int margin

	@Nonnull protected final TextureRegion[][] cache

	SpriteSheet(@Nonnull Texture texture, int columns, int rows, int spacing = 0, int margin = 0) {
		this.texture = texture
		this.columns = columns
		this.rows = rows
		this.spacing = spacing
		this.margin = margin

		cache = new TextureRegion[columns][rows]
	}

	int getFrameCount() {
		return columns * rows
	}

	@Nonnull IVec2 getFrameSize() {
		IVec2 textureSize = new Vec2(texture.width, texture.height)
		IVec2 blankSpace = new Vec2(margin) * 2 + new Vec2(spacing) * new Vec2(columns - 1, rows - 1)
		return (textureSize - blankSpace) / new Vec2(columns, rows)
	}

	@Nonnull TextureRegion getAt(int frameIndex) {
		return getAt(frameIndex % columns as int, frameIndex / columns as int)
	}

	@Nonnull TextureRegion getAt(int column, int row) {
		assert (0..<columns).contains(column)
		assert (0..<rows).contains(row)

		TextureRegion region = cache[column][row]
		if (!region) {
			IVec2 size = frameSize
			IVec2 position = new Vec2(margin) + new Vec2(column, row) * (size + new Vec2(spacing)) - new Vec2(spacing)
			region = new TextureRegion(texture, position.x as int, position.y + size.y as int, size.x as int, -size.y as int)
			cache[column][row] = region
		}
		return region
	}
}