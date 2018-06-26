package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;

public class PinConstraint extends Constraint {
	public enum Sides {
		Horizontal, Vertical, All
	}

	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constrainable sourceItem;

	@Nonnull
	public final Sides sides;

	public PinConstraint(@Nonnull Constrainable targetItem, @Nonnull Constrainable sourceItem) {
		this(targetItem, sourceItem, Sides.All);
	}

	public PinConstraint(@Nonnull Constrainable targetItem, @Nonnull Constrainable sourceItem, @Nonnull Sides sides) {
		this.targetItem = targetItem;
		this.sourceItem = sourceItem;
		this.sides = sides;
	}

	@Nonnull
	public static <T extends Entity & Constrainable> PinConstraint create(@Nonnull T entity) {
		return create(entity, Sides.All);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> PinConstraint create(@Nonnull T entity, @Nonnull Sides sides) {
		return new PinConstraint(entity, (Constrainable)entity.getParent(), sides);
	}

	@Override
	public void apply() {
		if (sides != Sides.Horizontal) {
			targetItem.setAttribute(Attribute.Height, sourceItem.getAttribute(Attribute.Height));
			targetItem.setAttribute(Attribute.Top, sourceItem.getAttribute(Attribute.Top));
		}
		if (sides != Sides.Vertical) {
			targetItem.setAttribute(Attribute.Width, sourceItem.getAttribute(Attribute.Width));
			targetItem.setAttribute(Attribute.Left, sourceItem.getAttribute(Attribute.Left));
		}
	}
}