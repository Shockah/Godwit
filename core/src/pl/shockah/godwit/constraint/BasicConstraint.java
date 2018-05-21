package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;

public class BasicConstraint extends Constraint {
	@Nonnull
	public static final Value ZeroConstant = new Pixels(0f);

	@Nonnull
	public final Constrainable targetItem;

	@Nonnull
	public final Constrainable sourceItem;

	@Nonnull
	public final Attribute targetAttribute;

	@Nonnull
	public final Attribute sourceAttribute;

	@Nonnull
	public Value length;

	public float ratio;

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem) {
		this(targetItem, attribute, sourceItem, attribute, ZeroConstant, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Value value) {
		this(targetItem, attribute, sourceItem, attribute, value, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, ZeroConstant, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute attribute, @Nonnull Constrainable sourceItem, @Nonnull Value value, float ratio) {
		this(targetItem, attribute, sourceItem, attribute, value, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, ZeroConstant, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Value value) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, value, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, ZeroConstant, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Attribute sourceAttribute, @Nonnull Value value, float ratio) {
		this(targetItem, targetAttribute, targetItem, sourceAttribute, value, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, ZeroConstant, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, @Nonnull Value length) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, length, 1f);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, float ratio) {
		this(targetItem, targetAttribute, sourceItem, sourceAttribute, ZeroConstant, ratio);
	}

	public BasicConstraint(@Nonnull Constrainable targetItem, @Nonnull Attribute targetAttribute, @Nonnull Constrainable sourceItem, @Nonnull Attribute sourceAttribute, @Nonnull Value length, float ratio) {
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

	public static abstract class Value {
		private Value() {
		}

		public abstract float getPixels();
	}

	public static final class Pixels extends Value {
		public final float pixels;

		public Pixels(float pixels) {
			this.pixels = pixels;
		}

		@Override
		public float getPixels() {
			return pixels;
		}

		@Override
		public String toString() {
			return String.format("[Pixels: %.1f]", pixels);
		}
	}

	public static final class Inches extends Value {
		public final float inches;

		public Inches(float inches) {
			this.inches = inches;
		}

		@Override
		public float getPixels() {
			return inches * Godwit.getInstance().getPpi().x;
		}

		@Override
		public String toString() {
			return String.format("[Inches: %.1f]", inches);
		}
	}
}