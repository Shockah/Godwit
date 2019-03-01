package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

val Vector2.godwit: ImmutableVec2
	get() = ImmutableVec2(x, y)

class ImmutableVec2(
		override val x: Float,
		override val y: Float
) : IVec2<ImmutableVec2>() {
	override val lengthSquared by lazy { super.lengthSquared }

	override val length by lazy { sqrt(lengthSquared) }

	override val degrees by lazy { super.degrees }

	override val radians by lazy { super.radians }

	override val gdx by lazy { super.gdx }

	override val normalized by lazy { super.normalized }

	constructor() : this(0f, 0f)

	companion object {
		val ZERO = ImmutableVec2()

		operator fun invoke(angle: Angle, length: Float): ImmutableVec2 {
			val radians = angle.radians
			return ImmutableVec2(radians.cos * length, radians.sin * length)
		}
	}

	override fun immutable(): ImmutableVec2 {
		return this
	}

	override operator fun unaryMinus(): ImmutableVec2 {
		return ImmutableVec2(-x, -y)
	}

	override operator fun plus(vector: Vec2): ImmutableVec2 {
		return ImmutableVec2(x + vector.x, y + vector.y)
	}

	override operator fun minus(vector: Vec2): ImmutableVec2 {
		return ImmutableVec2(x - vector.x, y - vector.y)
	}

	override operator fun times(vector: Vec2): ImmutableVec2 {
		return ImmutableVec2(x * vector.x, y * vector.y)
	}

	override operator fun div(vector: Vec2): ImmutableVec2 {
		return ImmutableVec2(x / vector.x, y / vector.y)
	}

	override operator fun plus(scalar: Float): ImmutableVec2 {
		return normalized * (length + scalar)
	}

	override operator fun minus(scalar: Float): ImmutableVec2 {
		return normalized * (length - scalar)
	}

	override operator fun times(scalar: Float): ImmutableVec2 {
		return ImmutableVec2(x * scalar, y * scalar)
	}

	override operator fun div(scalar: Float): ImmutableVec2 {
		return ImmutableVec2(x / scalar, y / scalar)
	}

	override fun rotated(angle: Angle): ImmutableVec2 {
		return ImmutableVec2(radians + angle.radians, length)
	}
}