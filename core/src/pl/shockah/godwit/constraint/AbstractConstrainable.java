package pl.shockah.godwit.constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.Rectangle;

public class AbstractConstrainable implements Constrainable {
	@Nonnull
	@Getter
	protected Rectangle bounds = new Rectangle(0f, 0f);

	@Nonnull
	private final List<Constraint> modifiableConstraints = new ArrayList<>();

	@Nonnull
	@Getter
	private final List<Constraint> constraints = Collections.unmodifiableList(modifiableConstraints);

	@Nonnull
	@Getter
	private final Constraint.InstancedAttributes attributes;

	@Nonnull
	public final Constraint.InstancedAttribute left;

	@Nonnull
	public final Constraint.InstancedAttribute right;

	@Nonnull
	public final Constraint.InstancedAttribute top;

	@Nonnull
	public final Constraint.InstancedAttribute bottom;

	@Nonnull
	public final Constraint.InstancedAttribute width;

	@Nonnull
	public final Constraint.InstancedAttribute height;

	@Nonnull
	public final Constraint.InstancedAttribute centerX;

	@Nonnull
	public final Constraint.InstancedAttribute centerY;

	public AbstractConstrainable() {
		attributes = new Constraint.InstancedAttributes(this);
		left = attributes.left;
		right = attributes.right;
		top = attributes.top;
		bottom = attributes.bottom;
		width = attributes.width;
		height = attributes.height;
		centerX = attributes.centerX;
		centerY = attributes.centerY;
	}

	@Override
	public void addConstraint(@Nonnull Constraint constraint) {
		modifiableConstraints.add(constraint);
	}

	@Override
	public void removeConstraint(@Nonnull Constraint constraint) {
		modifiableConstraints.remove(constraint);
	}

	@Override
	public float getAttribute(@Nonnull Constraint.Attribute attribute) {
		return getAttributeForBounds(bounds, attribute);
	}

	@Override
	public void setAttribute(@Nonnull Constraint.Attribute attribute, float value) {
		setAttributeForBounds(bounds, attribute, value);
	}

	@Override
	public void applyConstraints() {
		for (Constraint constraint : constraints) {
			constraint.apply();
		}
	}

	public static float getAttributeForBounds(@Nonnull Rectangle bounds, @Nonnull Constraint.Attribute attribute) {
		switch (attribute) {
			case Left:
				return bounds.position.x;
			case Right:
				return bounds.position.x + bounds.size.x;
			case Top:
				return bounds.position.y;
			case Bottom:
				return bounds.position.y + bounds.size.y;
			case Width:
				return bounds.size.x;
			case Height:
				return bounds.size.y;
			case CenterX:
				return bounds.position.x + bounds.size.x * 0.5f;
			case CenterY:
				return bounds.position.y + bounds.size.y * 0.5f;
			default:
				throw new IllegalArgumentException();
		}
	}

	public static void setAttributeForBounds(@Nonnull Rectangle bounds, @Nonnull Constraint.Attribute attribute, float value) {
		switch (attribute) {
			case Left:
				bounds.position.x = value;
				return;
			case Right:
				bounds.position.x = value - bounds.size.x;
				return;
			case Top:
				bounds.position.y = value;
				return;
			case Bottom:
				bounds.position.y = value - bounds.size.y;
				return;
			case Width:
				bounds.size.x = value;
				return;
			case Height:
				bounds.size.y = value;
				return;
			case CenterX:
				bounds.position.x = value - bounds.size.x * 0.5f;
				return;
			case CenterY:
				bounds.position.y = value - bounds.size.y * 0.5f;
				return;
			default:
				throw new IllegalArgumentException();
		}
	}
}