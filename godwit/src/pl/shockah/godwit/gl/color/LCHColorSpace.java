package pl.shockah.godwit.gl.color;

import javax.annotation.Nonnull;

public class LCHColorSpace extends AbstractColorSpace {
	public float l;
	public float c;
	public float h;

	public LCHColorSpace(float l, float c, float h, float alpha) {
		super(alpha);
		this.l = l;
		this.c = c;
		this.h = h;
	}

	@Nonnull public static LCHColorSpace from(@Nonnull LabColorSpace lab) {
		float h = (float)Math.atan2(lab.b, lab.a);
		h = h > 0 ? h / (float)Math.PI * 180 : 360 + h / (float)Math.PI * 180;

		return new LCHColorSpace(
				lab.l,
				(float)Math.sqrt(lab.a * lab.a + lab.b * lab.b),
				h / 360f,
				lab.alpha
		);
	}

	@Nonnull public LabColorSpace toLab() {
		float h = this.h * 360f;
		return new LabColorSpace(
				l,
				(float)Math.cos(Math.toRadians(h)) * c,
				(float)Math.sin(Math.toRadians(h)) * c,
				alpha
		);
	}

	@Override
	@Nonnull public RGBColorSpace toRGB() {
		return toLab().toRGB();
	}

	@Nonnull public RGBColorSpace toRGB(@Nonnull XYZColorSpace.Reference reference) {
		return toLab().toRGB(reference);
	}

	@Nonnull public RGBColorSpace toExactRGB() {
		return toLab().toExactRGB();
	}

	@Nonnull public RGBColorSpace toExactRGB(@Nonnull XYZColorSpace.Reference reference) {
		return toLab().toExactRGB(reference);
	}
}