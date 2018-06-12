package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
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

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Unit length) {
		this(targetItem, attribute, targetItem, attribute, length, 0f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, float ratio) {
		this(targetItem, attribute, targetItem, attribute, Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Unit length, float ratio) {
		this(targetItem, attribute, targetItem, attribute, length, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent());
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem) {
		this(targetItem, attribute, sourceItem, attribute, Unit.Zero, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, @Nonnull Unit length) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), length);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit length) {
		this(targetItem, attribute, sourceItem, attribute, length, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, float ratio) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, Unit.Zero, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, @Nonnull Unit length, float ratio) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), length, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit length, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, length, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, Unit.Zero, 0f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, length, 0f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, length, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		return new BasicConstraint(targetItem, targetAttribute, (Constrainable)targetItem.getParent(), sourceAttribute);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, Unit.Zero, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		return new BasicConstraint(targetItem, targetAttribute, (Constrainable)targetItem.getParent(), sourceAttribute, length);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, length, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		return new BasicConstraint(targetItem, targetAttribute, (Constrainable)targetItem.getParent(), sourceAttribute, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, Unit.Zero, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length, float ratio) {
		return new BasicConstraint(targetItem, targetAttribute, (Constrainable)targetItem.getParent(), sourceAttribute, length, ratio);
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