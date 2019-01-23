package pl.shockah.godwit.color

import pl.shockah.godwit.geom.degrees
import kotlin.math.*

data class LCHColor(
		val l: Float,
		val c: Float,
		val h: Float,
		val reference: XYZColor.Reference = XYZColor.Reference.D65_2
) : IGColor<LCHColor>() {
	companion object {
		fun from(lab: LabColor): LCHColor {
			var h = atan2(lab.b, lab.a)
			h = (if (h > 0) h / PI * 180 else 360 + h / PI * 180).toFloat()

			return LCHColor(
					lab.l,
					sqrt(lab.a * lab.a + lab.b * lab.b),
					h / 360f,
					lab.reference
			)
		}
	}

	override val rgb by lazy { lab.rgb }

	val exactRgb: RGBColor by lazy { lab.exactRgb }

	val lab: LabColor by lazy {
		val h = this.h * 360f
		return@lazy LabColor(
				l,
				cos(Math.toRadians(h.toDouble())).toFloat() * c,
				sin(Math.toRadians(h.toDouble())).toFloat() * c,
				reference
		)
	}

	val hsluv: HSLuvColor by lazy { HSLuvColor.from(this) }

	override fun getDistance(other: LCHColor): Float {
		var delta = (h * 360f).degrees delta (other.h * 360f).degrees
		if (delta.value < 0)
			delta = (delta.value + 360f).degrees
		return sqrt((delta.value / 360f).pow(2) + (l - other.l).pow(2) + (c - other.c).pow(2))
	}

	override fun ease(other: LCHColor, f: Float): LCHColor {
		val delta = (h * 360f).degrees delta (other.h * 360f).degrees
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return LCHColor(
				l.ease(other.l, f),
				c.ease(other.c, f),
				h,
				reference
		)
	}
}