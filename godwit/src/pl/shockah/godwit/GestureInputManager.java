package pl.shockah.godwit;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.Delegate;
import pl.shockah.util.SortedLinkedList;

public class GestureInputManager extends InputManager.Delegated {
	@Nonnull protected static final Comparator<Processor> orderComparator = (o1, o2) -> -Float.compare(o1.order, o2.order);

	protected GestureInputManager(float order) {
		super(order, new GestureDetector(new CustomGestureDetectorListener()));
	}

	@Nonnull protected List<Processor> getProcessors() {
		return ((CustomGestureDetectorListener)delegate).processors;
	}

	@Nullable public Processor getExclusive() {
		return ((CustomGestureDetectorListener)delegate).exclusive;
	}

	public void setExclusive(@Nullable Processor exclusive) {
		((CustomGestureDetectorListener)delegate).exclusive = exclusive;
	}

	public void addProcessor(Processor processor) {
		getProcessors().add(processor);
	}

	public void removeProcessor(Processor processor) {
		if (getExclusive() == processor)
			setExclusive(null);
		getProcessors().remove(processor);
	}

	private static class CustomGestureDetectorListener implements GestureDetector.GestureListener {
		@Nullable private Processor exclusive = null;
		@Nonnull private final List<Processor> processors = new SortedLinkedList<>(orderComparator);

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.touchDown(x, y, pointer, button))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.tap(x, y, count, button))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.longPress(x, y))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.fling(velocityX, velocityY, button))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.pan(x, y, deltaX, deltaY)) {
						exclusive = processor;
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			for (Processor processor : processors) {
				if (exclusive == processor) {
					if (processor.panStop(x, y, pointer, button)) {
						exclusive = null;
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.zoom(initialDistance, distance))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					if (processor.pinch(initialPointer1, initialPointer2, pointer1, pointer2)) {
						exclusive = processor;
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void pinchStop() {
			for (Processor processor : processors) {
				if (exclusive == null || exclusive == processor) {
					processor.pinchStop();
					exclusive = null;
					return;
				}
			}
		}
	}

	public static abstract class Processor implements GestureDetector.GestureListener {
		public final float order;

		public Processor(float order) {
			this.order = order;
		}
	}

	public static class Adapter extends Processor {
		public Adapter(float order) {
			super(order);
		}

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			return false;
		}

		@Override
		public void pinchStop() {
		}
	}

	public static class Delegated extends Processor {
		@Delegate
		@Nonnull public final GestureDetector.GestureListener delegate;

		public Delegated(float order, @Nonnull GestureDetector.GestureListener delegate) {
			super(order);
			this.delegate = delegate;
		}
	}
}