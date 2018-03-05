package pl.shockah.godwit.gl.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import javax.annotation.Nonnull;

public class RGBColorSpace extends AbstractColorSpace {
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
	@Nonnull public Color toColor() {
		return new Color(
				MathUtils.clamp(r, 0f, 1f),
				MathUtils.clamp(g, 0f, 1f),
				MathUtils.clamp(b, 0f, 1f),
				MathUtils.clamp(alpha, 0f, 1f)
		);
	}
}