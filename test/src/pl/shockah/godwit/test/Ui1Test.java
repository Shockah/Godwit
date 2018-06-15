package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.constraint.AxisConstraint;
import pl.shockah.godwit.constraint.BasicConstraint;
import pl.shockah.godwit.constraint.CenterConstraint;
import pl.shockah.godwit.constraint.ChainChildrenConstraint;
import pl.shockah.godwit.constraint.ChainConstraint;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.constraint.FitChildrenConstraint;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.ui.UiButton;
import pl.shockah.godwit.ui.UiScroll;
import pl.shockah.godwit.ui.Unit;

public class Ui1Test extends State {
	public Ui1Test() {
		UiScroll scroll;
		ui.addChild(scroll = new UiScroll(UiScroll.Direction.Vertical));

		scroll.addConstraint(new BasicConstraint(scroll, Constraint.Attribute.Width, safeArea, new Unit.Pixels(-32f)));
		scroll.addConstraint(new BasicConstraint(scroll, Constraint.Attribute.Height, safeArea, new Unit.Pixels(-32f)));
		scroll.addConstraint(new CenterConstraint(scroll, safeArea, AxisConstraint.Axis.Horizontal));
		scroll.addConstraint(new CenterConstraint(scroll, safeArea, AxisConstraint.Axis.Vertical));

		List<UiButton> buttons = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			final int fi = i;
			UiButton button = new UiButton(self -> {
				System.out.println(String.format("Clicked #%d", fi));
			}) {
				@Override
				public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
					super.render(gfx, v);
					gfx.setColor(isPressed ? Color.GRAY : Color.WHITE);
					gfx.drawFilled(getBounds());
				}
			};

			buttons.add(button);
			scroll.content.addChild(button);
			button.addConstraint(BasicConstraint.withParent(button, Constraint.Attribute.Width));
			button.addConstraint(new BasicConstraint(button.getAttributes().height, new Unit.ScreenHeights(0.1f)));
		}

		scroll.content.addConstraint(new FitChildrenConstraint<>(scroll.content, AxisConstraint.Axis.Vertical, new Unit.Pixels(4f), new Unit.Pixels(8f)));
		scroll.content.addConstraint(new ChainChildrenConstraint<>(scroll.content, AxisConstraint.Axis.Vertical, ChainConstraint.Style.Spread));
	}
}