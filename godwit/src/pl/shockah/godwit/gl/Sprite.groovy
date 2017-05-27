package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

@CompileStatic
abstract class Sprite {
	Vec2 offset = new Vec2()

	protected final void draw(SpriteBatch sb, Vec2 pos) {
		draw(sb, pos.x, pos.y)
	}

	protected final void draw(SpriteBatch sb, float x, float y) {
		internalDraw(sb, x + offset.x as float, y + offset.y as float)
	}

	protected abstract void internalDraw(SpriteBatch sb, float x, float y)
}