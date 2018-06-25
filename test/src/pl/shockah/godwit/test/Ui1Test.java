package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.asset.FreeTypeFontAsset;
import pl.shockah.godwit.asset.FreeTypeFontLoader;
import pl.shockah.godwit.constraint.AxisConstraint;
import pl.shockah.godwit.constraint.BasicConstraint;
import pl.shockah.godwit.constraint.CenterConstraint;
import pl.shockah.godwit.constraint.ChainChildrenConstraint;
import pl.shockah.godwit.constraint.ChainConstraint;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.constraint.FitChildrenConstraint;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxFont;
import pl.shockah.godwit.ui.Alignment;
import pl.shockah.godwit.ui.UiButton;
import pl.shockah.godwit.ui.UiLabel;
import pl.shockah.godwit.ui.UiScroll;
import pl.shockah.godwit.ui.Unit;

public class Ui1Test extends State {
	public Ui1Test() {
		FreeTypeFontAsset fontAsset = new FreeTypeFontAsset("Jellee-Roman.ttf", new FreeTypeFontLoader.FreeTypeFontParameter() {{
			size = 24;
			minFilter = magFilter = Texture.TextureFilter.Linear;
		}});
		loadAsset(fontAsset);

		UiLabel label;
		ui.addChild(label = new UiLabel(new GfxFont(fontAsset)));
		label.addConstraint(label.getAttributes().width.constraint(safeArea));
		label.addConstraint(label.getAttributes().height.constraint(new Unit.Pixels(120f)));
		label.addConstraint(new CenterConstraint(label, safeArea, AxisConstraint.Axis.Horizontal));
		label.addConstraint(label.getAttributes().top.constraint(safeArea));
		label.font.setAlignment(Alignment.Horizontal.Center.and(Alignment.Vertical.Middle));
		label.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

		UiScroll scroll;
		ui.addChild(scroll = new UiScroll(UiScroll.Direction.Vertical));

		scroll.addConstraint(scroll.getAttributes().width.constraint(safeArea, new Unit.Pixels(-32f)));
		scroll.addConstraint(scroll.getAttributes().height.constraint(safeArea, new Unit.Pixels(-128f)));
		scroll.addConstraint(new CenterConstraint(scroll, safeArea, AxisConstraint.Axis.Horizontal));
		scroll.addConstraint(scroll.getAttributes().bottom.constraint(safeArea));

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
			button.addConstraint(button.getAttributes().height.constraint(new Unit.ScreenHeights(0.1f)));
		}

		scroll.content.addConstraint(new FitChildrenConstraint<>(scroll.content, AxisConstraint.Axis.Vertical, new Unit.Pixels(4f), new Unit.Pixels(8f)));
		scroll.content.addConstraint(new ChainChildrenConstraint<>(scroll.content, AxisConstraint.Axis.Vertical, ChainConstraint.Style.Spread));
	}
}