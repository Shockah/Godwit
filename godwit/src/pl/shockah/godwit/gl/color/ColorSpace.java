package pl.shockah.godwit.gl.color;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

public interface ColorSpace {
	@Nonnull RGBColorSpace toRGB();

	@Nonnull default Color toColor() {
		return toRGB().toColor();
	}
}