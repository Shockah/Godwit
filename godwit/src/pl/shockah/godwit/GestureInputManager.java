package pl.shockah.godwit;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import pl.shockah.func.Action1;
import pl.shockah.util.SortedLinkedList;

public class GestureInputManager extends BaseInputManager<GestureInputManager.Processor> {
	@Nonnull protected static final Comparator<Processor> orderComparator = (o1, o2) -> -Float.compare(o1.order, o2.order);

	@Getter(AccessLevel.PROTECTED)
	@Nonnull private final List<Processor> processors = new SortedLinkedList<>(orderComparator);

	@Getter @Setter(AccessLevel.PROTECTED)
	@Nullable protected Processor exclusive = null;

	protected GestureInputManager() {
	}

	protected void resetupMultiplexer() {
		int count = multiplexer.getProcessors().size;
		for (int i = 0; i < count; i++) {
			multiplexer.removeProcessor(0);
		}
		for (Processor processor : getProcessors()) {
			multiplexer.addProcessor(processor.detector);
		}
	}

	public void removeProcessor(Processor processor) {
		if (getExclusive() == processor)
			setExclusive(null);
		super.removeProcessor(processor);
	}

	@Nonnull private GestureDetectorListenerWrapper wrap(@Nonnull GestureDetector.GestureListener listener) {
		return new GestureDetectorListenerWrapper(listener);
	}

	private class GestureDetectorListenerWrapper implements GestureDetector.GestureListener {
		@Nonnull public final GestureDetector.GestureListener wrapped;

		private GestureDetectorListenerWrapper(@Nonnull GestureDetector.GestureListener wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.touchDown(x, y, pointer, button))
					return true;
			}
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.tap(x, y, count, button))
					return true;
			}
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.longPress(x, y))
					return true;
			}
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.fling(velocityX, velocityY, button))
					return true;
			}
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.pan(x, y, deltaX, deltaY)) {
					exclusive = (Processor)wrapped;
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			if (exclusive == wrapped) {
				if (wrapped.panStop(x, y, pointer, button)) {
					exclusive = null;
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.zoom(initialDistance, distance))
					return true;
			}
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.pinch(initialPointer1, initialPointer2, pointer1, pointer2)) {
					exclusive = (Processor)wrapped;
					return true;
				}
			}
			return false;
		}

		@Override
		public void pinchStop() {
			if (exclusive == null || exclusive == wrapped) {
				wrapped.pinchStop();
				exclusive = null;
			}
		}
	}

	public static abstract class Processor implements GestureDetector.GestureListener {
		public final float order;
		@Nonnull protected final GestureDetector detector;

		public Processor(float order) {
			this(order, null);
		}

		public Processor(float order, @Nullable Action1<GestureDetector> setupDetectorFunc) {
			this.order = order;
			detector = new GestureDetector(Godwit.getInstance().inputManager.gestureManager.wrap(this));
			if (setupDetectorFunc != null)
				setupDetectorFunc.call(detector);
		}
	}

	public static class Adapter extends Processor {
		public Adapter(float order) {
			super(order);
		}

		public Adapter(float order, @Nullable Action1<GestureDetector> setupDetectorFunc) {
			super(order, setupDetectorFunc);
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
			this(order, delegate, null);
		}

		public Delegated(float order, @Nonnull GestureDetector.GestureListener delegate, @Nullable Action1<GestureDetector> setupDetectorFunc) {
			super(order, setupDetectorFunc);
			this.delegate = delegate;
		}
	}
}