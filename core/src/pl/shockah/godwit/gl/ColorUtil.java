package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;
import pl.shockah.unicorn.Math2;
import pl.shockah.unicorn.color.ColorSpace;
import pl.shockah.unicorn.color.HSLColorSpace;
import pl.shockah.unicorn.color.HSVColorSpace;
import pl.shockah.unicorn.color.RGBColorSpace;
import pl.shockah.unicorn.color.XYZColorSpace;

@UtilityClass
public final class ColorUtil {
	@Nonnull
	public static Color toGdx(@Nonnull ColorSpace color) {
		if (color instanceof RGBColorSpace) {
			RGBColorSpace rgb = (RGBColorSpace)color;
			return new Color(Math2.clamp(rgb.r, 0f, 1f), Math2.clamp(rgb.g, 0f, 1f), Math2.clamp(rgb.b, 0f, 1f), 1f);
		} else {
			return toGdx(color.toRGB());
		}
	}

	@Nonnull
	public static RGBColorSpace toRGB(@Nonnull Color color) {
		return new RGBColorSpace(color.r, color.g, color.b);
	}

	@Nonnull
	public static HSLColorSpace toHSL(@Nonnull Color color) {
		return HSLColorSpace.from(color.r, color.g, color.b);
	}

	@Nonnull
	public static HSVColorSpace toHSV(@Nonnull Color color) {
		return HSVColorSpace.from(color.r, color.g, color.b);
	}

	@Nonnull
	public static XYZColorSpace toXYZ(@Nonnull Color color) {
		return XYZColorSpace.from(color.r, color.g, color.b);
	}
}