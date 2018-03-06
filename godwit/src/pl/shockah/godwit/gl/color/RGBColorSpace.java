package pl.shockah.godwit.gl.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.ease.Easing;

public class RGBColorSpace extends AbstractColorSpace<RGBColorSpace> {
	public float r;
	public float g;
	public float b;

	public RGBColorSpace(float r, float g, float b, float alpha) {
		super(alpha);
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Nonnull public static RGBColorSpace from(@Nonnull Color color) {
		return new RGBColorSpace(color.r, color.g, color.b, color.a);
	}

	@Override
	@Nonnull public RGBColorSpace toRGB() {
		return this;
	}

	@Override
	public float getDistance(@Nonnull RGBColorSpace other) {
		return (float)Math.sqrt(
				Math.pow(r - other.r, 2)
						+ Math.pow(g - other.g, 2)
						+ Math.pow(b - other.b, 2)
						+ Math.pow(alpha - other.alpha, 2)
		);
	}

	@Override
	@Nonnull public Color toColor() {
		return new Color(
				MathUtils.clamp(r, 0f, 1f),
				MathUtils.clamp(g, 0f, 1f),
				MathUtils.clamp(b, 0f, 1f),
				MathUtils.clamp(alpha, 0f, 1f)
		);
	}

	@Override
	@Nonnull public RGBColorSpace ease(@Nonnull RGBColorSpace other, float f) {
		return new RGBColorSpace(
				Easing.linear.ease(r, other.r, f),
				Easing.linear.ease(g, other.g, f),
				Easing.linear.ease(b, other.b, f),
				Easing.linear.ease(alpha, other.alpha, f)
		);
	}
}