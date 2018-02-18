package pl.shockah.godwit.test.test

import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.State
import pl.shockah.godwit.geom.Circle
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
class ShapesState extends State {
	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)

		gfx.clear(Color.GRAY)

		def rect = Rectangle.centered(gfx.size / 2, gfx.size / 2)
		def rect2 = new Rectangle(rect.position.x - 4f as float, rect.position.y - 4f as float, rect.size.x + 8f as float, rect.size.y + 8f as float)

		def circle = new Circle(rect.position, Math.min(rect.size.x, rect.size.y) * 0.2f as float)
		def circle2 = new Circle(circle.position, circle.radius + 4f as float)

		gfx.with {
			color = Color.BLACK
			drawFilled(rect2)

			color = Color.WHITE
			drawFilled(rect)

			color = Color.BLACK
			drawFilled(circle2)

			color = Color.WHITE
			drawFilled(circle)
		}
	}
}