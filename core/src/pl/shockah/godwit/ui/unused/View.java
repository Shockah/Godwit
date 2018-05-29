package pl.shockah.godwit.ui.unused;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.AbstractRenderable;
import pl.shockah.godwit.gl.Gfx;

public class View extends AbstractRenderable {
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
			gfx.setColor(Color.WHITE);
			gfx.drawFilled(new Rectangle(bounds.size), v);
		}
	}

	@Nonnull
	public Vec2 getIntrinsicSize(@Nonnull IVec2 availableSize) {
		return Vec2.zero;
	}

	public void removeFromParent() {
		if (parent == null)
			throw new IllegalStateException(String.format("View %s does not have a parent.", this));
		parent.remove(this);
		parent = null;
	}
}