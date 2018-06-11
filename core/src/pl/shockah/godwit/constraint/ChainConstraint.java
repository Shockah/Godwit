package pl.shockah.godwit.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;

public class ChainConstraint extends AbstractChainConstraint {
	@Nonnull
	@Getter
	public final List<Constrainable> items;

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull Constrainable... items) {
		this(containerItem, axis, bias, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull List<? extends Constrainable> items) {
		super(containerItem, axis, bias);
		this.items = new ArrayList<>(items);
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
		super(containerItem, axis, style);
		this.items = new ArrayList<>(items);
	}
}