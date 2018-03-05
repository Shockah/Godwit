package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
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
	@Nonnull private Alignment alignment = Alignment.Horizontal.Left.and(Alignment.Vertical.Top);

	@Getter
	@Nonnull private LineBreakMode lineBreakMode = LineBreakMode.Wrap;

	public GfxFont(@Nonnull BitmapFont font) {
		this.font = font;
	}

	@Nonnull protected GlyphLayout getGlyphLayout() {
		if (cachedLayout == null)
			cachedLayout = new GlyphLayout(font, text, font.getColor(), maxWidth != null ? maxWidth : 0f, alignment.getGdxAlignment(), lineBreakMode == LineBreakMode.Wrap);
		return cachedLayout;
	}

	public void setText(@Nullable String text) {
		this.text = text;
		cachedLayout = null;
	}

	public void setMaxWidth(@Nullable Float maxWidth) {
		this.maxWidth = maxWidth;
		cachedLayout = null;
	}

	public void setAlignment(@Nonnull Alignment alignment) {
		this.alignment = alignment;
		cachedLayout = null;
	}

	public void setLineBreakMode(@Nonnull LineBreakMode lineBreakMode) {
		this.lineBreakMode = lineBreakMode;
		cachedLayout = null;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (text == null)
			return;

		GlyphLayout layout = getGlyphLayout();
		v = v.subtract(0f, layout.height * alignment.getVector().y());

		gfx.prepareSprites();
		font.draw(gfx.getSpriteBatch(), layout, v.x(), v.y());
	}

	public enum LineBreakMode {
		Wrap, Truncate;
	}
}