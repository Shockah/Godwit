package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.constraint.Constrainable;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Rectangle;

public class ConstrainableRenderGroup extends RenderGroup implements Constrainable {
	@Nonnull
	public MutableVec2 size = new MutableVec2();

	@Nonnull
	private final List<Constraint> constraints = new ArrayList<>();

	@Nonnull
	private final List<Constraint> unmodifiableConstraints = Collections.unmodifiableList(constraints);

	@Override
	@Nonnull
	public List<Constraint> getConstraints() {
		return unmodifiableConstraints;
	}

	@Override
	public void addConstraint(@Nonnull Constraint constraint) {
		constraints.add(constraint);
	}

	@Override
	public void removeConstraint(@Nonnull Constraint constraint) {
		constraints.remove(constraint);
	}

	@Override
	public float getAttribute(@Nonnull Constraint.Attribute attribute) {
		switch (attribute) {
			case Left:
				return position.x;
			case Right:
				return position.x + size.x;
			case Top:
				return position.y;
			case Bottom:
				return position.y + size.y;
			case Width:
				return size.x;
			case Height:
				return size.y;
			case CenterX:
				return position.x + size.x * 0.5f;
			case CenterY:
				return position.y + size.y * 0.5f;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public void setAttribute(@Nonnull Constraint.Attribute attribute, float value) {
		switch (attribute) {
			case Left:
				position.x = value;
				return;
			case Right:
				position.x = value - size.x;
				return;
			case Top:
				position.y = value;
				return;
			case Bottom:
				position.y = value - size.y;
				return;
			case Width:
				size.x = value;
				return;
			case Height:
				size.y = value;
				return;
			case CenterX:
				position.x = value - size.x * 0.5f;
				return;
			case CenterY:
				position.y = value - size.y * 0.5f;
				return;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	@Override
	public Rectangle getBounds() {
		return new Rectangle(position, size);
	}

	@Override
	public void applyConstraints() {
		for (Constraint constraint : constraints) {
			constraint.apply();
		}
	}

	@Override
	public void update() {
		children.update();
		updateSelf();
		updateFx();
		applyConstraints();
		updateChildren();
	}
}