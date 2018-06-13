package pl.shockah.godwit.constraint;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Rectangle;

public interface Constrainable {
	@Nonnull
	List<Constraint> getConstraints();

	void addConstraint(@Nonnull Constraint constraint);

	void removeConstraint(@Nonnull Constraint constraint);

	float getAttribute(@Nonnull Constraint.Attribute attribute);

	void setAttribute(@Nonnull Constraint.Attribute attribute, float value);

	@Nonnull
	Constraint.InstancedAttributes getAttributes();

	@Nonnull
	Rectangle getBounds();

	void applyConstraints();
}