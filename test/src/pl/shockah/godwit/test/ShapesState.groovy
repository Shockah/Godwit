package pl.shockah.godwit.test

import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.State
import pl.shockah.godwit.geom.Circle
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class ShapesState extends State {
	@Override
	protected void onRender(Gfx gfx) {
		super.onRender(gfx)

		gfx.clear(Color.GRAY)

		def rect = Rectangle.centered(gfx.size / 2, gfx.size / 2)
		def rect2 = new Rectangle(rect.pos.x - 4f as float, rect.pos.y - 4f as float, rect.size.x + 8f as float, rect.size.y + 8f as float)

		def circle = new Circle(rect.pos, Math.min(rect.size.x, rect.size.y) * 0.2f as float)
		def circle2 = new Circle(circle.pos, circle.radius + 4f as float)

		gfx.color = Color.BLACK
		gfx.draw(rect2, true)
		gfx.color = Color.WHITE
		gfx.draw(rect, true)

		gfx.color = Color.BLACK
		gfx.draw(circle2, true)
		gfx.color = Color.WHITE
		gfx.draw(circle, true)
	}
}