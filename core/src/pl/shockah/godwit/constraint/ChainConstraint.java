package pl.shockah.godwit.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.ui.Unit;

public class ChainConstraint extends AbstractChainConstraint {
	@Nonnull
	@Getter
	public final List<Constrainable> items;

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull Constrainable... items) {
		this(containerItem, axis, bias, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, float bias, @Nonnull List<? extends Constrainable> items) {
		this(containerItem, axis, Unit.Zero, bias, items);
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap, @Nonnull Constrainable... items) {
		this(containerItem, axis, gap, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap, @Nonnull List<? extends Constrainable> items) {
		this(containerItem, axis, gap, 0f, items);
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap, float bias, @Nonnull Constrainable... items) {
		this(containerItem, axis, gap, bias, Arrays.asList(items));
	}

	public ChainConstraint(@Nonnull Constrainable containerItem, @Nonnull Axis axis, @Nonnull Unit gap, float bias, @Nonnull List<? extends Constrainable> items) {
		super(containerItem, axis, gap, bias);
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