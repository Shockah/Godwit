package pl.shockah.godwit.ui.unused;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ViewHolder<T> extends View {
	public final void add(@Nonnull View view) {
		add(view, null);
	}

	public void add(@Nonnull View view, @Nullable T attributes) {
		if (view.parent != null)
			throw new IllegalStateException(String.format("Cannot add view %s to ViewHolder %s, as it already has a parent (%s).", view, this, view.parent));
		view.parent = this;
	}

	public void remove(@Nonnull View view) {
		if (view.parent != this)
			throw new IllegalStateException(String.format("View %s has a parent, but it is not %s (%s).", view, this, view.parent));
		view.parent = null;
	}
}