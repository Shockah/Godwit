package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

public class CenterConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constrainable sourceItem;

	public final float bias;

	public CenterConstraint(@Nonnull Constrainable targetItem, @Nonnull Constrainable sourceItem, @Nonnull Axis axis) {
		this(targetItem, sourceItem, axis, 0.5f);
	}

	public CenterConstraint(@Nonnull Constrainable targetItem, @Nonnull Constrainable sourceItem, @Nonnull Axis axis, float bias) {
		super(axis);
		this.targetItem = targetItem;
		this.sourceItem = sourceItem;
		this.bias = bias;
	}

	protected float getCenterValue() {
		return sourceItem.getAttribute(getLeadingAttribute()) + sourceItem.getAttribute(getLengthAttribute()) * bias;
	}

	@Override
	public void apply() {
		targetItem.setAttribute(getCenterAttribute(), getCenterValue());
	}
}