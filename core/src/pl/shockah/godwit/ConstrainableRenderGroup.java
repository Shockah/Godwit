package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.constraint.Constrainable;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;

public class ConstrainableRenderGroup extends RenderGroup implements Constrainable {
	@Nonnull
	public MutableVec2 size = new MutableVec2();

	@Nonnull
	private final List<Constraint> modifiableConstraints = new ArrayList<>();

	@Nonnull
	@Getter
	private final List<Constraint> constraints = Collections.unmodifiableList(modifiableConstraints);

	@Nonnull
	@Getter(lazy = true)
	private final Constraint.InstancedAttributes attributes = getLazyAttributes();

	@Nonnull
	private Constraint.InstancedAttributes getLazyAttributes() {
		return new Constraint.InstancedAttributes(this);
	}

	@Override
	public void addConstraint(@Nonnull Constraint constraint) {
		modifiableConstraints.add(constraint);
	}

	@Override
	public void removeConstraint(@Nonnull Constraint constraint) {
		modifiableConstraints.remove(constraint);
	}

	@Override
	public float getAttribute(@Nonnull Constraint.Attribute attribute) {
		switch (attribute) {
			case Left:
				return getAbsolutePoint().x;
			case Right:
				return getAbsolutePoint().x + size.x;
			case Top:
				return getAbsolutePoint().y;
			case Bottom:
				return getAbsolutePoint().y + size.y;
			case Width:
				return size.x;
			case Height:
				return size.y;
			case CenterX:
				return getAbsolutePoint().x + size.x * 0.5f;
			case CenterY:
				return getAbsolutePoint().y + size.y * 0.5f;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public void setAttribute(@Nonnull Constraint.Attribute attribute, float value) {
		switch (attribute) {
			case Left:
				position.x = value - getParent().getAbsolutePoint().x;
				return;
			case Right:
				position.x = value - getParent().getAbsolutePoint().x - size.x;
				return;
			case Top:
				position.y = value - getParent().getAbsolutePoint().y;
				return;
			case Bottom:
				position.y = value - getParent().getAbsolutePoint().y - size.y;
				return;
			case Width:
				size.x = value;
				return;
			case Height:
				size.y = value;
				return;
			case CenterX:
				position.x = value - getParent().getAbsolutePoint().x - size.x * 0.5f;
				return;
			case CenterY:
				position.y = value - getParent().getAbsolutePoint().y - size.y * 0.5f;
				return;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	@Override
	public Rectangle getBounds() {
		return new Rectangle(
				getAttribute(Constraint.Attribute.Left),
				getAttribute(Constraint.Attribute.Top),
				size
		);
	}

	@Override
	public void applyConstraints() {
		for (Constraint constraint : constraints) {
			constraint.apply();
		}
	}

	@Override
	public void update() {
		children.update();
		updateSelf();
		updateFx();
		applyConstraints();
		updateChildren();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.getScissors().push(getBounds());
		renderChildren(gfx, v);
		gfx.getScissors().pop();
	}
}