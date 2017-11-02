package pl.shockah.godwit.ui

import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.Renderable

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class View implements Renderable {
	@Nullable ViewHolder parent = null
	@Nullable Color backgroundColor = null
	@Nonnull Rectangle bounds = new Rectangle(0, 0)

	void onUpdate() {
	}

	void onLayout() {
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		if (backgroundColor) {
			gfx.color = backgroundColor
			gfx.drawFilled(new Rectangle(bounds.size), x, y)
		}
	}

	@Nonnull Vec2 getIntrinsicSize(@Nonnull Vec2 availableSize) {
		return new Vec2()
	}

	void removeFromParent() {
		assert parent != null
		parent.remove(this)
	}
}