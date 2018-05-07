package pl.shockah.godwit.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.gl.ColorUtil;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.unicorn.algo.tsp.ExactTravellingSalesmanSolver;
import pl.shockah.unicorn.algo.tsp.TravellingSalesmanSolver;
import pl.shockah.unicorn.color.HSVColorSpace;
import pl.shockah.unicorn.operation.AsyncOperation;

public class TravellingSalesmanTest extends State {
	@Nonnull public final Set<IVec2> nodes = new HashSet<>();
	@Nullable public TravellingSalesmanSolver<IVec2>.Route route;
	@Nullable private AsyncOperation<Set<IVec2>, TravellingSalesmanSolver<IVec2>.Route> async;
	@Nullable public Polygon polygon;

	public void addNode(@Nonnull IVec2 v) {
		nodes.add(v);
		if (nodes.size() >= 2) {
			AsyncOperation<Set<IVec2>, TravellingSalesmanSolver<IVec2>.Route> async = new ExactTravellingSalesmanSolver<IVec2>(p -> new float[] { p.x(), p.y() }).async(nodes);
			this.async = async;
			Thread thread = new Thread(() -> {
				System.out.println(String.format("Solving for %d nodes", nodes.size()));
				long ms = TimeUtils.millis();
				async.run();
				TravellingSalesmanSolver<IVec2>.Route route = async.waitAndGetOutput();
				this.route = route;
				this.async = null;
				System.out.println(String.format("Solved in %dms", TimeUtils.millis() - ms));

				polygon = new Polygon();
				polygon.closed = false;
				for (IVec2 node : route.getNodes()) {
					polygon.addPoint(node);
				}
			});
			thread.setDaemon(true);
			thread.start();
		}
	}

	@Override
	public void updateSelf() {
		super.updateSelf();

		AsyncOperation<Set<IVec2>, TravellingSalesmanSolver<IVec2>.Route> async = this.async;
		if (async != null)
			System.out.println(String.format("%.1f %s", async.operation.getProgress(), async.operation.getDescription()));

//		addNode(new Vec2(
//				Godwit.getInstance().random.getFloatRangeGenerator(0f, 1f).generate(),
//				Godwit.getInstance().random.getFloatRangeGenerator(0f, 1f).generate()
//		).multiply(Godwit.getInstance().gfx.getSize()));

		if (Gdx.input.justTouched() && async == null)
			addNode(new Vec2(Gdx.input.getX(), Gdx.input.getY()));
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		Circle circle = new Circle(6f);
		if (route == null || polygon == null) {
			gfx.setColor(Color.WHITE);
			for (IVec2 node : nodes) {
				gfx.drawFilled(circle, v.add(node));
			}
		} else {
			gfx.setColor(Color.WHITE);
			gfx.drawOutline(polygon, v);

			int index = 0;
			for (IVec2 node : route.getNodes()) {
				gfx.setColor(ColorUtil.toGdx(new HSVColorSpace(1f * index / nodes.size(), 1f, 1f)));
				gfx.drawFilled(circle, v.add(node));
				index++;
			}
		}
	}
}