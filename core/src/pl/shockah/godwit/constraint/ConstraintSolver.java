package pl.shockah.godwit.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.geom.Rectangle;

public class ConstraintSolver {
	public static final int DEFAULT_ITERATION_THRESHOLD = 100;

	public final int iterationThreshold;

	public ConstraintSolver() {
		this(DEFAULT_ITERATION_THRESHOLD);
	}

	public ConstraintSolver(int iterationThreshold) {
		this.iterationThreshold = iterationThreshold;
	}

	public void solve(@Nonnull Entity root) {
		List<Constraint> constraints = new ArrayList<>();
		List<Constrainable> constrainableList = new ArrayList<>();
		fillLists(root, constraints, constrainableList);

		Rectangle[] previous = new Rectangle[constrainableList.size()];
		for (int i = 0; i < constrainableList.size(); i++) {
			previous[i] = constrainableList.get(i).getBounds();
		}

		for (int i = 0; i < iterationThreshold; i++) {
			for (Constraint constraint : constraints) {
				constraint.apply();
			}

			Rectangle[] current = new Rectangle[constrainableList.size()];
			for (int j = 0; j < constrainableList.size(); j++) {
				current[j] = constrainableList.get(j).getBounds();
			}

			if (Arrays.equals(previous, current))
				return;
			previous = current;
		}

		throw new IllegalStateException(String.format("Couldn't solve constraints (iterations: %d).", iterationThreshold));
	}

	protected void fillLists(@Nonnull Entity entity, @Nonnull List<Constraint> constraints, @Nonnull List<Constrainable> constrainableList) {
		if (entity instanceof Constrainable) {
			Constrainable constrainable = (Constrainable)entity;
			constrainableList.add(constrainable);
			constraints.addAll(constrainable.getConstraints());
		}
		for (Entity child : entity.children.get()) {
			fillLists(child, constraints, constrainableList);
		}
	}
}