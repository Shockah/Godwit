package pl.shockah.godwit

import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.vec2

interface Alignment {
	companion object {
		val Centered = Horizontal.Center + Vertical.Middle
	}

	val multiplier: ImmutableVec2

	enum class Horizontal(override val multiplier: ImmutableVec2) : Alignment {
		Left(0f), Center(0.5f), Right(1f);

		constructor(multiplier: Float) : this(vec2(multiplier, Float.NaN))
	}

	enum class Vertical(override val multiplier: ImmutableVec2) : Alignment {
		Top(0f), Middle(0.5f), Bottom(1f);

		constructor(multiplier: Float) : this(vec2(Float.NaN, multiplier))
	}

	operator fun plus(horizontal: Horizontal): Alignment {
		return object : Alignment {
			override val multiplier = vec2(horizontal.multiplier.x, this@Alignment.multiplier.y)
		}
	}

	operator fun plus(vertical: Vertical): Alignment {
		return object : Alignment {
			override val multiplier = vec2(this@Alignment.multiplier.x, vertical.multiplier.y)
		}
	}
}