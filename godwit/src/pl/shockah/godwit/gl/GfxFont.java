package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.ui.Alignment;

public class GfxFont implements Renderable {
	private interface DelegateExclusions {
		BitmapFontCache getCache();
		float getScaleX();
		float getScaleY();
	}

	@Delegate(excludes = DelegateExclusions.class)
	@Nonnull public final BitmapFont font;

	@Nullable private GlyphLayout cachedLayout;
	@Nullable private ScalableBitmapFontCache cache;

	@Getter
	private float scaleX = 1f;

	@Getter
	private float scaleY = 1f;

	@Getter
	@Nullable private String text;

	@Getter
	@Nullable private Float maxWidth = null;

	@Getter
	@Nonnull private Alignment.Plane alignment = Alignment.Horizontal.Left.and(Alignment.Vertical.Top);

	@Getter
	@Nonnull private LineBreakMode lineBreakMode = LineBreakMode.Wrap;

	public GfxFont(@Nonnull BitmapFont font) {
		this.font = font;
	}

	protected void markDirty() {
		cachedLayout = null;
		cache = null;
	}

	@Nonnull public GlyphLayout getGlyphLayout() {
		if (text == null || text.isEmpty())
			throw new IllegalStateException();
		if (cachedLayout == null) {
			float oldScaleX = font.getData().scaleX;
			float oldScaleY = font.getData().scaleY;

			if (scaleX != 0f && scaleY != 0f)
				font.getData().setScale(scaleX, scaleY);

			cachedLayout = new GlyphLayout(font, text, Color.WHITE, maxWidth != null ? maxWidth : 0f, alignment.getHorizontalGdxAlignment(), lineBreakMode == LineBreakMode.Wrap);

			font.getData().setScale(oldScaleX, oldScaleY);
		}
		return cachedLayout;
	}

	@Nonnull protected ScalableBitmapFontCache getCache() {
		if (cache == null) {
			cache = new ScalableBitmapFontCache(font);
			if (scaleX != 0f && scaleY != 0f) {
				cache.scale.x = scaleX;
				cache.scale.y = scaleY;
			}
			cache.addText(getGlyphLayout(), 0f, 0f);
		}
		return cache;
	}

	public void setText(@Nullable String text) {
		if (Objects.equals(text, this.text))
			return;
		this.text = text;
		markDirty();
	}

	public void setMaxWidth(@Nullable Float maxWidth) {
		if (Objects.equals(maxWidth, this.maxWidth))
			return;
		this.maxWidth = maxWidth;
		markDirty();
	}

	public void setAlignment(@Nonnull Alignment.Plane alignment) {
		if (alignment == this.alignment)
			return;
		this.alignment = alignment;
		markDirty();
	}

	public void setLineBreakMode(@Nonnull LineBreakMode lineBreakMode) {
		if (lineBreakMode == this.lineBreakMode)
			return;
		this.lineBreakMode = lineBreakMode;
		markDirty();
	}

	@Nonnull public IVec2 getScaleVector() {
		BitmapFont.BitmapFontData data = font.getData();
		return new Vec2(data.scaleX, data.scaleY);
	}

	public void setScaleVector(@Nonnull IVec2 v) {
		setScale(v.x(), v.y());
	}

	public void setScale(float scale) {
		setScale(scale, scale);
	}

	public void setScaleX(float scaleX) {
		setScale(scaleX, scaleY);
	}

	public void setScaleY(float scaleY) {
		setScale(scaleX, scaleY);
	}

	public void setScale(float x, float y) {
		if (scaleX == x && scaleY == y)
			return;

		scaleX = x;
		scaleY = y;

		if (scaleX == 0f || scaleY == 0f)
			return;

		markDirty();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (scaleX == 0f || scaleY == 0f)
			return;
		if (text == null || text.isEmpty())
			return;

		GlyphLayout layout = getGlyphLayout();

		Rectangle boundingBox = new Rectangle(
				v.subtract(0f, (scaleY - 1f) * getData().ascent) - alignment.getVector().multiply(layout.width, layout.height),
				layout.width,
				layout.height
		);
		if (!gfx.getBoundingBox().collides(boundingBox))
			return;

		ScalableBitmapFontCache cache = getCache();

		if (alignment.vertical != Alignment.Vertical.Top)
			v = v.subtract(0f, layout.height * alignment.vertical.getVector().y());

		float oldX = cache.getX();
		float oldY = cache.getY();

		cache.translate(v.x(), v.y());
		gfx.prepareSprites();
		cache.draw(gfx.getSpriteBatch());

		cache.setPosition(oldX, oldY);
	}

	@Nonnull public Entity asEntity() {
		return new Entity(this);
	}

	public static class Entity extends pl.shockah.godwit.Entity {
		@Nonnull public final GfxFont font;

		public Entity(@Nonnull BitmapFont font) {
			this(new GfxFont(font));
		}

		public Entity(@Nonnull GfxFont font) {
			this.font = font;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			gfx.draw(font, v);
		}
	}

	public enum LineBreakMode {
		Wrap, Truncate;
	}
}