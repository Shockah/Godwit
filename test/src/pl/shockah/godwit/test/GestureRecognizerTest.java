package pl.shockah.godwit.test;

import com.badlogic.gdx.Gdx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gesture.GestureHandler;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.LongPressGestureRecognizer;
import pl.shockah.godwit.gesture.PanGestureRecognizer;
import pl.shockah.godwit.gesture.PinchGestureRecognizer;
import pl.shockah.godwit.gesture.TapGestureRecognizer;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.unicorn.Box;

public class GestureRecognizerTest extends State implements GestureHandler {
	@Override
	public void onAddedToHierarchy() {
		super.onAddedToHierarchy();

		TestEntity entity = new TestEntity();
		game.addChild(entity);

		PanGestureRecognizer pan = new PanGestureRecognizer(entity, (recognizer, initial, current, delta) -> {
//			if (recognizer.getState() == GestureRecognizer.State.Began) {
//				run(new FuncFx(0.15f, f -> {
//					entity.shape.radius = 32f + f * 16f;
//				}).withMethod(SmoothstepEasing.smoothstep2));
//			} else if (recognizer.getState() == GestureRecognizer.State.Ended) {
//				run(new FuncFx(0.15f, f -> {
//					entity.shape.radius = 32f + (1f - f) * 16f;
//				}).withMethod(SmoothstepEasing.smoothstep2));
//			}

			entity.shape.translate(delta);
			//Gdx.app.log("GestureRecognizerTest", "pan");
		});

		LongPressGestureRecognizer longPress = new LongPressGestureRecognizer(entity, 0.2f, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "long press");
			pan.cancel();
		});

		TapGestureRecognizer doubleTap = new TapGestureRecognizer(entity, 2, 0.5f, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "double tap");
		});

		TapGestureRecognizer singleTap = new TapGestureRecognizer(entity, recognizer -> {
			Gdx.app.log("GestureRecognizerTest", "tap");
		});

		Box<Float> radius = new Box<>();
		PinchGestureRecognizer pinch = new PinchGestureRecognizer(this, (recognizer, initial, current) -> {
			if (recognizer.getState() == GestureRecognizer.State.Began) {
				radius.value = entity.shape.radius;
			}

			entity.shape.radius = radius.value * current.distance / initial.distance;
			//Gdx.app.log("GestureRecognizerTest", "pan");
		});

		//pinch.requireToFail(pan);
		//pan.requireToFail(longPress);

		//doubleTap.requireToFail(pan);
		doubleTap.requireToFail(longPress);

		//singleTap.requireToFail(pan);
		singleTap.requireToFail(longPress);
		singleTap.requireToFail(doubleTap);

		pinch.register();
		pan.register();

		longPress.register();
		doubleTap.register();
		singleTap.register();

		//pan.clone().register();
	}

	@Override
	@Nullable public Shape.Filled getGestureShape() {
		return new Rectangle(Godwit.getInstance().gfx.getSize());
	}

	public static class TestEntity extends Shape.Filled.Entity<Circle> implements GestureHandler {
		private IVec2 lastRenderPosition = Vec2.zero;

		public TestEntity() {
			super(new Circle(64, 64, 32));
		}

		@Override
		@Nullable public Shape.Filled getGestureShape() {
			Circle shape = this.shape.copy();
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
