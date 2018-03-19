package pl.shockah.godwit.gl.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.fx.ease.Easing;

@EqualsAndHashCode
public class RGBColorSpace implements ColorSpace<RGBColorSpace> {
	public float r;
	public float g;
	public float b;

	public RGBColorSpace(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Nonnull public static RGBColorSpace from(@Nonnull Color color) {
		return new RGBColorSpace(color.r, color.g, color.b);
	}

	@Override
	public String toString() {
		return String.format("[RGBColorSpace: R:%.3f G:%.3f B:%.3f]", r, g, b);
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
		);
	}

	@Override
	@Nonnull public Color toColor() {
		return new Color(
				MathUtils.clamp(r, 0f, 1f),
				MathUtils.clamp(g, 0f, 1f),
				MathUtils.clamp(b, 0f, 1f),
				1f
		);
	}

	@Override
	@Nonnull public RGBColorSpace ease(@Nonnull RGBColorSpace other, float f) {
		return new RGBColorSpace(
				Easing.linear.ease(r, other.r, f),
				Easing.linear.ease(g, other.g, f),
				Easing.linear.ease(b, other.b, f)
		);
	}
}