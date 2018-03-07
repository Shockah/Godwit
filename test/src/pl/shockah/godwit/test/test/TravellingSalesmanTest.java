package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.State;
import pl.shockah.godwit.algo.tsp.CloseTravellingSalesmanSolver;
import pl.shockah.godwit.algo.tsp.TravellingSalesmanSolver;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.HSVColorSpace;

public class TravellingSalesmanTest extends State {
	@Nonnull public final Set<IVec2> nodes = new HashSet<>();
	@Nullable public TravellingSalesmanSolver<IVec2>.Route route;
	@Nullable public Polygon polygon;

	@Override
	public void updateSelf() {
		super.updateSelf();

		if (Gdx.input.justTouched()) {
			nodes.add(new ImmutableVec2(Gdx.input.getX(), Gdx.input.getY()));
			if (nodes.size() >= 2) {
				System.out.println(String.format("Solving for %d nodes", nodes.size()));
				long ms = TimeUtils.millis();
				route = CloseTravellingSalesmanSolver.forVec2(0.1f).solve(nodes);
				System.out.println(String.format("Solved in %dms", TimeUtils.millis() - ms));

				polygon = new Polygon();
				polygon.closed = false;
				for (IVec2 node : route.getNodes()) {
					polygon.addPoint(node);
				}
			}
		}
	}

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);

		Circle circle = new Circle(6f);
		if (route == null || polygon == null) {
			gfx.setColor(Color.WHITE);
			for (IVec2 node : nodes) {
				gfx.drawFilled(circle, v + node);
			}
		} else {
			gfx.setColor(Color.WHITE);
			gfx.drawOutline(polygon, v);

			int index = 0;
			for (IVec2 node : route.getNodes()) {
				gfx.setColor(new HSVColorSpace(1f * index / nodes.size(), 1f, 1f).toColor());
				gfx.drawFilled(circle, v + node);
				index++;
			}
		}
	}
}