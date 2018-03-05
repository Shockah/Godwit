package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;

import pl.shockah.func.Func3;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.ColorSpace;
import pl.shockah.godwit.gl.color.HSVColorSpace;
import pl.shockah.godwit.gl.color.RGBColorSpace;
import pl.shockah.godwit.gl.color.XYZColorSpace;

public class ColorSpaceTest extends State {
	public static ColorSpaceType type = ColorSpaceType.RGB;
	private static final int X = 256;
	private static final int Y = 256;

	enum ColorSpaceType {
		RGB((f1, f2, mouse) -> new RGBColorSpace(f1, f2, mouse, 1f)),
		HSV((f1, f2, mouse) -> new HSVColorSpace(mouse * 360f, f1, f2, 1f)),
		XYZ((f1, f2, mouse) -> new XYZColorSpace(f1 * 100f, mouse * 100f, f2 * 108f, 1f));

		public final Func3<Float, Float, Float, ColorSpace> func;

		ColorSpaceType(Func3<Float, Float, Float, ColorSpace> func) {
			this.func = func;
		}
	}

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		Rectangle rect = new Rectangle(1f * gfx.getWidth() / X, 1f * gfx.getHeight() / Y);

		if (Gdx.input.justTouched())
			type = ColorSpaceType.values()[(type.ordinal() + 1) % ColorSpaceType.values().length];

		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				float f1 = x / (X - 1f);
				float f2 = y / (Y - 1f);
				float mouse = Math.min(Math.max(1f * Gdx.input.getY() / gfx.getHeight(), 0f), 1f);
				float alpha = 1f;

				try {
					gfx.setColor(type.func.call(f1, f2, mouse).toColor());
					gfx.drawFilled(rect, v + rect.size.multiply(x, y));
				} catch (Exception ignored) {
				}
			}
		}
	}
}