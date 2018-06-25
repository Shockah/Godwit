package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ConstrainableRenderGroup;
import pl.shockah.godwit.State;
import pl.shockah.godwit.constraint.Constrainable;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.ui.Unit;

public class ConstraintTest extends State {
	public ConstraintTest() {
		ui.addChild(new ConstrainableRenderGroup() {
			{
				addChild(new ConstrainableRenderGroup() {
					@Override
					public void onAddedToParent() {
						super.onAddedToParent();
						addConstraint(getAttributes().width.constraint((Constrainable)getParent(), 0.5f));
						addConstraint(getAttributes().height.constraint((Constrainable)getParent(), 0.5f));
						addConstraint(getAttributes().top.constraint((Constrainable)getParent(), new Unit.Pixels(32f)));
						addConstraint(getAttributes().left.constraint((Constrainable)getParent(), new Unit.Pixels(32f)));
					}

					@Override
					public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
						gfx.setColor(Color.SCARLET);
						gfx.drawFilled(getBounds());
						super.render(gfx, v);
					}
				});
			}

			@Override
			public void onAddedToParent() {
				super.onAddedToParent();
				addConstraint(getAttributes().width.constraint(getCameraGroup(), 0.8f));
				addConstraint(getAttributes().height.constraint(getCameraGroup(), 0.8f));
				addConstraint(getAttributes().centerX.constraint(getCameraGroup()));
				addConstraint(getAttributes().top.constraint(getCameraGroup()));
			}

			@Override
			public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				gfx.setColor(Color.WHITE);
				gfx.drawFilled(getBounds());
				super.render(gfx, v);
			}
		});
	}
}