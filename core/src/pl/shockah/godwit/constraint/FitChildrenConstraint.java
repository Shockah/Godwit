package pl.shockah.godwit.constraint;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.ui.Unit;

public class FitChildrenConstraint<T extends Entity & Constrainable> extends AxisConstraint {
	@Nonnull
	public final T targetItem;

	@Nonnull
	public Unit gap;

	@Nonnull
	public Unit additionalLength;

	public FitChildrenConstraint(@Nonnull T targetItem, @Nonnull Axis axis) {
		this(targetItem, axis, Unit.Zero);
	}

	public FitChildrenConstraint(@Nonnull T targetItem, @Nonnull Axis axis, @Nonnull Unit gap) {
		this(targetItem, axis, gap, Unit.Zero);
	}

	public FitChildrenConstraint(@Nonnull T targetItem, @Nonnull Axis axis, @Nonnull Unit gap, @Nonnull Unit additionalLength) {
		super(axis);
		this.targetItem = targetItem;
		this.gap = gap;
		this.additionalLength = additionalLength;
	}

	@Override
	public void apply() {
		Attribute lengthAttribute = getLengthAttribute();

		List<Constrainable> children = StreamSupport.stream(targetItem.children.get())
				.filter(child -> child instanceof Constrainable)
				.map(child -> (Constrainable)child)
				.collect(Collectors.toList());

		float childrenLength = (float)StreamSupport.stream(children)
				.mapToDouble(child -> child.getAttribute(lengthAttribute))
				.sum();
		float gapLength = children.isEmpty() ? 0f : (children.size() - 1) * gap.getPixels();

		targetItem.setAttribute(lengthAttribute, childrenLength + gapLength + additionalLength.getPixels());
	}
}