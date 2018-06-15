package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Scroller;
import pl.shockah.godwit.constraint.BasicConstraint;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.constraint.PinConstraint;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.PanGestureRecognizer;
import pl.shockah.godwit.gl.Gfx;

public class UiScroll extends UiPanel {
	@Nonnull
	public final UiPanel content;

	@Nonnull
	public final Direction direction;

	@Nonnull
	protected final Scroller scroller;

	public UiScroll(@Nonnull Direction direction) {
		this.direction = direction;
		addChild(content = new UiPanel() {
			@Override
			public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				renderChildren(gfx, v);
			}
		});

		switch (direction) {
			case Vertical:
				content.addConstraint(new BasicConstraint(content, Constraint.Attribute.Width, this));
				content.addConstraint(PinConstraint.create(content, PinConstraint.Sides.Horizontal));
				break;
			case Horizontal:
				content.addConstraint(new BasicConstraint(content, Constraint.Attribute.Height, this));
				content.addConstraint(PinConstraint.create(content, PinConstraint.Sides.Vertical));
				break;
			default:
				break;
		}

		scroller = new Scroller.Lambda(
				() -> content.position.negate(),
				newValue -> content.position.set(newValue.negate())
		);
		scroller.rectangle = new Rectangle();
		scroller.forceCenterSmallContent = false;

		gestureRecognizers.add(new PanGestureRecognizer(this, (recognizer, touch, initialPoint, currentPoint, delta) -> {
			if (recognizer.getState() == GestureRecognizer.State.Began) {
				scroller.panning = true;
			}

			MutableVec2 newScrollValue = scroller.get().mutableCopy();
			if (direction != Direction.Vertical)
				newScrollValue.x -= delta.x;
			if (direction != Direction.Horizontal)
				newScrollValue.y -= delta.y;

			if (recognizer.isFinished()) {
				scroller.panning = false;

				Vec2 velocity = PanGestureRecognizer.getFlingVelocity(touch, 0.15f, 10).multiply(2.5f);
				//velocity = velocity.getNormalized().multiply(Math.min(velocity.getLength(), 400f * 60f));
				if (direction != Direction.Vertical)
					scroller.velocity.x -= velocity.x * (scroller.rectangle.size.x > 0f ? 1f : 0f);
				if (direction != Direction.Horizontal)
					scroller.velocity.y -= velocity.y * (scroller.rectangle.size.y > 0f ? 1f : 0f);
			} else {
				scroller.set(newScrollValue.multiply(
						scroller.rectangle.size.x > 0f ? 1f : 0f,
						scroller.rectangle.size.y > 0f ? 1f : 0f
				));
			}
		}));
	}

	@Override
	public void updateSelf() {
		super.updateSelf();
		scroller.rectangle.size.x = content.size.x - size.x;
		scroller.rectangle.size.y = content.size.y - size.y;
		scroller.update();
	}

	public enum Direction {
		Vertical, Horizontal, Both
	}
}