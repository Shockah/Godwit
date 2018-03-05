package pl.shockah.godwit.gl.color;

import javax.annotation.Nonnull;

public class LabColorSpace extends AbstractColorSpace {
	public float l;
	public float a;
	public float b;

	public LabColorSpace(float l, float a, float b, float alpha) {
		super(alpha);
		this.l = l;
		this.a = a;
		this.b = b;
	}

	@Nonnull public static LabColorSpace from(@Nonnull XYZColorSpace xyz) {
		return from(xyz, XYZColorSpace.Reference.D65_2);
	}

	@Nonnull public static LabColorSpace from(@Nonnull XYZColorSpace xyz, @Nonnull XYZColorSpace.Reference reference) {
		float x = xyz.x / reference.x;
		float y = xyz.y / reference.y;
		float z = xyz.z / reference.z;

		x = x > 0.008856f ? (float)Math.pow(x, 1f / 3f) : (7.787f * x) + (16f / 116f);
		y = y > 0.008856f ? (float)Math.pow(y, 1f / 3f) : (7.787f * y) + (16f / 116f);
		z = z > 0.008856f ? (float)Math.pow(z, 1f / 3f) : (7.787f * z) + (16f / 116f);

		return new LabColorSpace(
				116 * y - 16,
				500 * (x - y),
				200 * (y - z),
				xyz.alpha
		);
	}

	@Nonnull public XYZColorSpace toXYZ() {
		return toXYZ(XYZColorSpace.Reference.D65_2);
	}

	@Nonnull public XYZColorSpace toXYZ(@Nonnull XYZColorSpace.Reference reference) {
		float y = (l + 16) / 116f;
		float x = a / 500f + y;
		float z = y - b / 200f;

		x = x > 0.008856f ? (float)Math.pow(x, 3f) : (x - 16f / 116f) / 7.787f;
		y = y > 0.008856f ? (float)Math.pow(y, 3f) : (y - 16f / 116f) / 7.787f;
		z = z > 0.008856f ? (float)Math.pow(z, 3f) : (z - 16f / 116f) / 7.787f;

		return new XYZColorSpace(
				x * reference.x,
				y * reference.y,
				z * reference.z,
				alpha
		);
	}

	@Override
	@Nonnull public RGBColorSpace toRGB() {
		return toXYZ().toRGB();
	}

	@Nonnull public RGBColorSpace toRGB(@Nonnull XYZColorSpace.Reference reference) {
		return toXYZ(reference).toRGB();
	}

	@Nonnull public RGBColorSpace toExactRGB() {
		return toXYZ().toExactRGB();
	}

	@Nonnull public RGBColorSpace toExactRGB(@Nonnull XYZColorSpace.Reference reference) {
		return toXYZ(reference).toExactRGB();
	}
}