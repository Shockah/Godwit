package pl.shockah.godwit.gl.color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Math2;
import pl.shockah.godwit.fx.ease.Easable;
import pl.shockah.godwit.fx.ease.Easing;

public class LCHColorSpace extends AbstractColorSpace implements Easable<LCHColorSpace> {
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

	@Override
	@Nonnull public LCHColorSpace ease(@Nonnull LCHColorSpace other, float f) {
		float h2 = Math2.deltaAngle(this.h, other.h) > 0 ? other.h : other.h - 1f;
		float h = Easing.linear.ease(this.h, h2, f);
		if (h < 0)
			h += 1f;
		if (h > 0)
			h -= 1f;

		return new LCHColorSpace(
				Easing.linear.ease(l, other.l, f),
				Easing.linear.ease(c, other.c, f),
				h,
				Easing.linear.ease(alpha, other.alpha, f)
		);
	}
}