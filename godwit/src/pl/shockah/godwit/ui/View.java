package pl.shockah.godwit.ui;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;

public class View implements Renderable {
	@Nullable public ViewHolder parent = null;
	@Nullable public Color backgroundColor = null;
	@Nonnull public Rectangle bounds = new Rectangle(0, 0);

	public void onUpdate() {
	}

	public void onLayout() {
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (backgroundColor != null) {
			gfx.withColor(backgroundColor, () -> {
				gfx.drawFilled(new Rectangle(bounds.size), v);
			});
		}
	}

	@Nonnull
	public IVec2 getIntrinsicSize(@Nonnull IVec2 availableSize) {
		return new ImmutableVec2();
	}

	public void removeFromParent() {
		if (parent == null)
			throw new IllegalStateException();
		parent.remove(this);
	}
}