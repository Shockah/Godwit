package pl.shockah.godwit;

import com.badlogic.gdx.math.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.unicorn.func.Action1;
import pl.shockah.unicorn.func.Func0;

public abstract class Scroller {
	public Rectangle rectangle;

	@Nonnull
	public MutableVec2 velocity = new MutableVec2();

	public float decelerationMultiplier = 0.9f;

	@Nullable
	public Float bounceMultiplier = 0.8f;

	public boolean panning = false;

	public boolean forceCenterSmallContent = true;

	@Nonnull
	public static Scroller forCamera(@Nonnull Entity entity) {
		return new Scroller.Lambda(
				() -> entity.getCameraGroup().getCameraPosition(),
				position -> entity.getCameraGroup().setCameraPosition(position)
		);
	}

	@Nonnull
	public static Scroller forCamera(@Nonnull CameraGroup cameraGroup) {
		return new Scroller.Lambda(
				cameraGroup::getCameraPosition,
				cameraGroup::setCameraPosition
		);
	}

	public void update() {
		MutableVec2 change = new MutableVec2();

		if (panning) {
			velocity.set(0f, 0f);
		} else {
			change.x += velocity.x;
			change.y += velocity.y;

			velocity.x *= decelerationMultiplier;
			velocity.y *= decelerationMultiplier;
		}

		Vec2 current = get();
		MutableVec2 newPosition = (current.add(change)).mutableCopy();

		if (rectangle != null) {
			if (rectangle.size.x < 0) {
				newPosition.x = forceCenterSmallContent ? -rectangle.size.x * 0.5f : current.x;
			} else {
				if (bounceMultiplier == null) {
					newPosition.x = MathUtils.clamp(newPosition.x, rectangle.position.x, rectangle.position.x + rectangle.size.x);
				} else {
					if (newPosition.x > rectangle.position.x + rectangle.size.x) {
						velocity.x *= bounceMultiplier;
						if (!panning)
							newPosition.x -= (newPosition.x - (rectangle.position.x + rectangle.size.x)) * (1f - bounceMultiplier);
					} else if (newPosition.x < rectangle.position.x) {
						velocity.x *= bounceMultiplier;
						if (!panning)
							newPosition.x += (rectangle.position.x - newPosition.x) * (1f - bounceMultiplier);
					}
				}
			}

			if (rectangle.size.y < 0) {
				newPosition.y = forceCenterSmallContent ? -rectangle.size.y * 0.5f : current.y;
			} else {
				if (bounceMultiplier == null) {
					newPosition.y = MathUtils.clamp(newPosition.y, rectangle.position.y, rectangle.position.y + rectangle.size.y);
				} else {
					if (newPosition.y > rectangle.position.y + rectangle.size.y) {
						velocity.y *= bounceMultiplier;
						if (!panning)
							newPosition.y -= (newPosition.y - (rectangle.position.y + rectangle.size.y)) * (1f - bounceMultiplier);
					} else if (newPosition.y < rectangle.position.y) {
						velocity.y *= bounceMultiplier;
						if (!panning)
							newPosition.y += (rectangle.position.y - newPosition.y) * (1f - bounceMultiplier);
					}
				}
			}
		}

		if (!MathUtils.isEqual(current.x, newPosition.x, 0.001f) || !MathUtils.isEqual(current.y, newPosition.y, 0.001f))
			set(newPosition);
	}

	@Nonnull
	public abstract Vec2 get();

	public abstract void set(@Nonnull IVec2 position);

	public static class Lambda extends Scroller {
		@Nonnull
		private final Func0<Vec2> getFunc;

		@Nonnull
		private final Action1<IVec2> setFunc;

		public Lambda(@Nonnull Func0<Vec2> getFunc, @Nonnull Action1<IVec2> setFunc) {
			this.getFunc = getFunc;
			this.setFunc = setFunc;
		}

		@Override
		@Nonnull
		public Vec2 get() {
			return getFunc.call();
		}

		@Override
		public void set(@Nonnull IVec2 position) {
			setFunc.call(position);
		}
	}

	public static class Value extends Scroller {
		@Nonnull
		private Vec2 value = new Vec2();

		@Override
		@Nonnull
		public Vec2 get() {
			return value;
		}

		@Override
		public void set(@Nonnull IVec2 position) {
			value = position.asImmutable();
		}
	}
}