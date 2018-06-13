package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ui.Unit;

public class BetweenConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constrainable leadingItem;

	@Nonnull
	public final Constrainable trailingItem;

	@Nonnull
	public Unit length;

	public float ratio;

	public float bias;

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem) {
		this(targetItem, axis, leadingItem, trailingItem, Unit.Zero, 1f, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem, float ratio) {
		this(targetItem, axis, leadingItem, trailingItem, Unit.Zero, ratio, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem, float ratio, float bias) {
		this(targetItem, axis, leadingItem, trailingItem, Unit.Zero, ratio, bias);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem, @Nonnull Unit length) {
		this(targetItem, axis, leadingItem, trailingItem, length, 0f, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem, @Nonnull Unit length, float ratio) {
		this(targetItem, axis, leadingItem, trailingItem, length, ratio, 0.5f);
	}

	public BetweenConstraint(@Nonnull Constrainable targetItem, @Nonnull Axis axis, @Nonnull Constrainable leadingItem, @Nonnull Constrainable trailingItem, @Nonnull Unit length, float ratio, float bias) {
		super(axis);
		this.targetItem = targetItem;
		this.leadingItem = leadingItem;
		this.trailingItem = trailingItem;
		this.length = length;
		this.ratio = ratio;
		this.bias = bias;
	}

	@Override
	public void apply() {
		float leading = leadingItem.getAttribute(getTrailingAttribute());
		float trailing = trailingItem.getAttribute(getLeadingAttribute());

		float availableLength = trailing - leading;
		float usingLength = availableLength * ratio + length.getPixels();
		targetItem.setAttribute(getLengthAttribute(), usingLength);

		float center = leading + availableLength * bias;
		targetItem.setAttribute(getCenterAttribute(), center);
	}
}