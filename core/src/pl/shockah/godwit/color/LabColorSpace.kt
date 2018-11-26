package pl.shockah.godwit.color

import kotlin.math.pow
import kotlin.math.sqrt

data class LabColorSpace(
		var l: Float,
		var a: Float,
		var b: Float,
		val reference: XYZColorSpace.Reference = XYZColorSpace.Reference.D65_2
) : ColorSpace<LabColorSpace> {
	companion object {
		fun from(xyz: XYZColorSpace, reference: XYZColorSpace.Reference = XYZColorSpace.Reference.D65_2): LabColorSpace {
			var x = xyz.x / reference.x
			var y = xyz.y / reference.y
			var z = xyz.z / reference.z

			x = if (x > 0.008856f) x.pow(1f / 3f) else 7.787f * x + 16f / 116f
			y = if (y > 0.008856f) y.pow(1f / 3f) else 7.787f * y + 16f / 116f
			z = if (z > 0.008856f) z.pow(1f / 3f) else 7.787f * z + 16f / 116f

			return LabColorSpace(
					116 * y - 16,
					500 * (x - y),
					200 * (y - z),
					reference
			)
		}
	}

	override val rgb: RGBColorSpace
		get() = xyz.rgb

	val exactRgb: RGBColorSpace
		get() = xyz.exactRgb

	val xyz: XYZColorSpace
		get() {
			var y = (l + 16) / 116f
			var x = a / 500f + y
			var z = y - b / 200f

			x = if (x > 0.008856f) x.pow(3) else (x - 16f / 116f) / 7.787f
			y = if (y > 0.008856f) y.pow(3) else (y - 16f / 116f) / 7.787f
			z = if (z > 0.008856f) z.pow(3) else (z - 16f / 116f) / 7.787f

			return XYZColorSpace(
					x * reference.x,
					y * reference.y,
					z * reference.z
			)
		}

	override fun copy(): LabColorSpace = LabColorSpace(l, a, b, reference)

	override fun getDistance(other: LabColorSpace): Float {
		return sqrt((l - other.l).pow(2) * 0.01f + (a - other.a).pow(2) * 0.005f + (b - other.b).pow(2) * 0.005f)
	}

	override fun ease(other: LabColorSpace, f: Float): LabColorSpace {
		return LabColorSpace(
				l.ease(other.l, f),
				a.ease(other.a, f),
				b.ease(other.b, f),
				reference
		)
	}
}