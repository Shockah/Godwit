package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Shape;

public abstract class FocusableUiControl<S extends Shape.Filled> extends UiControl<S> implements Focusable {
	@Nonnull
	@Override
	public final State getState() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof State)
				return (State)entity;
			entity = entity.getParent();
		}
		throw new IllegalStateException(String.format("Entity %s doesn't have a State parent.", this));
	}

	@Override
	public void onRemovedFromHierarchy() {
		blur();
		super.onRemovedFromHierarchy();
	}

	@Override
	public final boolean isFocus() {
		return getState().getFocus() == this;
	}

	@Override
	public final void focus() {
		getState().setFocus(this);
	}

	@Override
	public final void blur() {
		State state = getState();
		if (state.getFocus() == this)
			state.setFocus(null);
	}

	@Override
	public void onFocus() {
	}

	@Override
	public void onBlur() {
	}
}