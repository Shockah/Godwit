package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.ui.Padding;

public class ChildrenBoundConstraint<T extends Entity & Constrainable> extends Constraint {
	public enum Sides {
		Horizontal, Vertical, All
	}

	@Nonnull
	public final T container;

	@Nonnull
	public final Sides sides;

	@Nonnull
	public Padding padding;

	public ChildrenBoundConstraint(@Nonnull T container) {
		this(container, Sides.All, new Padding());
	}

	public ChildrenBoundConstraint(@Nonnull T container, @Nonnull Padding padding) {
		this(container, Sides.All, padding);
	}

	public ChildrenBoundConstraint(@Nonnull T container, @Nonnull Sides sides) {
		this(container, sides, new Padding());
	}

	public ChildrenBoundConstraint(@Nonnull T container, @Nonnull Sides sides, @Nonnull Padding padding) {
		this.container = container;
		this.sides = sides;
		this.padding = padding;
	}

	@Override
	public void apply() {
		if (sides != Sides.Horizontal)
			applySide(AxisConstraint.Axis.Vertical);
		if (sides != Sides.Vertical)
			applySide(AxisConstraint.Axis.Horizontal);
	}

	private void applySide(@Nonnull AxisConstraint.Axis axis) {
		float max = (float)StreamSupport.stream(container.children.get())
				.filter(child -> child instanceof Constrainable)
				.map(child -> (Entity & Constrainable)child)
				.mapToDouble(child -> AxisConstraint.getVectorValue(axis, child.position.add(child.getAttribute(Attribute.Width), child.getAttribute(Attribute.Height))))
				.max().orElse(0f);
		container.setAttribute(AxisConstraint.getLengthAttribute(axis), max);
	}
}