package pl.shockah.godwit.gl.color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Math2;

public class HSVColorSpace extends AbstractColorSpace {
	public float h;
	public float s;
	public float v;

	public HSVColorSpace(float h, float s, float v, float alpha) {
		super(alpha);
		this.h = h;
		this.s = s;
		this.v = v;
	}

	@Nonnull public static HSVColorSpace from(@Nonnull RGBColorSpace rgb) {
		float max = Math2.max(rgb.r, rgb.g, rgb.b);
		float min = Math2.min(rgb.r, rgb.g, rgb.b);
		float range = max - min;

		float h, s, v;
		if (range == 0)
			h = 0;
		else if (max == rgb.r)
			h = (60 * (rgb.g - rgb.b) / range + 360) % 360;
		else if (max == rgb.g)
			h = 60 * (rgb.b - rgb.r) / range + 120;
		else
			h = 60 * (rgb.r - rgb.g) / range + 240;

		if (max > 0)
			s = 1 - min / max;
		else
			s = 0;

		v = max;

		return new HSVColorSpace(h, s, v, rgb.alpha);
	}

	@Override
	@Nonnull public RGBColorSpace toRGB() {
		float x = (h / 60f + 6) % 6;
		int i = (int)x;
		float f = x - i;
		float p = v * (1 - s);
		float q = v * (1 - s * f);
		float t = v * (1 - s * (1 - f));
		switch (i) {
			case 0:
				return new RGBColorSpace(v, t, p, alpha);
			case 1:
				return new RGBColorSpace(q, v, p, alpha);
			case 2:
				return new RGBColorSpace(p, v, t, alpha);
			case 3:
				return new RGBColorSpace(p, q, v, alpha);
			case 4:
				return new RGBColorSpace(t, p, v, alpha);
			default:
				return new RGBColorSpace(v, p, q, alpha);
		}
	}
}