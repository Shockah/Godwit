package pl.shockah.godwit.test

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
	void onRender(@Nonnull Gfx gfx) {
		gfx.clear(Color.GRAY)

		def entity = entities.find { it instanceof AttachmentEntity } as AttachmentEntity
		if (entity) {
			gfx.offset = gfx.size / 2 - entity.pos
			gfx.updateCamera()
		}

		gfx.color = Color.RED
		gfx.drawFilled(Rectangle.centered(gfx.size / 2f, gfx.size * 0.2f))

		super.onRender(gfx)
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
		void onRender(@Nonnull Gfx gfx) {
			super.onRender(gfx)
			gfx.color = Color.WHITE
			gfx.drawFilled(circle, pos)
		}
	}
}