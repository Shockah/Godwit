package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Triangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;

public class TriangleCollisionState extends State {
	private static final float length = 32f;
	@Nonnull private static final Vec2 staticTrianglePosition = new Vec2(128, 128);

	@Nonnull private Vec2 trianglePosition = new Vec2(64, 64);
	private float rotation = 0f;

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			trianglePosition.x -= 2f;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			trianglePosition.x += 2f;
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			trianglePosition.y -= 2f;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			trianglePosition.y += 2f;

		rotation -= 2f;

		Triangle triangle1 = new Triangle(
				Vec2.angled(length, rotation) + staticTrianglePosition,
				Vec2.angled(length, rotation + 120) + staticTrianglePosition,
				Vec2.angled(length, rotation + 240) + staticTrianglePosition
		);
		Triangle triangle2 = new Triangle(
				Vec2.angled(length, 0) + trianglePosition,
				Vec2.angled(length, 120) + trianglePosition,
				Vec2.angled(length, 240) + trianglePosition
		);

		gfx.setColor(triangle1.collides(triangle2) ? Color.WHITE : Color.GRAY);
		gfx.drawFilled(triangle1);
		gfx.drawFilled(triangle2);
	}
}