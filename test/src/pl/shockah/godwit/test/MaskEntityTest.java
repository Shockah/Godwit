package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.ComplexShape;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.MaskEntity;

public class MaskEntityTest extends State {
	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		game.addChild(new MaskEntity() {{
			mask = new ComplexShape.Filled<Circle>() {{
				add(new Circle(64 - 24, 64, 32f));
				add(new Circle(64 + 24, 64, 32f));
				add(new Circle(64, 64 - 24, 32f));
				add(new Circle(64, 64 + 24, 32f));
			}}.asFilledEntity();
			addChild(new Rectangle(0f, 0f, Godwit.getInstance().gfx.getSize()).asFilledEntity().withColor(Color.RED));
		}});
	}
}