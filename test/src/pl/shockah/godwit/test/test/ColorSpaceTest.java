package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.XYZColorSpace;

public class ColorSpaceTest extends State {
	private static final int X = 256;
	private static final int Y = 256;

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		Rectangle rect = new Rectangle(1f * gfx.getWidth() / X, 1f * gfx.getHeight() / Y);

		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				float f1 = x / (X - 1f);
				float f2 = y / (Y - 1f);
				float mouse = Math.min(Math.max(1f * Gdx.input.getY() / gfx.getHeight(), 0f), 1f);
				float alpha = 1f;

				try {
					gfx.setColor(new XYZColorSpace(f1 * 100f, mouse * 100f, f2 * 108f, alpha).toColor());
					gfx.drawFilled(rect, v + rect.size.multiply(x, y));
				} catch (Exception ignored) {
				}
			}
		}
	}
}