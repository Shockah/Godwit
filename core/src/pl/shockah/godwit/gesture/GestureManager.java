package pl.shockah.godwit.gesture;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import pl.shockah.godwit.CameraGroup;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.RenderGroup;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.geom.Vec2;

public class GestureManager extends InputAdapter {
	@Nonnull public final Map<Integer, Touch> touches = new HashMap<>();
	@Nonnull public final Set<GestureRecognizer> recognizers = new LinkedHashSet<>();
	@Nonnull protected final Set<ContinuousGestureRecognizer> currentContinuousRecognizers = new LinkedHashSet<>();

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
		handle(GestureRecognizer::handleTouchDown, true, touch, point, Godwit.getInstance().getRootEntity());
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Touch touch = touches.get(pointer);
		if (touch == null || touch.isFinished())
			return false;

		Vec2 point = new Vec2(screenX, screenY);
		touch.addPoint(point);
		handle(GestureRecognizer::handleTouchDragged, false, touch, point, Godwit.getInstance().getRootEntity());
		return true;
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
		handle(GestureRecognizer::handleTouchUp, false, touch, point, Godwit.getInstance().getRootEntity());
		return true;
	}

	private void handle(@Nonnull GestureHandleMethod method, boolean checkShape, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		handle(method, checkShape, new ArrayList<>(recognizers), new HashSet<>(), touch, point, entity);
	}

	private void handle(@Nonnull GestureHandleMethod method, boolean checkShape, @Nonnull List<GestureRecognizer> recognizers, @Nonnull Set<CameraGroup> unprojected, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		List<GestureRecognizer> delayedHandlers = null;

		try {
			CameraGroup cameraGroup = entity.getCameraGroup();
			if (!unprojected.contains(cameraGroup)) {
				Vector3 unproject = cameraGroup.getCamera().unproject(new Vector3(point.x, point.y, 0f));
				point = new Vec2(unproject.x, unproject.y);
				unprojected = new HashSet<>(unprojected);
				unprojected.add(cameraGroup);
			}
		} catch (Exception ignored) {
		}

		if (entity instanceof GestureHandler) {
			GestureHandler handler = (GestureHandler)entity;
			Shape.Filled shape = handler.getGestureShape();

			if (!checkShape || (passThroughWithoutShape && shape == null) || (shape != null && shape.contains(point))) {
				for (GestureRecognizer recognizer : recognizers) {
					if (recognizer.handler == handler) {
						if (delayedHandlers == null)
							delayedHandlers = new ArrayList<>();
						delayedHandlers.add(recognizer);
					}
				}
			}
		}

		handleChildren(method, checkShape, recognizers, unprojected, touch, point, entity);

		if (delayedHandlers != null) {
			for (GestureRecognizer recognizer : delayedHandlers) {
				method.handle(recognizer, touch, point);
			}
		}
	}

	private void handleChildren(@Nonnull GestureHandleMethod method, boolean checkShape, @Nonnull List<GestureRecognizer> recognizers, @Nonnull Set<CameraGroup> unprojected, @Nonnull Touch touch, @Nonnull Vec2 point, @Nonnull Entity entity) {
		if (entity instanceof RenderGroup) {
			RenderGroup renderGroup = (RenderGroup)entity;
			ListIterator<Entity> iterator = renderGroup.renderOrder.get().listIterator(renderGroup.renderOrder.get().size());
			while (iterator.hasPrevious()) {
				handle(method, checkShape, recognizers, unprojected, touch, point, iterator.previous());
			}
		} else {
			for (Entity child : entity.children.get()) {
				handle(method, checkShape, recognizers, unprojected, touch, point, child);
			}
		}
	}

	private interface GestureHandleMethod {
		void handle(@Nonnull GestureRecognizer recognizer, @Nonnull Touch touch, @Nonnull Vec2 point);
	}
}