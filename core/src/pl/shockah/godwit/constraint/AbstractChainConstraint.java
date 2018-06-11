package pl.shockah.godwit.constraint;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;

public abstract class AbstractChainConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable containerItem;

	@Nonnull
	public final Style style;

	public final float bias;

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias) {
		super(axis);
		this.containerItem = containerItem;
		this.style = Style.Packed;
		this.bias = bias;
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis) {
		this(containerItem, axis, Style.SpreadInside);
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Style style) {
		super(axis);
		this.containerItem = containerItem;
		this.style = style;
		bias = 0f;
	}

	@Nonnull
	protected abstract List<Constrainable> getItems();

	@Override
	public void apply() {
		List<Constrainable> items = getItems();

		float containerLength = containerItem.getAttribute(getLengthAttribute());
		float totalLength = (float)StreamSupport.stream(items)
				.mapToDouble(item -> item.getAttribute(getLengthAttribute()))
				.sum();
		float totalSeparatorLength = containerLength - totalLength;

		switch (style) {
			case Spread: {
				float separatorLength = totalSeparatorLength / (items.size() + 1);
				float currentOffset = 0f;
				for (Constrainable item : items) {
					currentOffset += separatorLength;
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute());
				}
				break;
			}
			case SpreadInside: {
				float separatorLength = totalSeparatorLength / (items.size() - 1);
				float currentOffset = 0f;
				for (Constrainable item : items) {
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute()) + separatorLength;
				}
				break;
			}
			case Packed: {
				float currentOffset = totalSeparatorLength * bias;
				for (Constrainable item : items) {
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute());
				}
				break;
			}
			default:
				throw new IllegalArgumentException();
		}
	}

	public enum Style {
		Spread, SpreadInside, Packed
	}
}