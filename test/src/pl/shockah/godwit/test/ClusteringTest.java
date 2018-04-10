package pl.shockah.godwit.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.State;
import pl.shockah.godwit.algo.cluster.TravellingSalesmanKMeansClustering;
import pl.shockah.godwit.algo.tsp.NearestNeighborTravellingSalesmanSolver;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.color.HSVColorSpace;

public class ClusteringTest extends State {
	@Nonnull public final Set<IVec2> nodes = new HashSet<>();
	@Nullable public List<IVec2>[] clusters;

	@Override
	public void updateSelf() {
		super.updateSelf();

		if (Gdx.input.justTouched()) {
			nodes.add(new Vec2(Gdx.input.getX(), Gdx.input.getY()));
			if (nodes.size() >= 4) {
				System.out.println(String.format("Solving for %d nodes", nodes.size()));
				long ms = TimeUtils.millis();
				clusters = new TravellingSalesmanKMeansClustering<IVec2>(
						v -> new float[] { v.x(), v.y() },
						v -> new Vec2(v[0], v[1]),
						4,
						new NearestNeighborTravellingSalesmanSolver<>(v -> new float[]{v.x(), v.y()}, 0f)
				).run(nodes);
				System.out.println(String.format("Solved in %dms", TimeUtils.millis() - ms));
			}
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		Circle circle = new Circle(6f);
		if (clusters == null) {
			gfx.setColor(Color.WHITE);
			for (IVec2 node : nodes) {
				gfx.drawFilled(circle, v + node);
			}
		} else {
			int index = 0;
			for (List<IVec2> cluster : clusters) {
				gfx.setColor(new HSVColorSpace(1f * index / clusters.length, 1f, 1f).toColor());
				for (IVec2 node : cluster) {
					gfx.drawFilled(circle, v + node);
				}
				index++;
			}
		}
	}
}