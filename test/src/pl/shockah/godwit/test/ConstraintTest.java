package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ConstrainableRenderGroup;
import pl.shockah.godwit.State;
import pl.shockah.godwit.constraint.BasicConstraint;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;

public class ConstraintTest extends State {
	public ConstraintTest() {
		ui.addChild(new ConstrainableRenderGroup() {
			@Override
			public void onAddedToParent() {
				super.onAddedToParent();
				addConstraint(new BasicConstraint(this, Constraint.Attribute.Width, getCameraGroup(), 0.8f));
				addConstraint(new BasicConstraint(this, Constraint.Attribute.Height, getCameraGroup(), 0.8f));
				addConstraint(new BasicConstraint(this, Constraint.Attribute.CenterX, getCameraGroup()));
				addConstraint(new BasicConstraint(this, Constraint.Attribute.Top, getCameraGroup()));
			}

			@Override
			public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				super.render(gfx, v);
				gfx.setColor(Color.WHITE);
				gfx.drawFilled(getBounds());
			}
		});
	}
}