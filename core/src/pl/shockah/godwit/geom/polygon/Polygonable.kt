package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.geom.Shape

class Polygonable private constructor() {
	interface Open : Shape.Outline {
		fun asPolygon(): Polygon
	}

	interface Closed : Shape.Outline, Shape.Filled {
		fun asClosedPolygon(): ClosedPolygon
	}
}