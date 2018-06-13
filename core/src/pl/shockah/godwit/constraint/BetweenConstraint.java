package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ui.Unit;

public class BetweenConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constraint.InstancedAttribute leadingAttribute;

	@Nonnull
	public final Constraint.InstancedAttribute trailingAttribute;

	@Nonnull
	public Unit length;

	public float ratio;

	public float bias;

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute) {
		this(targetItem, axis, leadingAttribute, trailingAttribute, Unit.Zero, 1f, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute, float ratio) {
		this(targetItem, axis, leadingAttribute, trailingAttribute, Unit.Zero, ratio, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute, float ratio, float bias) {
		this(targetItem, axis, leadingAttribute, trailingAttribute, Unit.Zero, ratio, bias);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute, @Nonnull Unit length) {
		this(targetItem, axis, leadingAttribute, trailingAttribute, length, 0f, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute, @Nonnull Unit length, float ratio) {
		this(targetItem, axis, leadingAttribute, trailingAttribute, length, ratio, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constraint.InstancedAttribute leadingAttribute, @Nonnull Constraint.InstancedAttribute trailingAttribute, @Nonnull Unit length, float ratio, float bias) {
		super(axis);
		if (!axis.matches(leadingAttribute.attribute) || !axis.matches(trailingAttribute.attribute))
			throw new IllegalArgumentException();

		this.targetItem = targetItem;
		this.leadingAttribute = leadingAttribute;
		this.trailingAttribute = trailingAttribute;
		this.length = length;
		this.ratio = ratio;
		this.bias = bias;
	}

	@Override
	public void apply() {
		float leading = leadingAttribute.get();
		float trailing = trailingAttribute.get();

		float availableLength = trailing - leading;
		float usingLength = availableLength * ratio + length.getPixels();
		targetItem.setAttribute(getLengthAttribute(), usingLength);

		float center = leading + availableLength * bias;
		targetItem.setAttribute(getCenterAttribute(), center);
	}
}