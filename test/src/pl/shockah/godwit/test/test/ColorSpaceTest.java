package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.HSVColorSpace;
import pl.shockah.godwit.gl.color.RGBColorSpace;

public class ColorSpaceTest extends State {
	private static final int X = 32;
	private static final int Y = 32;
	private static final float SPACER = 4;

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		Rectangle rect = new Rectangle(1f * (gfx.getWidth() - SPACER) / X / 2f, 1f * gfx.getHeight() / Y);

		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				float f1 = x / (X - 1f);
				float f2 = y / (Y - 1f);
				float mouse = Math.min(Math.max(1f * Gdx.input.getY() / gfx.getHeight(), 0f), 1f);
				float alpha = 1f;

				try {
					Color baseColor = new Color(f1, mouse, f2, alpha);
					gfx.setColor(baseColor);
					gfx.drawFilled(rect, v + rect.size.multiply(x, y));
					gfx.setColor(HSVColorSpace.from(RGBColorSpace.from(baseColor)).toColor());
					gfx.drawFilled(rect, v.add(rect.size.x * X + SPACER, 0f) + rect.size.multiply(x, y));
				} catch (Exception ignored) {
				}
			}
		}
	}
}