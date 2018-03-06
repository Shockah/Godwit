package pl.shockah.godwit.gl.color;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.ease.Easable;

public interface ColorSpace<CS extends ColorSpace<CS>> extends Easable<CS> {
	@Nonnull RGBColorSpace toRGB();

	float getDistance(@Nonnull CS other);

	@Nonnull default Color toColor() {
		return toRGB().toColor();
	}
}