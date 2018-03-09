package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import javax.annotation.Nonnull;

import pl.shockah.func.Func3;
import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.ease.Easing;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.HSLColorSpace;
import pl.shockah.godwit.gl.color.HSVColorSpace;
import pl.shockah.godwit.gl.color.LCHColorSpace;
import pl.shockah.godwit.gl.color.LabColorSpace;
import pl.shockah.godwit.gl.color.RGBColorSpace;
import pl.shockah.godwit.gl.color.XYZColorSpace;

public class ColorSpaceTest extends State {
	public static ColorSpaceType type = ColorSpaceType.RGB;
	public static int rotate = 0;
	private static final int X = 256;
	private static final int Y = 256;

	enum ColorSpaceType {
		RGB(RGBColorSpace::new),
		HSV((f1, f2, f3) -> new HSVColorSpace(f1, f2, f3).toRGB()),
		HSL((f1, f2, f3) -> new HSLColorSpace(f1, f2, f3).toRGB()),
		XYZ((f1, f2, f3) -> new XYZColorSpace(
				f1 * XYZColorSpace.Reference.D65_2.x,
				f2 * XYZColorSpace.Reference.D65_2.y,
				f3 * XYZColorSpace.Reference.D65_2.z
		).toRGB()),
		Lab((f1, f2, f3) -> new LabColorSpace(
				f1 * 100f,
				Easing.linear.ease(-85.9283f, 97.9619f, f2),
				Easing.linear.ease(-107.5428f, 94.2014f, f3)
		).toRGB()),
		LCH((f1, f2, f3) -> new LCHColorSpace(
				f1 * 100f,
				f2 * 133.4178f,
				f3
		).toRGB());

		public final Func3<Float, Float, Float, RGBColorSpace> func;

		ColorSpaceType(Func3<Float, Float, Float, RGBColorSpace> func) {
			this.func = func;
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		Rectangle rect = new Rectangle(1f * gfx.getWidth() / X, 1f * gfx.getHeight() / Y);

		if (Gdx.input.justTouched()) {
			if (Gdx.input.isButtonPressed(0))
				type = ColorSpaceType.values()[(type.ordinal() + 1) % ColorSpaceType.values().length];
			else if (Gdx.input.isButtonPressed(1))
				rotate = (rotate + 1) % 3;
		}

		float f3 = (float)Math.sin(TimeUtils.millis() / 2000.0 * Math.PI) * 0.5f + 0.5f;

		//float mouse = Math.min(Math.max(1f * Gdx.input.getY() / gfx.getHeight(), 0f), 1f);
		for (int y = 0; y < Y; y++) {
			float f2 = y / (Y - 1f);
			for (int x = 0; x < X; x++) {
				float f1 = x / (X - 1f);
				try {
					RGBColorSpace space;
					switch (rotate) {
						case 0:
							space = type.func.call(f1, f2, f3);
							break;
						case 1:
							space = type.func.call(f1, f3, f2);
							break;
						case 2:
							space = type.func.call(f3, f1, f2);
							break;
						default:
							throw new IndexOutOfBoundsException();
					}
					gfx.setColor(space.toColor());
					gfx.drawFilled(rect, v + rect.size.multiply(x, y));
				} catch (Exception ignored) {
				}
			}
		}
	}
}