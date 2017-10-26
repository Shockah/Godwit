package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
abstract class Sprite {
	@Nonnull Vec2 offset = new Vec2()

	protected final void draw(@Nonnull SpriteBatch sb, @Nonnull Vec2 pos) {
		draw(sb, pos.x, pos.y)
	}

	protected final void draw(@Nonnull SpriteBatch sb, float x, float y) {
		internalDraw(sb, x + offset.x as float, y + offset.y as float)
	}

	protected abstract void internalDraw(@Nonnull SpriteBatch sb, float x, float y)
}