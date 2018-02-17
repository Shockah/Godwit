package pl.shockah.godwit.test.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.Entity
import pl.shockah.godwit.State
import pl.shockah.godwit.geom.Circle
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.test.Configurable

import javax.annotation.Nonnull

@CompileStatic
class AttachedViewportState extends State implements Configurable {
	@Override
	void configure(@Nonnull Lwjgl3ApplicationConfiguration config) {
		config.setWindowedMode(256, 256)
	}

	@Override
	protected void onCreate() {
		super.onCreate()
		new AttachmentEntity().create(this)
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		gfx.clear(Color.GRAY)

		(entities.find { it instanceof AttachmentEntity } as AttachmentEntity)?.with {
			gfx.cameraPosition = it.pos
		}

		gfx.withColor(Color.RED) {
			drawFilled(Rectangle.centered(gfx.size / 2f, gfx.size * 0.2f))
		}

		super.onRender(gfx, x, y)
	}

	class AttachmentEntity extends Entity {
		@Nonnull Vec2 pos = new Vec2()
		@Nonnull Circle circle = new Circle(24f)

		@Override
		protected void onUpdate() {
			super.onUpdate()
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
				pos.x -= 2f
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				pos.x += 2f
			if (Gdx.input.isKeyPressed(Input.Keys.UP))
				pos.y -= 2f
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
				pos.y += 2f
		}

		@Override
		void onRender(@Nonnull Gfx gfx, float x, float y) {
			super.onRender(gfx, x, y)
			gfx.withColor(Color.WHITE) {
				drawFilled(circle, pos)
			}
		}
	}
}