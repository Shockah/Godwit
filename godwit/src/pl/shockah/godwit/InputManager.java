package pl.shockah.godwit;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import pl.shockah.util.SortedLinkedList;

public class InputManager {
	@Nonnull protected static final Comparator<Processor> orderComparator = (o1, o2) -> Float.compare(o1.order, o2.order);

	@Nonnull final InputMultiplexer multiplexer = new InputMultiplexer();
	@Nonnull private final List<Processor> processors = new SortedLinkedList<>(orderComparator);

	public void addProcessor(Processor processor) {
		processors.add(processor);
		resetupMultiplexer();
	}

	public void removeProcessor(Processor processor) {
		processors.remove(processor);
		resetupMultiplexer();
	}

	protected void resetupMultiplexer() {
		int count = multiplexer.getProcessors().size;
		for (int i = 0; i < count; i++) {
			multiplexer.removeProcessor(0);
		}
		for (Processor processor : processors) {
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