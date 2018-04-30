package pl.shockah.godwit;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.godwit.gesture.GestureManager;
import pl.shockah.godwit.platform.BackButtonService;
import pl.shockah.unicorn.SortedLinkedList;

public class InputManager extends BaseInputManager<InputManager.Processor> {
	@Nonnull protected static final Comparator<Processor> orderComparator = (o1, o2) -> -Float.compare(o1.order, o2.order);

	@Nonnull public final GestureManager gestureManager = new GestureManager();

	@Getter
	@Nonnull private final List<Processor> processors = new SortedLinkedList<>(orderComparator);

	public InputManager() {
		addProcessor(new Adapter(0f) {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.BACK) {
					Godwit.getInstance().platformServiceProvider.get(BackButtonService.class).onBackButton();
					return true;
				} else {
					return false;
				}
			}
		});
		addProcessor(new Delegated(0f, gestureManager));
	}

	protected void resetupMultiplexer() {
		int count = multiplexer.getProcessors().size;
		for (int i = 0; i < count; i++) {
			multiplexer.removeProcessor(0);
		}
		for (Processor processor : getProcessors()) {
			multiplexer.addProcessor(processor);
		}
	}

	public static abstract class Processor implements InputProcessor {
		public final float order;

		public Processor(float order) {
			this.order = order;
		}
	}

	public static abstract class Adapter extends Processor {
		public Adapter(float order) {
			super(order);
		}

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			return false;
		}
	}

	public static class Delegated extends Processor {
		@Delegate
		@Nonnull public final InputProcessor delegate;

		public Delegated(float order, @Nonnull InputProcessor delegate) {
			super(order);
			this.delegate = delegate;
		}
	}
}