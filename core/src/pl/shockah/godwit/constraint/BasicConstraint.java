package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.ui.Unit;

public class BasicConstraint extends Constraint {
	@Nonnull
	public final Constraint.InstancedAttribute target;

	@Nonnull
	public final Constraint.InstancedAttribute source;

	@Nonnull
	public Unit length;

	public float ratio;

	public BasicConstraint(@Nonnull Constraint.InstancedAttribute target, @Nonnull Unit length) {
		this(target, target, length, 0f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent());
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem) {
		this(targetItem.getAttributes().get(attribute), sourceItem.getAttributes().get(attribute), Unit.Zero, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, @Nonnull Unit length) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), length);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit length) {
		this(targetItem.getAttributes().get(attribute), sourceItem.getAttributes().get(attribute), length, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, float ratio) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, float ratio) {
		this(targetItem.getAttributes().get(attribute), sourceItem.getAttributes().get(attribute), Unit.Zero, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute attribute, @Nonnull Unit length, float ratio) {
		return new BasicConstraint(targetItem, attribute, (Constrainable)targetItem.getParent(), length, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Unit length, float ratio) {
		this(targetItem.getAttributes().get(attribute), sourceItem.getAttributes().get(attribute), length, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		this(targetItem.getAttributes().get(targetAttribute), targetItem.getAttributes().get(sourceAttribute), Unit.Zero, 0f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		this(targetItem.getAttributes().get(targetAttribute), targetItem.getAttributes().get(sourceAttribute), length, 0f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem.getAttributes().get(targetAttribute), targetItem.getAttributes().get(sourceAttribute), Unit.Zero, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length, float ratio) {
		this(targetItem.getAttributes().get(targetAttribute), targetItem.getAttributes().get(sourceAttribute), length, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		return new BasicConstraint(targetItem.getAttributes().get(targetAttribute), ((Constrainable)targetItem.getParent()).getAttributes().get(sourceAttribute));
	}

	public BasicConstraint(@Nonnull Constraint.InstancedAttribute target, @Nonnull Constraint.InstancedAttribute source) {
		this(target, source, Unit.Zero, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length) {
		return new BasicConstraint(targetItem.getAttributes().get(targetAttribute), ((Constrainable)targetItem.getParent()).getAttributes().get(sourceAttribute), length);
	}

	public BasicConstraint(@Nonnull Constraint.InstancedAttribute target, @Nonnull Constraint.InstancedAttribute source, @Nonnull Unit length) {
		this(target, source, length, 1f);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		return new BasicConstraint(targetItem.getAttributes().get(targetAttribute), ((Constrainable)targetItem.getParent()).getAttributes().get(sourceAttribute), ratio);
	}

	public BasicConstraint(@Nonnull Constraint.InstancedAttribute target, @Nonnull Constraint.InstancedAttribute source, float ratio) {
		this(target, source, Unit.Zero, ratio);
	}

	@Nonnull
	public static <T extends Entity & Constrainable> BasicConstraint withParent(@Nonnull T targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Unit length, float ratio) {
		return new BasicConstraint(targetItem.getAttributes().get(targetAttribute), ((Constrainable)targetItem.getParent()).getAttributes().get(sourceAttribute), length, ratio);
	}

	public BasicConstraint(@Nonnull Constraint.InstancedAttribute target, @Nonnull Constraint.InstancedAttribute source, @Nonnull Unit length, float ratio) {
		this.target = target;
		this.source = source;
		this.length = length;
		this.ratio = ratio;
	}

	@Override
	public void apply() {
		float value = length.getPixels();
		if (source.attribute.isDimensional())
			value += source.get() * ratio;
		else
			value += source.get();
		target.set(value);
	}
}