package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Delegate;
import pl.shockah.func.Action1;
import pl.shockah.util.SortedLinkedList;

public class GestureInputManager extends BaseInputManager<GestureInputManager.Processor> {
	@Nonnull protected static final Comparator<Processor> orderComparator = (o1, o2) -> -Float.compare(o1.order, o2.order);

	@Getter(AccessLevel.PROTECTED)
	@Nonnull private final List<Processor> processors = new SortedLinkedList<>(orderComparator);

	@Getter
	@Nullable private Processor exclusive = null;

	protected GestureInputManager() {
	}

	protected void resetupMultiplexer() {
		int count = multiplexer.getProcessors().size;
		for (int i = 0; i < count; i++) {
			multiplexer.removeProcessor(0);
		}

		if (exclusive == null) {
			for (Processor processor : getProcessors()) {
				multiplexer.addProcessor(processor.detector);
			}
		} else {
			multiplexer.addProcessor(exclusive.detector);
		}
	}

	public void removeProcessor(Processor processor) {
		if (getExclusive() == processor)
			Gdx.app.postRunnable(() -> setExclusive(null));
		super.removeProcessor(processor);
	}

	public void setExclusive(@Nullable Processor exclusive) {
		this.exclusive = exclusive;
		if (exclusive != null)
			cancelLongPressTasks(exclusive);
		resetupMultiplexer();
	}

	private void cancelLongPressTasks() {
		cancelLongPressTasks(null);
	}

	private void cancelLongPressTasks(@Nullable Processor excluded) {
		for (Processor processor : getProcessors()) {
			if (processor != exclusive)
				processor.cancelLongPressTask();
		}
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
				if (wrapped.tap(x, y, count, button)) {
					cancelLongPressTasks((Processor)wrapped);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.longPress(x, y)) {
					cancelLongPressTasks((Processor)wrapped);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.fling(velocityX, velocityY, button)) {
					cancelLongPressTasks((Processor)wrapped);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.pan(x, y, deltaX, deltaY)) {
					Gdx.app.postRunnable(() -> setExclusive((Processor)wrapped));
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			if (exclusive == wrapped) {
				if (wrapped.panStop(x, y, pointer, button)) {
					Gdx.app.postRunnable(() -> setExclusive(null));
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.zoom(initialDistance, distance)) {
					cancelLongPressTasks((Processor)wrapped);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
			if (exclusive == null || exclusive == wrapped) {
				if (wrapped.pinch(initialPointer1, initialPointer2, pointer1, pointer2)) {
					Gdx.app.postRunnable(() -> setExclusive((Processor)wrapped));
					return true;
				}
			}
			return false;
		}

		@Override
		public void pinchStop() {
			if (exclusive == null || exclusive == wrapped) {
				wrapped.pinchStop();
				Gdx.app.postRunnable(() -> setExclusive(null));
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

		private void cancelLongPressTask() {
			try {
				Field longPressTaskField = GestureDetector.class.getDeclaredField("longPressTask");
				longPressTaskField.setAccessible(true);
				((Timer.Task)longPressTaskField.get(detector)).cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void resetToPanning() {
			Gdx.app.postRunnable(() -> {
				try {
					Field longPressFiredField = GestureDetector.class.getDeclaredField("longPressFired");
					longPressFiredField.setAccessible(true);
					longPressFiredField.set(detector, false);

					Field inTapSquareField = GestureDetector.class.getDeclaredField("inTapSquare");
					inTapSquareField.setAccessible(true);
					inTapSquareField.set(detector, false);

					cancelLongPressTask();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
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