package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ui.Unit;

public class BasicConstraint extends Constraint {
	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constrainable sourceItem;

	@Nonnull
	public final Attribute targetAttribute;

	@Nonnull
	public final Attribute sourceAttribute;

	@Nonnull
	public Unit length;

	public float ratio;

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem) {
		this(targetItem, attribute, sourceItem, attribute, Unit.Zero, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit value) {
		this(targetItem, attribute, sourceItem, attribute, value, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit value, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, value, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, Unit.Zero, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit value) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, value, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit value, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, value, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, Unit.Zero, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, length, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, @Nonnull Unit length, float ratio) {
		this.targetItem = targetItem;
		this.sourceItem = sourceItem;
		this.targetAttribute = targetAttribute;
		this.sourceAttribute = sourceAttribute;
		this.length = length;
		this.ratio = ratio;
	}

	@Override
	public void apply() {
		float value = length.getPixels();
		if (sourceAttribute == Attribute.Width || sourceAttribute == Attribute.Height)
			value += sourceItem.getAttribute(sourceAttribute) * ratio;
		else
			value += sourceItem.getAttribute(sourceAttribute);
		targetItem.setAttribute(targetAttribute, value);
	}
}