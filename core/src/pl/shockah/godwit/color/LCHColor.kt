package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Angle
import pl.shockah.godwit.geom.degrees
import kotlin.math.*

data class LCHColor(
		val l: Float,
		val c: Float,
		val h: Angle,
		val reference: XYZColor.Reference = XYZColor.Reference.D65_2
) : IGColor<LCHColor>() {
	companion object {
		fun from(lab: LabColor): LCHColor {
			var h = atan2(lab.b, lab.a)
			h = (if (h > 0) h / PI * 180 else 360 + h / PI * 180).toFloat()

			return LCHColor(
					lab.l,
					sqrt(lab.a * lab.a + lab.b * lab.b),
					h.degrees,
					lab.reference
			)
		}
	}

	override val rgb by lazy { lab.rgb }

	val exactRgb: RGBColor by lazy { lab.exactRgb }

	val lab: LabColor by lazy {
		return@lazy LabColor(
				l,
				h.cos * c,
				h.sin * c,
				reference
		)
	}

	val hsluv: HSLuvColor by lazy { HSLuvColor.from(this) }

	override fun getDistance(other: LCHColor): Float {
		val delta = h delta other.h
		return sqrt((delta.degrees.value / 180f).pow(2) + (l - other.l).pow(2) + (c - other.c).pow(2))
	}

	override fun ease(other: LCHColor, f: Float): LCHColor {
		return LCHColor(
				l.ease(other.l, f),
				c.ease(other.c, f),
				h.ease(other.h, f),
				reference
		)
	}

	fun with(l: Float = this.l, c: Float = this.c, h: Angle = this.h): LCHColor {
		return LCHColor(l, c, h)
	}
}