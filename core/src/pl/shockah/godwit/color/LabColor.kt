package pl.shockah.godwit.color

import pl.shockah.godwit.ease.ease
import kotlin.math.pow
import kotlin.math.sqrt

data class LabColor(
		val l: Float,
		val a: Float,
		val b: Float,
		val reference: XYZColor.Reference = XYZColor.Reference.D65_2
) : IGColor<LabColor>() {
	companion object {
		fun from(xyz: XYZColor, reference: XYZColor.Reference = XYZColor.Reference.D65_2): LabColor {
			var x = xyz.x / reference.x
			var y = xyz.y / reference.y
			var z = xyz.z / reference.z

			x = if (x > 0.008856f) x.pow(1f / 3f) else 7.787f * x + 16f / 116f
			y = if (y > 0.008856f) y.pow(1f / 3f) else 7.787f * y + 16f / 116f
			z = if (z > 0.008856f) z.pow(1f / 3f) else 7.787f * z + 16f / 116f

			return LabColor(
					116 * y - 16,
					500 * (x - y),
					200 * (y - z),
					reference
			)
		}
	}

	override val rgb by lazy { xyz.rgb }

	val exactRgb: RGBColor by lazy { xyz.exactRgb }

	val xyz: XYZColor by lazy {
		var y = (l + 16) / 116f
		var x = a / 500f + y
		var z = y - b / 200f

		x = if (x > 0.008856f) x.pow(3) else (x - 16f / 116f) / 7.787f
		y = if (y > 0.008856f) y.pow(3) else (y - 16f / 116f) / 7.787f
		z = if (z > 0.008856f) z.pow(3) else (z - 16f / 116f) / 7.787f

		return@lazy XYZColor(
				x * reference.x,
				y * reference.y,
				z * reference.z
		)
	}

	val lch: LCHColor by lazy { LCHColor.from(this) }

	override fun getDistance(other: LabColor): Float {
		return sqrt((l - other.l).pow(2) * 0.01f + (a - other.a).pow(2) * 0.005f + (b - other.b).pow(2) * 0.005f)
	}

	override fun ease(other: LabColor, f: Float): LabColor {
		return LabColor(
				l.ease(other.l, f),
				a.ease(other.a, f),
				b.ease(other.b, f),
				reference
		)
	}

	fun with(l: Float = this.l, a: Float = this.a, b: Float = this.b): LabColor {
		return LabColor(l, a, b)
	}
}