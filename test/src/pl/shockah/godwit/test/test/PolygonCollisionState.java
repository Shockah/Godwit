package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Triangle;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.gl.Gfx;

public class PolygonCollisionState extends State {
	@Nonnull private final Polygon polygon1 = new Polygon();
	@Nonnull private final Polygon polygon2 = new Polygon();

	public PolygonCollisionState() {
		polygon1.closed = true;
		polygon2.closed = true;
	}

	@Override
	public void updateSelf() {
		super.updateSelf();

		if (Gdx.input.justTouched()) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				polygon1.addPoint(Gdx.input.getX(), Gdx.input.getY());
			} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				polygon2.addPoint(Gdx.input.getX(), Gdx.input.getY());
			}
		}
	}

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		renderPolygon(gfx, v, polygon1, polygon2, new Color(1f, 0f, 0f, 1f), new Color(1f, 0.5f, 0.5f, 1f), new Color(0.5f, 0.5f, 1f, 0.5f));
		renderPolygon(gfx, v, polygon2, polygon1, new Color(0f, 1f, 0f, 1f), new Color(0.5f, 1f, 0.5f, 1f), new Color(1f, 0.5f, 1f, 0.5f));
	}

	private void renderPolygon(@Nonnull Gfx gfx, @Nonnull IVec2 v, @Nonnull Polygon polygon, @Nonnull Polygon polygon2, @Nonnull Color color1, @Nonnull Color color2, @Nonnull Color color3) {
		if (polygon.getPointCount() >= 3) {
			for (Triangle triangle : polygon.getTriangles()) {
				boolean collides = false;
				if (polygon2.getPointCount() >= 3) {
					if (polygon2.collides(triangle))
						collides = true;
				}
				if (collides)
					gfx.setColor(color3);
				else
					gfx.setColor(new Color(color1.r, color1.g, color1.b, 0.5f));
				gfx.drawFilled(triangle, v);
			}
			gfx.setColor(color1);
			for (Triangle triangle : polygon.getTriangles()) {
				gfx.drawOutline(triangle, v);
			}
		}
		gfx.setColor(new Color(color2.r, color2.g, color2.b, 0.5f));
		for (int i = 0; i < polygon.getPointCount(); i++) {
			gfx.drawFilled(new Circle(polygon.get(i), 4f), v);
		}
		gfx.setColor(color2);
		for (int i = 0; i < polygon.getPointCount(); i++) {
			gfx.drawOutline(new Circle(polygon.get(i), 4f), v);
		}
	}
}