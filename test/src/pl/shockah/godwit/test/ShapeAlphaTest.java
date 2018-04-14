package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.gl.Gfx;

public class ShapeAlphaTest extends State {
	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		gfx.setColor(Color.WHITE);
		gfx.drawFilled(new Rectangle(gfx.getSize()));

		gfx.setColor(new Color(1f, 0f, 0f, 0.5f));
		gfx.drawFilled(new Circle(gfx.getSize() * 0.5f - new MutableVec2(24f, 0f), 64f));
		gfx.drawFilled(new Circle(gfx.getSize() * 0.5f + new MutableVec2(24f, 0f), 64f));
	}
}