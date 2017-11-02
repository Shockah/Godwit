package pl.shockah.godwit.test

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

		gfx.color = Color.BLACK
		gfx.drawFilled(rect2)
		gfx.color = Color.WHITE
		gfx.drawFilled(rect)

		gfx.color = Color.BLACK
		gfx.drawFilled(circle2)
		gfx.color = Color.WHITE
		gfx.drawFilled(circle)
	}
}