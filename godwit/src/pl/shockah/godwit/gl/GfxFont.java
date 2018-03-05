package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.ui.Alignment;

public class GfxFont implements Renderable {
	@Delegate
	@Nonnull public final BitmapFont font;

	@Nullable private GlyphLayout cachedLayout;

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

	@Nonnull public GlyphLayout getGlyphLayout() {
		if (text == null || text.isEmpty())
			throw new IllegalStateException();
		if (cachedLayout == null)
			cachedLayout = new GlyphLayout(font, text, Color.WHITE, maxWidth != null ? maxWidth : 0f, alignment.getHorizontalGdxAlignment(), lineBreakMode == LineBreakMode.Wrap);
		return cachedLayout;
	}

	public void setText(@Nullable String text) {
		if (Objects.equals(text, this.text))
			return;
		this.text = text;
		cachedLayout = null;
	}

	public void setMaxWidth(@Nullable Float maxWidth) {
		if (Objects.equals(maxWidth, this.maxWidth))
			return;
		this.maxWidth = maxWidth;
		cachedLayout = null;
	}

	public void setAlignment(@Nonnull Alignment.Plane alignment) {
		if (alignment == this.alignment)
			return;
		this.alignment = alignment;
		cachedLayout = null;
	}

	public void setLineBreakMode(@Nonnull LineBreakMode lineBreakMode) {
		if (lineBreakMode == this.lineBreakMode)
			return;
		this.lineBreakMode = lineBreakMode;
		cachedLayout = null;
	}

	@Nonnull public IVec2 getScaleVector() {
		BitmapFont.BitmapFontData data = font.getData();
		return new ImmutableVec2(data.scaleX, data.scaleY);
	}

	public void setScaleVector(@Nonnull IVec2 v) {
		setScale(v.x(), v.y());
	}

	public void setScale(float scale) {
		setScale(scale, scale);
	}

	public void setScale(float x, float y) {
		BitmapFont.BitmapFontData data = font.getData();
		if (data.scaleX == x && data.scaleY == y)
			return;
		data.setScale(x, y);
		cachedLayout = null;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (text == null || text.isEmpty())
			return;

		GlyphLayout layout = getGlyphLayout();
		if (alignment.vertical != Alignment.Vertical.Top)
			v = v.subtract(0f, layout.height * alignment.vertical.getVector().y());

		gfx.prepareSprites();
		font.draw(gfx.getSpriteBatch(), layout, v.x(), v.y());
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
		public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.renderSelf(gfx, v);
			gfx.draw(font, v);
		}
	}

	public enum LineBreakMode {
		Wrap, Truncate;
	}
}