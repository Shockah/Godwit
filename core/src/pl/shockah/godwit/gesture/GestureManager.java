package pl.shockah.godwit.gesture;

import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.RenderGroup;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.geom.Vec2;

public class GestureManager extends InputAdapter {
	@Nonnull public final Map<Integer, Touch> touches = new HashMap<>();
	@Nonnull public final Set<GestureRecognizer> recognizers = new HashSet<>();
	@Nonnull protected final Set<ContinuousGestureRecognizer> currentContinuousRecognizers = new HashSet<>();

	public void update() {
		for (GestureRecognizer recognizer : recognizers) {
			if (recognizer.getState() == GestureRecognizer.State.Ended)
				recognizer.setState(GestureRecognizer.State.Possible);
		}

		if (touches.isEmpty()) {
			boolean inProgress = false;
			for (GestureRecognizer recognizer : recognizers) {
				if (recognizer.isInProgress()) {
					inProgress = true;
					break;
				}
			}

			if (!inProgress) {
				for (GestureRecognizer recognizer : recognizers) {
					GestureRecognizer.State state = recognizer.getState();
					if (state == GestureRecognizer.State.Cancelled || state == GestureRecognizer.State.Failed)
						recognizer.setState(GestureRecognizer.State.Possible);
				}
			}
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Touch touch = new Touch(pointer);
		Vec2 point = new Vec2(screenX, screenY);
		touch.addPoint(point);
		touches.put(pointer, touch);

		Set<ContinuousGestureRecognizer> continuous = new HashSet<>(currentContinuousRecognizers);
		boolean result = handle(GestureRecognizer::handleTouchDown, continuous, touch, point, Godwit.getInstance().getRootEntity());
		for (ContinuousGestureRecognizer recognizer : continuous) {
			result |= recognizer.handleTouchDown(touch, point);
		}
		return result;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Touch touch = touches.get(pointer);
		if (touch == null || touch.isFinished())
			return false;

		Vec2 point = new Vec2(screenX, screenY);
		touch.addPoint(point);


		Set<ContinuousGestureRecognizer> continuous = new HashSet<>(currentContinuousRecognizers);
		boolean result = handle(GestureRecognizer::handleTouchDragged, continuous, touch, point, Godwit.getInstance().getRootEntity());
		for (ContinuousGestureRecognizer recognizer : continuous) {
			result |= recognizer.handleTouchDragged(touch, point);
		}
		return result;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Touch touch = touches.get(pointer);
		if (touch == null || touch.isFinished())
			return false;

		Vec2 point = new Vec2(screenX, screenY);
		touch.addPoint(point);
		touch.finish();
		touches.remove(pointer);

		Set<ContinuousGestureRecognizer> continuous = new HashSet<>(currentContinuousRecognizers);
		boolean result = handle(GestureRecognizer::handleTouchUp, continuous, touch, point, Godwit.getInstance().getRootEntity());
		for (ContinuousGestureRecognizer recognizer : continuous) {
			result |= recognizer.handleTouchUp(touch, point);
		}
		return result;
	}

	private boolean handle(@Nonnull GestureHandleMethod method, @Nonnull Set<ContinuousGestureRecognizer> continuous, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		boolean result = false;

		if (entity instanceof GestureHandler) {
			GestureHandler handler = (GestureHandler)entity;
			Shape.Filled shape = handler.getGestureShape();

			if (shape != null && shape.contains(point)) {
				for (GestureRecognizer recognizer : recognizers) {
					if (recognizer.handler == handler) {
						if (recognizer instanceof ContinuousGestureRecognizer)
							continuous.remove(recognizer);
						if (method.handle(recognizer, touch, point))
							result = true;
					}
				}
			}
		}

		if (entity instanceof RenderGroup) {
			RenderGroup renderGroup = (RenderGroup)entity;
			ListIterator<Entity> iterator = renderGroup.renderOrder.get().listIterator(renderGroup.renderOrder.get().size());
			while (iterator.hasPrevious()) {
				if (handle(method, continuous, touch, point, iterator.previous()))
					result = true;
			}
		} else {
			for (Entity child : entity.children.get()) {
				if (handle(method, continuous, touch, point, child))
					result = true;
			}
		}

		return result;
	}

	private interface GestureHandleMethod {
		boolean handle(@Nonnull GestureRecognizer recognizer, @Nonnull Touch touch, @Nonnull Vec2 point);
	}
}