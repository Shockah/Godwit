package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;

public class ViewEntity extends Entity {
	@Nonnull public final View view;

	public ViewEntity(@Nonnull View view) {
		this.view = view;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		view.onUpdate();
	}

	@Override
	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.onRender(gfx, v);
		view.render(gfx, v);
	}
}