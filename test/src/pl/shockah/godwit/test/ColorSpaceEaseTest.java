package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.entity.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.ColorUtil;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.unicorn.color.HSLColorSpace;
import pl.shockah.unicorn.color.HSLuvColorSpace;
import pl.shockah.unicorn.color.HSVColorSpace;
import pl.shockah.unicorn.color.LCHColorSpace;
import pl.shockah.unicorn.color.LabColorSpace;
import pl.shockah.unicorn.color.RGBColorSpace;
import pl.shockah.unicorn.color.XYZColorSpace;
import pl.shockah.unicorn.func.Func3;

public class ColorSpaceEaseTest extends State {
	private static final int COLORS = 255;
	private static final float MARGIN = 4f;
	private static final float SPACING = 0f;

	enum ColorSpaceType {
		RGB((c1, c2, f) -> {
			RGBColorSpace space1 = ColorUtil.toRGB(c1);
			RGBColorSpace space2 = ColorUtil.toRGB(c2);
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		HSV((c1, c2, f) -> {
			HSVColorSpace space1 = ColorUtil.toHSV(c1);
			HSVColorSpace space2 = ColorUtil.toHSV(c2);
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		HSL((c1, c2, f) -> {
			HSLColorSpace space1 = ColorUtil.toHSL(c1);
			HSLColorSpace space2 = ColorUtil.toHSL(c2);
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		XYZ((c1, c2, f) -> {
			XYZColorSpace space1 = ColorUtil.toXYZ(c1);
			XYZColorSpace space2 = ColorUtil.toXYZ(c2);
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		Lab((c1, c2, f) -> {
			LabColorSpace space1 = LabColorSpace.from(ColorUtil.toXYZ(c1));
			LabColorSpace space2 = LabColorSpace.from(ColorUtil.toXYZ(c2));
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		LCH((c1, c2, f) -> {
			LCHColorSpace space1 = LCHColorSpace.from(LabColorSpace.from(ColorUtil.toXYZ(c1)));
			LCHColorSpace space2 = LCHColorSpace.from(LabColorSpace.from(ColorUtil.toXYZ(c2)));
			return ColorUtil.toGdx(space1.ease(space2, f));
		}),
		HSLuv((c1, c2, f) -> {
			HSLuvColorSpace space1 = HSLuvColorSpace.from(LCHColorSpace.from(LabColorSpace.from(ColorUtil.toXYZ(c1))));
			HSLuvColorSpace space2 = HSLuvColorSpace.from(LCHColorSpace.from(LabColorSpace.from(ColorUtil.toXYZ(c2))));
			return ColorUtil.toGdx(space1.ease(space2, f));
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
				gfx.drawFilled(rect, v.add(rect.size.add(SPACING, MARGIN).multiply(j, i)));
			}
		}
	}
}