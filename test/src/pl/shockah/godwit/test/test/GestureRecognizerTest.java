package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.ease.SmoothstepEasing;
import pl.shockah.godwit.fx.raw.RawFuncFx;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gesture.GestureHandler;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.LongPressGestureRecognizer;
import pl.shockah.godwit.gesture.PanGestureRecognizer;
import pl.shockah.godwit.gesture.TapGestureRecognizer;
import pl.shockah.godwit.gl.Gfx;

public class GestureRecognizerTest extends State {
	@Override
	public void onAddedToHierarchy() {
		super.onAddedToHierarchy();

		TestEntity entity = new TestEntity();
		addChild(entity);

		LongPressGestureRecognizer longPress = new LongPressGestureRecognizer(entity, 0.2f, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "long press");
		});

		TapGestureRecognizer doubleTap = new TapGestureRecognizer(entity, 2, 0.5f, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "double tap");
		});

		TapGestureRecognizer singleTap = new TapGestureRecognizer(entity, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "tap");
		});

		PanGestureRecognizer pan = new PanGestureRecognizer(entity, (recognizer, initial, current, delta) -> {
			if (recognizer.getState() == GestureRecognizer.State.Began) {
				run(new RawFuncFx(0.15f, f -> {
					entity.shape.radius = 32f + f * 16f;
				}).withMethod(SmoothstepEasing.smoothstep2));
			} else if (recognizer.getState() == GestureRecognizer.State.Ended) {
				run(new RawFuncFx(0.15f, f -> {
					entity.shape.radius = 32f + (1f - f) * 16f;
				}).withMethod(SmoothstepEasing.smoothstep2));
			}

			entity.shape.translate(delta);
			//Gdx.app.log("GestureRecognizerTest", "pan");
		});

		//pan.requireToFail(longPress);

		//doubleTap.requireToFail(pan);
		doubleTap.requireToFail(longPress);

		//singleTap.requireToFail(pan);
		singleTap.requireToFail(longPress);
		singleTap.requireToFail(doubleTap);

		pan.register();
		longPress.register();
		doubleTap.register();
		singleTap.register();
	}

	public static class TestEntity extends Shape.Filled.Entity<Circle> implements GestureHandler {
		private IVec2 lastRenderPosition = Vec2.zero;

		public TestEntity() {
			super(new Circle(64, 64, 32));
		}

		@Override
		@Nullable public Shape.Filled getGestureShape() {
			Circle shape = this.shape.copyCircle();
			shape.translate(lastRenderPosition);
			return shape;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			lastRenderPosition = v;
			super.render(gfx, v);
		}
	}
}
