package pl.shockah.godwit.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;

public class ChainConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable containerItem;

	@Nonnull
	public final Style style;

	public final float bias;

	@Nonnull
	public final List<Constrainable> items;

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull Constrainable... items) {
		this(containerItem, axis, bias, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull List<? extends Constrainable> items) {
		super(axis);
		this.containerItem = containerItem;
		this.style = Style.Packed;
		this.items = new ArrayList<>(items);
		this.bias = bias;
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Constrainable... items) {
		this(containerItem, axis, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull List<? extends Constrainable> items) {
		this(containerItem, axis, Style.SpreadInside, items);
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Style style, @Nonnull Constrainable... items) {
		this(containerItem, axis, style, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Style style, @Nonnull List<? extends Constrainable> items) {
		super(axis);
		this.containerItem = containerItem;
		this.style = style;
		this.items = new ArrayList<>(items);
		bias = 0f;
	}

	@Override
	public void apply() {
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