package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.ui.Unit;

public class ChildrenBoundConstraint<T extends Entity & Constrainable> extends AxisConstraint {
	@Nonnull
	public final T container;

	@Nonnull
	public Unit trailingPadding;

	public ChildrenBoundConstraint(@Nonnull T container, @Nonnull Axis axis) {
		this(container, axis, Unit.Zero);
	}

	public ChildrenBoundConstraint(@Nonnull T container, @Nonnull Axis axis, @Nonnull Unit trailingPadding) {
		super(axis);
		this.container = container;
		this.trailingPadding = trailingPadding;
	}

	@Override
	public void apply() {
		float max = (float)StreamSupport.stream(container.children.get())
				.filter(child -> child instanceof Constrainable)
				.map(child -> (Entity & Constrainable)child)
				.mapToDouble(child -> getVectorValue(child.position) + child.getAttribute(getLengthAttribute()))
				.max().orElse(0f);
		container.setAttribute(AxisConstraint.getLengthAttribute(axis), max + trailingPadding.getPixels());
	}
}