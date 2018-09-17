package pl.shockah.godwit.entity;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.ui.View;

public class ViewEntity extends Entity {
	@Nonnull
	public final View view;

	public ViewEntity(@Nonnull View view) {
		this.view = view;
	}

	@Override
	public void updateSelf() {
		super.updateSelf();
		view.onUpdate();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);
		view.render(gfx, v);
	}
}