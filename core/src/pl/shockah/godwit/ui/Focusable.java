package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;

public interface Focusable {
	@Nonnull
	State getState();

	boolean isFocus();

	void focus();

	void blur();

	void onFocus();

	void onBlur();
}