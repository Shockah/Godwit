package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;

public class AttachedViewportTest extends State {
	public static void configure(@Nonnull Lwjgl3ApplicationConfiguration config) {
		config.setWindowedMode(256, 256);
	}

	public AttachedViewportTest() {
		game.addChild(new AttachmentEntity());
	}

	@Override
	public void update() {
		super.update();

		StreamSupport.stream(game.children.get()).filter(e -> e instanceof AttachmentEntity).map(e -> (AttachmentEntity)e).findFirst().ifPresent(e -> {
			game.getCamera().position.x = e.position.x;
			game.getCamera().position.y = e.position.y;
		});
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.clear(Color.GRAY);
		gfx.setColor(Color.RED);
		gfx.drawFilled(Rectangle.centered(gfx.getSize() / 2f + v, gfx.getSize() * 0.2f));
		super.render(gfx, v);
	}

	public class AttachmentEntity extends Entity {
		@Nonnull Circle circle = new Circle(24f);

		@Override
		public void updateSelf() {
			super.updateSelf();
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
				position.x -= 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				position.x += 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.UP))
				position.y -= 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
				position.y += 2f;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);
			gfx.setColor(Color.WHITE);
			gfx.drawFilled(circle, v);
		}
	}
}