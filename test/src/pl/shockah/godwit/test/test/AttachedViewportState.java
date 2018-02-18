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
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.test.Configurable;

public class AttachedViewportState extends State implements Configurable {
	@Override
	public void configure(@Nonnull Lwjgl3ApplicationConfiguration config) {
		config.setWindowedMode(256, 256);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new AttachmentEntity().create(this);
	}

	@Override
	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.clear(Color.GRAY);

		StreamSupport.stream(entities).filter(e -> e instanceof AttachmentEntity).map(e -> (AttachmentEntity)e).findFirst().ifPresent(e -> {
			gfx.setCameraPosition(e.pos);
		});

		gfx.setColor(Color.RED);
		gfx.drawFilled(Rectangle.centered(gfx.getSize() / 2f + v, gfx.getSize() * 0.2f));

		super.onRender(gfx, v);
	}

	public class AttachmentEntity extends Entity {
		@Nonnull Vec2 pos = new Vec2();
		@Nonnull Circle circle = new Circle(24f);

		@Override
		public void onUpdate() {
			super.onUpdate();
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
				pos.x -= 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				pos.x += 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.UP))
				pos.y -= 2f;
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
				pos.y += 2f;
		}

		@Override
		public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.onRender(gfx, v);
			gfx.setColor(Color.WHITE);
			gfx.drawFilled(circle, pos + v);
		}
	}
}