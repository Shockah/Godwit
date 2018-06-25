package pl.shockah.godwit.ui;

import com.badlogic.gdx.Input;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.InputManager;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.ComplexShape;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gesture.GestureHandler;
import pl.shockah.godwit.gesture.TapGestureRecognizer;

public abstract class UiTextbox extends UiButton implements Focusable {
	@Nonnull
	private final TapGestureRecognizer outsideTap;

	@Nonnull
	private final InputManager.Processor textInputProcessor;

	@Nonnull
	public String text = "";

	public UiTextbox() {
		super(button -> {
			UiTextbox textbox = (UiTextbox)button;
			textbox.focus();
		});

		outsideTap = new TapGestureRecognizer(new GestureHandler() {
			@Nullable
			@Override
			public Shape.Filled getGestureShape() {
				if (UiTextbox.this.gestureShape == null)
					return null;
				return new ComplexShape.Filled<>(new Rectangle(Godwit.getInstance().gfx.getSize()), UiTextbox.this.gestureShape, ComplexShape.Operation.Difference);
			}
		}, recognizer -> blur());

		textInputProcessor = new InputManager.Adapter(0f) {
			//TODO: handle held key repeat
			private boolean backspaceDown = false;

			@Override
			public boolean keyDown(int keycode) {
				System.out.println(String.format("down %d", keycode));

				if (keycode == Input.Keys.BACKSPACE) {
					backspaceDown = true;
					text = text.substring(0, text.length() - 1);
					return true;
				} else {
					backspaceDown = false;
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				System.out.println(String.format("up %d", keycode));

				if (keycode == Input.Keys.BACKSPACE) {
					backspaceDown = false;
					return true;
				}
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				text += character;
				return true;
			}
		};
	}

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
		outsideTap.register();
		Godwit.getInstance().inputManager.addProcessor(textInputProcessor);
	}

	@Override
	public void onBlur() {
		outsideTap.unregister();
		Godwit.getInstance().inputManager.removeProcessor(textInputProcessor);
	}
}