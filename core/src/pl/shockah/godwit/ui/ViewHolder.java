package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ViewHolder<T> extends View {
	public final void add(@Nonnull View view) {
		add(view, null);
	}

	public void add(@Nonnull View view, @Nullable T attributes) {
		if (view.parent != null)
			throw new IllegalStateException();
		view.parent = this;
	}

	public void remove(@Nonnull View view) {
		if (view.parent != this)
			throw new IllegalStateException();
		view.parent = null;
	}
}