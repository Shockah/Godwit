package pl.shockah.godwit.test.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;

public class ShapesState extends State {
	@Override
	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.onRender(gfx, v);

		gfx.clear(Color.GRAY);

		Rectangle rect = Rectangle.centered(gfx.getSize() / 2, gfx.getSize() / 2);
		Rectangle rect2 = new Rectangle(rect.position.x - 4f, rect.position.y - 4f, rect.size.x + 8f, rect.size.y + 8f);

		Circle circle = new Circle(rect.position, Math.min(rect.size.x, rect.size.y) * 0.2f);
		Circle circle2 = new Circle(circle.position, circle.radius + 4f);

		gfx.setColor(Color.BLACK);
		gfx.drawFilled(rect2, v);

		gfx.setColor(Color.WHITE);
		gfx.drawFilled(rect, v);

		gfx.setColor(Color.BLACK);
		gfx.drawFilled(circle2, v);

		gfx.setColor(Color.WHITE);
		gfx.drawFilled(circle, v);
	}
}