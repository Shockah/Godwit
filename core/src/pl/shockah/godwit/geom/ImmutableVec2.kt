package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import kotlin.math.cos
import kotlin.math.sin

class ImmutableVec2(
		override val x: Float,
		override val y: Float
) : IVec2<ImmutableVec2>() {
	override val lengthSquared by lazy { super.lengthSquared }

	override val length by lazy { super.length }

	override val angle by lazy { super.angle }

	override val gdx by lazy { super.gdx }

	override val normalized by lazy { super.normalized }

	constructor() : this(0f, 0f)

	companion object {
		val ZERO = ImmutableVec2()

		fun angled(degrees: Degrees, length: Float): ImmutableVec2 {
			val angle = Math.toRadians((degrees.value + 180f).toDouble())
			return ImmutableVec2(
					(-cos(angle) * length).toFloat(),
					(-sin(angle) * length).toFloat()
			)
		}
	}

	override operator fun unaryMinus(): ImmutableVec2 = ImmutableVec2(-x, -y)

	override operator fun plus(vector: Vec2): ImmutableVec2 = ImmutableVec2(x + vector.x, y + vector.y)
	override operator fun minus(vector: Vec2): ImmutableVec2 = ImmutableVec2(x - vector.x, y - vector.y)
	override operator fun times(vector: Vec2): ImmutableVec2 = ImmutableVec2(x * vector.x, y * vector.y)
	override operator fun div(vector: Vec2): ImmutableVec2 = ImmutableVec2(x / vector.x, y / vector.y)

	override operator fun plus(scalar: Float): ImmutableVec2 = normalized * (length + scalar)
	override operator fun minus(scalar: Float): ImmutableVec2 = normalized * (length - scalar)
	override operator fun times(scalar: Float): ImmutableVec2 = ImmutableVec2(x * scalar, y * scalar)
	override operator fun div(scalar: Float): ImmutableVec2 = ImmutableVec2(x / scalar, y / scalar)

	override fun rotated(degrees: Degrees): ImmutableVec2 {
		return angled(angle + degrees, length)
	}

	val Vector2.godwit: ImmutableVec2
		get() = ImmutableVec2(x, y)
}