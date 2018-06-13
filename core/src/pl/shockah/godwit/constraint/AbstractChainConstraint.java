package pl.shockah.godwit.constraint;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.godwit.ui.Unit;

public abstract class AbstractChainConstraint extends AxisConstraint {
	@Nonnull
	public final Constrainable containerItem;

	@Nonnull
	public final Style style;

	@Nonnull
	public final Unit gap;

	public final float bias;

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias) {
		this(containerItem, axis, Unit.Zero, bias);
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap, float bias) {
		super(axis);
		this.containerItem = containerItem;
		this.style = Style.Packed;
		this.gap = gap;
		this.bias = bias;
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis) {
		this(containerItem, axis, Style.SpreadInside, Unit.Zero);
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap) {
		this(containerItem, axis, Style.SpreadInside, gap);
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Style style) {
		this(containerItem, axis, style, Unit.Zero);
	}

	public AbstractChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Style style, @Nonnull Unit gap) {
		super(axis);
		this.containerItem = containerItem;
		this.style = style;
		this.gap = gap;
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
		totalLength += items.isEmpty() ? 0f : (items.size() - 1) * gap.getPixels();
		float totalSeparatorLength = containerLength - totalLength;

		float currentOffset = containerItem.getAttribute(getLeadingAttribute());
		switch (style) {
			case Spread: {
				float separatorLength = totalSeparatorLength / (items.size() + 1);
				for (Constrainable item : items) {
					currentOffset += separatorLength;
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute()) + gap.getPixels();
				}
				break;
			}
			case SpreadInside: {
				float separatorLength = totalSeparatorLength / (items.size() - 1);
				for (Constrainable item : items) {
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute()) + separatorLength + gap.getPixels();
				}
				break;
			}
			case Packed: {
				currentOffset += totalSeparatorLength * bias;
				for (Constrainable item : items) {
					item.setAttribute(getLeadingAttribute(), currentOffset);
					currentOffset += item.getAttribute(getLengthAttribute()) + gap.getPixels();
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