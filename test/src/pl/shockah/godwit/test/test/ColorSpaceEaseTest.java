package pl.shockah.godwit.test.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.func.Func3;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.HSLColorSpace;
import pl.shockah.godwit.gl.color.HSVColorSpace;
import pl.shockah.godwit.gl.color.LCHColorSpace;
import pl.shockah.godwit.gl.color.LabColorSpace;
import pl.shockah.godwit.gl.color.RGBColorSpace;
import pl.shockah.godwit.gl.color.XYZColorSpace;

public class ColorSpaceEaseTest extends State {
	private static final int COLORS = 255;
	private static final float MARGIN = 4f;
	private static final float SPACING = 0f;

	enum ColorSpaceType {
		RGB((c1, c2, f) -> {
			RGBColorSpace space1 = RGBColorSpace.from(c1);
			RGBColorSpace space2 = RGBColorSpace.from(c2);
			return space1.ease(space2, f).toColor();
		}),
		HSV((c1, c2, f) -> {
			HSVColorSpace space1 = HSVColorSpace.from(RGBColorSpace.from(c1));
			HSVColorSpace space2 = HSVColorSpace.from(RGBColorSpace.from(c2));
			return space1.ease(space2, f).toColor();
		}),
		HSL((c1, c2, f) -> {
			HSLColorSpace space1 = HSLColorSpace.from(RGBColorSpace.from(c1));
			HSLColorSpace space2 = HSLColorSpace.from(RGBColorSpace.from(c2));
			return space1.ease(space2, f).toColor();
		}),
		XYZ((c1, c2, f) -> {
			XYZColorSpace space1 = XYZColorSpace.from(RGBColorSpace.from(c1));
			XYZColorSpace space2 = XYZColorSpace.from(RGBColorSpace.from(c2));
			return space1.ease(space2, f).toColor();
		}),
		Lab((c1, c2, f) -> {
			LabColorSpace space1 = LabColorSpace.from(XYZColorSpace.from(RGBColorSpace.from(c1)));
			LabColorSpace space2 = LabColorSpace.from(XYZColorSpace.from(RGBColorSpace.from(c2)));
			return space1.ease(space2, f).toColor();
		}),
		LCH((c1, c2, f) -> {
			LCHColorSpace space1 = LCHColorSpace.from(LabColorSpace.from(XYZColorSpace.from(RGBColorSpace.from(c1))));
			LCHColorSpace space2 = LCHColorSpace.from(LabColorSpace.from(XYZColorSpace.from(RGBColorSpace.from(c2))));
			return space1.ease(space2, f).toColor();
		});

		public final Func3<Color, Color, Float, Color> func;

		ColorSpaceType(Func3<Color, Color, Float, Color> func) {
			this.func = func;
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		Rectangle rect = new Rectangle(
				1f * (gfx.getWidth() - SPACING * (COLORS - 1)) / COLORS,
				1f * (gfx.getHeight() - MARGIN * (ColorSpaceType.values().length - 1)) / ColorSpaceType.values().length
		);

		for (int i = 0; i < ColorSpaceType.values().length; i++) {
			ColorSpaceType type = ColorSpaceType.values()[i];
			for (int j = 0; j < COLORS; j++) {
				float f = 1f * j / (COLORS - 1);
				gfx.setColor(type.func.call(Color.RED, Color.GREEN, f));
				gfx.drawFilled(rect, v + rect.size.add(SPACING, MARGIN).multiply(j, i));
			}
		}
	}
}