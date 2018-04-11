package pl.shockah.godwit.gesture;

import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	public boolean passThroughWithoutShape = false;

	public void update() {
		List<GestureRecognizer> recognizers = new ArrayList<>(this.recognizers);

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
					if (recognizer.isFinished())
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
		return handle(GestureRecognizer::handleTouchDown, touch, point, Godwit.getInstance().getRootEntity());
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Touch touch = touches.get(pointer);
		if (touch == null || touch.isFinished())
			return false;

		Vec2 point = new Vec2(screenX, screenY);
		touch.addPoint(point);
		return handle(GestureRecognizer::handleTouchDragged, touch, point, Godwit.getInstance().getRootEntity());
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
		return handle(GestureRecognizer::handleTouchUp, touch, point, Godwit.getInstance().getRootEntity());
	}

	private boolean handle(@Nonnull GestureHandleMethod method, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		Set<ContinuousGestureRecognizer> continuous = new HashSet<>(currentContinuousRecognizers);
		boolean result = handle(method, new ArrayList<>(recognizers), continuous, touch, point, entity);
		for (ContinuousGestureRecognizer recognizer : continuous) {
			result |= method.handle(recognizer, touch, point);
		}
		return result;
	}

	private boolean handle(@Nonnull GestureHandleMethod method, @Nonnull List<GestureRecognizer> recognizers, @Nonnull Set<ContinuousGestureRecognizer> continuous, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		boolean result = false;

		if (entity instanceof GestureHandler) {
			GestureHandler handler = (GestureHandler)entity;
			Shape.Filled shape = handler.getGestureShape();

			if ((passThroughWithoutShape && shape == null) || (shape != null && shape.contains(point))) {
				for (GestureRecognizer recognizer : recognizers) {
					if (recognizer.handler != handler)
						continue;

					if (recognizer instanceof ContinuousGestureRecognizer)
						continuous.remove(recognizer);
					if (method.handle(recognizer, touch, point))
						result = true;
				}

				result |= handleChildren(method, recognizers, continuous, touch, point, entity);
			}
		} else {
			result = handleChildren(method, recognizers, continuous, touch, point, entity);
		}

		return result;
	}

	private boolean handleChildren(@Nonnull GestureHandleMethod method, @Nonnull List<GestureRecognizer> recognizers, @Nonnull Set<ContinuousGestureRecognizer> continuous, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		boolean result = false;

		if (entity instanceof RenderGroup) {
			RenderGroup renderGroup = (RenderGroup)entity;
			ListIterator<Entity> iterator = renderGroup.renderOrder.get().listIterator(renderGroup.renderOrder.get().size());
			while (iterator.hasPrevious()) {
				if (handle(method, recognizers, continuous, touch, point, iterator.previous()))
					result = true;
			}
		} else {
			for (Entity child : entity.children.get()) {
				if (handle(method, recognizers, continuous, touch, point, child))
					result = true;
			}
		}

		return result;
	}

	private interface GestureHandleMethod {
		boolean handle(@Nonnull GestureRecognizer recognizer, @Nonnull Touch touch, @Nonnull Vec2 point);
	}
}