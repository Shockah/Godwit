package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Entity;
import pl.shockah.godwit.Scroller;
import pl.shockah.godwit.constraint.AxisConstraint;
import pl.shockah.godwit.constraint.BasicConstraint;
import pl.shockah.godwit.constraint.ChainConstraint;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.PanGestureRecognizer;

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
			@Nonnull
			@Override
			public Vec2 getRelativePositionOfChild(@Nonnull Entity child) {
				try {
					if (child.getParent() == this)
						return child.position.add(position);
				} catch (Exception ignored) {
				}
				throw new IllegalArgumentException(String.format("%s is not a child of %s.", child, this));
			}

			@Override
			public float getAttribute(@Nonnull Constraint.Attribute attribute) {
				float baseValue = super.getAttribute(attribute);
				if (attribute.isPositional()) {
					if (attribute.isHorizontal())
						baseValue -= position.x;
					else
						baseValue -= position.y;
				}
				return baseValue;
			}
		});

		switch (direction) {
			case Vertical:
				content.addConstraint(new BasicConstraint(content, Constraint.Attribute.Width, this));
				content.addConstraint(new ChainConstraint(this, AxisConstraint.Axis.Horizontal));
				break;
			case Horizontal:
				content.addConstraint(new BasicConstraint(content, Constraint.Attribute.Height, this));
				content.addConstraint(new ChainConstraint(this, AxisConstraint.Axis.Vertical));
				break;
			default:
				break;
		}

		scroller = new Scroller.Lambda(
				() -> content.position.negate(),
				newValue -> content.position.set(newValue.negate())
		);
		scroller.rectangle = new Rectangle();

		gestureRecognizers.add(new PanGestureRecognizer(this, (recognizer, touch, initialPoint, currentPoint, delta) -> {
			if (recognizer.getState() == GestureRecognizer.State.Began) {
				scroller.panning = true;
			}

			MutableVec2 newScrollValue = scroller.get().mutableCopy();
			if (direction != Direction.Vertical)
				newScrollValue.x -= delta.x;
			if (direction != Direction.Horizontal)
				newScrollValue.y -= delta.y;
			scroller.set(newScrollValue);

			if (recognizer.isFinished()) {
				scroller.panning = false;

				Vec2 velocity = PanGestureRecognizer.getFlingVelocity(touch, 0.15f, 10).multiply(2.5f);
				//velocity = velocity.getNormalized().multiply(Math.min(velocity.getLength(), 400f * 60f));
				if (direction != Direction.Vertical)
					scroller.velocity.x -= velocity.x;
				if (direction != Direction.Horizontal)
					scroller.velocity.y -= velocity.y;
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