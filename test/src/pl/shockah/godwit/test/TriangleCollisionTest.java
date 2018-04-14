package pl.shockah.godwit.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Triangle;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.gl.Gfx;

public class TriangleCollisionTest extends State {
	private static final float length = 32f;
	@Nonnull private static final MutableVec2 staticTrianglePosition = new MutableVec2(128, 128);

	@Nonnull private MutableVec2 trianglePosition = new MutableVec2(64, 64);
	private float rotation = 0f;

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

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
				MutableVec2.angled(length, rotation) + staticTrianglePosition,
				MutableVec2.angled(length, rotation + 120) + staticTrianglePosition,
				MutableVec2.angled(length, rotation + 240) + staticTrianglePosition
		);
		Triangle triangle2 = new Triangle(
				MutableVec2.angled(length, 0) + trianglePosition,
				MutableVec2.angled(length, 120) + trianglePosition,
				MutableVec2.angled(length, 240) + trianglePosition
		);

		gfx.setColor(triangle1.collides(triangle2) ? Color.WHITE : Color.GRAY);
		gfx.drawFilled(triangle1);
		gfx.drawFilled(triangle2);
	}
}