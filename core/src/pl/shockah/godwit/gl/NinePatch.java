package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.ui.Padding;
import pl.shockah.unicorn.collection.Array2D;
import pl.shockah.unicorn.collection.MutableArray2D;

public class NinePatch implements Renderable {
	public float scale = 1f;
	@Nonnull protected final Array2D<GfxSprite> parts;
	@Nonnull public Rectangle rectangle = new Rectangle(0f, 0f);
	@Nonnull private Rectangle previousRectangle = rectangle.copy();
	private boolean dirty = true;
	private float previousScale = scale;

	@Getter
	@Nonnull private Color color = Color.WHITE;

	@Getter
	private float alpha = 1f;

	public NinePatch(@Nonnull Texture texture, @Nonnull Padding padding) {
		MutableArray2D<GfxSprite> parts = new MutableArray2D<>(GfxSprite.class, 3, 3);

		int top = (int)padding.top.getPixels();
		int bottom = (int)padding.bottom.getPixels();
		int left = (int)padding.left.getPixels();
		int right = (int)padding.right.getPixels();
		int width = texture.getWidth();
		int height = texture.getHeight();

		parts.set(0, 0, new GfxSprite(new Sprite(new TextureRegion(texture, 0, 0, left, top))));
		parts.set(2, 0, new GfxSprite(new Sprite(new TextureRegion(texture, width - right, 0, right, top))));
		parts.set(0, 2, new GfxSprite(new Sprite(new TextureRegion(texture, 0, height - bottom, left, bottom))));
		parts.set(2, 2, new GfxSprite(new Sprite(new TextureRegion(texture, width - right, height - bottom, right, bottom))));

		parts.set(1, 0, new GfxSprite(new Sprite(new TextureRegion(texture, left, 0, width - left - right, top))));
		parts.set(1, 2, new GfxSprite(new Sprite(new TextureRegion(texture, left, height - bottom, width - left - right, bottom))));
		parts.set(0, 1, new GfxSprite(new Sprite(new TextureRegion(texture, 0, top, left, height - top - bottom))));
		parts.set(2, 1, new GfxSprite(new Sprite(new TextureRegion(texture, width - right, top, right, height - top - bottom))));

		parts.set(1, 1, new GfxSprite(new Sprite(new TextureRegion(texture, left, top, width - left - right, height - top - bottom))));

		for (int y = 0; y < parts.height; y++) {
			for (int x = 0; x < parts.width; x++) {
				parts.get(x, y).setOrigin(0f, 0f);
			}
		}

		this.parts = parts;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareSprites();

		if (dirty || !previousRectangle.equals(rectangle) || previousScale != scale) {
			previousRectangle = rectangle.copy();
			previousScale = scale;
			dirty = false;

			GfxSprite topLeft = parts.get(0, 0);
			GfxSprite top = parts.get(1, 0);
			GfxSprite topRight = parts.get(2, 0);
			GfxSprite left = parts.get(0, 1);
			GfxSprite center = parts.get(1, 1);
			GfxSprite right = parts.get(2, 1);
			GfxSprite bottomLeft = parts.get(0, 2);
			GfxSprite bottom = parts.get(1, 2);
			GfxSprite bottomRight = parts.get(2, 2);

			topLeft.setScale(scale);
			topRight.setScale(scale);
			bottomLeft.setScale(scale);
			bottomRight.setScale(scale);

			float horizontalSize = rectangle.size.x - topLeft.getScaledWidth() - topRight.getScaledWidth();
			float verticalSize = rectangle.size.y - topLeft.getScaledHeight() - bottomLeft.getScaledHeight();

			top.setScale(horizontalSize / top.getWidth(), scale);
			bottom.setScale(horizontalSize / bottom.getWidth(), scale);
			left.setScale(scale, verticalSize / left.getHeight());
			right.setScale(scale, verticalSize / right.getHeight());
			center.setScale(horizontalSize / center.getWidth(), verticalSize / center.getHeight());

			//topLeft.setPosition(0f, 0f);
			topRight.setPosition(rectangle.size.x - topRight.getScaledWidth(), 0f);
			bottomLeft.setPosition(0f, rectangle.size.y - bottomLeft.getScaledWidth());
			bottomRight.setPosition(rectangle.size.x - bottomRight.getScaledWidth(), rectangle.size.y - bottomRight.getScaledWidth());

			top.setPosition(topLeft.getScaledWidth(), 0f);
			bottom.setPosition(bottomLeft.getScaledWidth(), rectangle.size.y - bottom.getScaledHeight());
			left.setPosition(0f, topLeft.getScaledHeight());
			right.setPosition(rectangle.size.x - right.getScaledWidth(), topRight.getScaledHeight());
			center.setPosition(topLeft.getScaledWidth(), topLeft.getScaledHeight());
		}

		for (int y = 0; y < parts.height; y++) {
			for (int x = 0; x < parts.width; x++) {
				gfx.draw(parts.get(x, y), v.add(rectangle.position));
			}
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new Vec2(x, y));
	}

	@Override
	public void render(@Nonnull Gfx gfx) {
		render(gfx, Vec2.zero);
	}

	public void setColor(@Nonnull Color color) {
		if (this.color.equals(color))
			return;

		this.color = color;
		for (int y = 0; y < parts.height; y++) {
			for (int x = 0; x < parts.width; x++) {
				parts.get(x, y).setColor(color);
			}
		}
	}

	public void setAlpha(float alpha) {
		if (this.alpha == alpha)
			return;

		this.alpha = alpha;
		for (int y = 0; y < parts.height; y++) {
			for (int x = 0; x < parts.width; x++) {
				parts.get(x, y).setAlpha(alpha);
			}
		}
	}
}