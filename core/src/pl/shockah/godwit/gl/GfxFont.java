package pl.shockah.godwit.gl;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.Objects;
import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.godwit.CameraGroup;
import pl.shockah.godwit.asset.FreeTypeFontLoader;
import pl.shockah.godwit.asset.SingleAsset;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.ui.Alignment;

public class GfxFont extends AbstractRenderable {
	private interface DelegateExclusions {
		BitmapFontCache getCache();
		float getScaleX();
		float getScaleY();
		Color getColor();
		void setColor(Color color);
	}

	@Delegate(excludes = DelegateExclusions.class)
	@Nonnull public final BitmapFont font;

	@Nullable public final AssetLoaderParameters<BitmapFont> parameters;

	@Nullable private GlyphLayout cachedLayout;
	@Nullable private ScalableBitmapFontCache cache;
	@Nullable private Vec2 cachedSize;
	@Nullable private Vec2 cachedOffset;

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

	@Getter
	@Nonnull private IVec2 position = Vec2.zero;

	@Getter
	@Nonnull private Color color = Color.WHITE;

	public GfxFont(@Nonnull SingleAsset<BitmapFont> asset) {
		this(asset.get(), asset.parameters);
	}

	public GfxFont(@Nonnull BitmapFont font) {
		this(font, null);
	}

	public GfxFont(@Nonnull BitmapFont font, @Nullable AssetLoaderParameters<BitmapFont> parameters) {
		this.font = font;
		this.parameters = parameters;
	}

	protected void markDirty() {
		cachedLayout = null;
		cache = null;
		cachedSize = null;
		cachedOffset = null;
	}

	@Nonnull protected GlyphLayout getGlyphLayout() {
		if (text == null || text.isEmpty())
			throw new IllegalStateException("Cannot get a GlyphLayout for an empty text.");
		if (cachedLayout == null) {
			float oldScaleX = font.getData().scaleX;
			float oldScaleY = font.getData().scaleY;

			if (scaleX != 0f && scaleY != 0f)
				font.getData().setScale(scaleX, scaleY);

			cachedLayout = new GlyphLayout(font, text, Color.WHITE, maxWidth != null ? maxWidth : 0f, Align.left, lineBreakMode == LineBreakMode.Wrap);

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
			cache.addText(getGlyphLayout(), position.x(), position.y());
			cache.setColors(color);
		}
		return cache;
	}

	@Nonnull protected Vec2 getOffset() {
		if (cachedOffset == null) {
			getGlyphLayout();
			Vec2 offset = Vec2.zero;

			if (this.parameters instanceof FreeTypeFontLoader.FreeTypeFontParameter) {
				FreeTypeFontLoader.FreeTypeFontParameter parameters = (FreeTypeFontLoader.FreeTypeFontParameter)this.parameters;
				offset = new Vec2(parameters.borderWidth * scaleX, parameters.borderWidth * scaleY);
			}

			cachedOffset = offset;
		}
		return cachedOffset;
	}

	@Nonnull public Vec2 getSize() {
		if (cachedSize == null) {
			if (text == null || text.isEmpty())
				return Vec2.zero;
			GlyphLayout layout = getGlyphLayout();
			Vec2 size = new Vec2(layout.width, layout.height);

			if (this.parameters instanceof FreeTypeFontLoader.FreeTypeFontParameter) {
				FreeTypeFontLoader.FreeTypeFontParameter parameters = (FreeTypeFontLoader.FreeTypeFontParameter)this.parameters;
				size = size.add(parameters.borderWidth * scaleX, parameters.borderWidth * scaleY * 2f);
			}

			cachedSize = size;
		}
		return cachedSize;
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

	public void setPosition(@Nonnull IVec2 position) {
		if (position.equals(this.position))
			return;
		this.position = position;
		markDirty();
	}

	@Nonnull public Vec2 getScaleVector() {
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

	public void setColor(@Nonnull Color color) {
		if (color.r == this.color.r && color.g == this.color.g && color.b == this.color.b && color.a == this.color.a)
			return;

		this.color = color;
		if (cache != null) {
			cache.setColor(color);
			cache.setColors(color);
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		render(gfx, v, null);
	}

	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v, @Nullable CameraGroup camera) {
		if (scaleX == 0f || scaleY == 0f)
			return;
		if (text == null || text.isEmpty())
			return;

		IVec2 layoutSize = getSize();
		float layoutW = layoutSize.x();
		float layoutH = layoutSize.y();
		IVec2 alignmentVector = alignment.getVector();

		BitmapFont.BitmapFontData data = getData();
		if (camera != null && !camera.getBoundingBox().collides(
				v.x() - alignmentVector.x() * layoutW - 4f,
				v.y() - (scaleY - 1f) * data.ascent - alignmentVector.y() * layoutH - 4f,
				layoutW + 8f,
				layoutH + 8f
		))
			return;

		ScalableBitmapFontCache cache = getCache();
		v = v.subtract(alignmentVector.multiply(layoutW, layoutH)).add(getOffset());

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

		public Entity(@Nonnull SingleAsset<BitmapFont> asset) {
			this(new GfxFont(asset));
		}

		public Entity(@Nonnull BitmapFont font) {
			this(new GfxFont(font));
		}

		public Entity(@Nonnull BitmapFont font, @Nullable AssetLoaderParameters<BitmapFont> parameters) {
			this(new GfxFont(font, parameters));
		}

		public Entity(@Nonnull GfxFont font) {
			this.font = font;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			font.render(gfx, v, getCameraGroup());
		}
	}

	public enum LineBreakMode {
		Wrap, Truncate;
	}
}