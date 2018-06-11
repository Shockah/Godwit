package pl.shockah.godwit.constraint;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.shockah.godwit.Entity;

public class ChainChildrenConstraint<T extends Entity & Constrainable> extends AbstractChainConstraint {
	public ChainChildrenConstraint(@Nonnull T containerItem, @Nonnull Axis axis, float bias) {
		super(containerItem, axis, bias);
	}

	public ChainChildrenConstraint(@Nonnull T containerItem, @Nonnull Axis axis) {
		this(containerItem, axis, Style.SpreadInside);
	}

	public ChainChildrenConstraint(@Nonnull T containerItem, @Nonnull Axis axis, @Nonnull Style style) {
		super(containerItem, axis, style);
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	protected List<Constrainable> getItems() {
		return StreamSupport.stream(((T)containerItem).children.get())
				.filter(child -> child instanceof Constrainable)
				.map(child -> (Constrainable)child)
				.collect(Collectors.toList());
	}
}