package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.fx.AbstractAnimatable;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;
import pl.shockah.unicorn.UnexpectedException;
import pl.shockah.unicorn.collection.SafeList;

public class Entity extends AbstractAnimatable implements Renderable {
	@Nonnull
	public final SafeList<Entity> children = new SafeList<>(new ArrayList<>());

	@Nonnull
	public MutableVec2 position = new MutableVec2();

	@Getter
	private Entity parent;

	@Getter
	private float depth = 0f;

	public final void setDepth(float depth) {
		Entity parent = this.parent;
		if (parent != null)
			parent.children.remove(this);
		this.depth = depth;
		if (parent != null)
			parent.children.add(this);
	}

	public final boolean hasParent() {
		return parent != null;
	}

	public final boolean hasRenderGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof RenderGroup && entity != this)
				return true;
			entity = entity.parent;
		}
		return false;
	}

	@Nonnull
	public final RenderGroup getRenderGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof RenderGroup && entity != this)
				return (RenderGroup)entity;
			entity = entity.parent;
		}
		throw new IllegalStateException(String.format("Entity %s doesn't have a RenderGroup.", this));
	}

	public final boolean hasCameraGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof RenderGroup && entity != this)
				return true;
			entity = entity.parent;
		}
		return false;
	}

	@Nonnull
	public final CameraGroup getCameraGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof CameraGroup && entity != this)
				return (CameraGroup)entity;
			entity = entity.parent;
		}
		throw new IllegalStateException(String.format("Entity %s doesn't have a CameraGroup.", this));
	}

	public void addChild(@Nonnull Entity entity) {
		if (entity.parent != null)
			throw new IllegalStateException(String.format("Entity %s already has a parent %s.", entity, entity.parent));
		entity.parent = this;
		children.add(entity);
		callDownAddedToHierarchy(this, entity);
		callAddedToHierarchy(entity);
		entity.onAddedToParent();
	}

	public void removeFromParent() {
		if (parent == null)
			throw new IllegalStateException(String.format("Entity %s doesn't have a parent.", this));
		onRemovedFromParent();
		callRemovedFromHierarchy(this);
		callDownRemovedFromHierarchy(parent, this);
		parent.children.remove(this);
		parent = null;
	}

	private static void callAddedToHierarchy(@Nonnull Entity entity) {
		entity.onAddedToHierarchy();
		for (Entity child : entity.children.get()) {
			callAddedToHierarchy(child);
		}
	}

	private static void callDownAddedToHierarchy(@Nonnull Entity context, @Nonnull Entity entity) {
		context.onAddedToHierarchy(entity);
		Entity parent = context.getParent();
		if (parent != null)
			callDownAddedToHierarchy(parent, entity);
	}

	private static void callRemovedFromHierarchy(@Nonnull Entity entity) {
		for (Entity child : entity.children.get()) {
			callRemovedFromHierarchy(child);
		}
		entity.onRemovedFromHierarchy();
	}

	private static void callDownRemovedFromHierarchy(@Nonnull Entity context, @Nonnull Entity entity) {
		context.onRemovedFromHierarchy(entity);
		Entity parent = context.getParent();
		if (parent != null)
			callDownRemovedFromHierarchy(parent, entity);
	}

	public void update() {
		children.update();
		updateSelf();
		updateFx();
		updateChildren();
	}

	public void updateSelf() {
	}

	public void updateChildren() {
		for (Entity entity : children.get()) {
			entity.update();
		}
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
	}

	@Override
	public final void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new Vec2(x, y));
	}

	@Override
	public final void render(@Nonnull Gfx gfx) {
		render(gfx, Vec2.zero);
	}

	public void onAddedToParent() {
		children.update();
	}

	public void onRemovedFromParent() {
	}

	private void handleAddToRenderGroupHierarchy() {
		try {
			if (getClass() != Entity.class) {
				RenderGroup renderGroup = getRenderGroup();
				if (!renderGroup.renderOrder.contains(this))
					getRenderGroup().renderOrder.add(this);
			}
			for (Entity entity : children.get()) {
				entity.handleAddToRenderGroupHierarchy();
			}
			for (Entity entity : children.getWaitingToAdd()) {
				entity.handleAddToRenderGroupHierarchy();
			}
		} catch (IllegalStateException ignored) {
		}
	}

	private void handleRemoveFromRenderGroupHierarchy() {
		try {
			if (getClass() != Entity.class)
				getRenderGroup().renderOrder.remove(this);
			for (Entity entity : children.get()) {
				entity.handleRemoveFromRenderGroupHierarchy();
			}
			for (Entity entity : children.getWaitingToAdd()) {
				entity.handleRemoveFromRenderGroupHierarchy();
			}
		} catch (IllegalStateException ignored) {
		}
	}

	public void onAddedToHierarchy() {
		handleAddToRenderGroupHierarchy();
	}

	public void onRemovedFromHierarchy() {
		handleRemoveFromRenderGroupHierarchy();
	}

	public void onAddedToHierarchy(@Nonnull Entity indirectChild) {
	}

	public void onRemovedFromHierarchy(@Nonnull Entity indirectChild) {
	}

	@Nonnull
	public final Vec2 getAbsolutePoint() {
		Entity[] tree = getParentTree();
		MutableVec2 point = new MutableVec2();

		for (int i = tree.length - 2; i >= 0; i--) {
			Vec2 relativePoint = tree[i].getRelativePositionOfChild(tree[i + 1]);
			point.x += relativePoint.x;
			point.y += relativePoint.y;
			if (point.y < -250f)
				throw new UnexpectedException("REEEEEEEEE");
		}
		return point.asImmutable();
	}

	@Nonnull
	public Vec2 getRelativePositionOfChild(@Nonnull Entity child) {
		try {
			if (child.getParent() == this)
				return child.position.asImmutable();
		} catch (Exception ignored) {
		}
		throw new IllegalArgumentException(String.format("%s is not a child of %s.", child, this));
	}

	@Nonnull
	public final MutableVec2 getPointInEntity(@Nonnull Entity entity) {
		MutableVec2 mutable = new MutableVec2();
		calculatePointInEntity(entity, mutable);
		return mutable;
	}

	@Nonnull
	public final MutableVec2 getPointInEntity(@Nonnull Entity entity, @Nonnull IVec2 point) {
		MutableVec2 mutable = point.mutableCopy();
		calculatePointInEntity(entity, mutable);
		return mutable;
	}

	public final void calculatePointInEntity(@Nonnull Entity entity, @Nonnull MutableVec2 point) {
		Entity current = this;
		while (current != null) {
			if (current == entity)
				return;
			current.calculateTranslatedPoint(point);
			current = current.parent;
		}
		throw new IllegalStateException(String.format("Entity %s is not in the tree of %s.", entity, this));
	}

	protected void calculateTranslatedPoint(@Nonnull MutableVec2 point) {
		point.x += position.x;
		point.y += position.y;
		if (point.y < -250f)
			throw new UnexpectedException("REEEEEEEEE");
	}

	@Nonnull
	protected Vec2 getTranslatedPoint(@Nonnull IVec2 point) {
		return point.add(position);
	}

	@Nonnull
	public final Entity[] getParentTree() {
		List<Entity> tree = new LinkedList<>();
		Entity entity = this;
		while (entity != null) {
			if (tree.isEmpty())
				tree.add(entity);
			else
				tree.add(0, entity);
			entity = entity.parent;
		}
		return tree.toArray(new Entity[tree.size()]);
	}

	@Nullable
	public static Entity getCommonParent(@Nonnull Entity[] parentTree1, @Nonnull Entity[] parentTree2, @Nonnull Entity entity1, @Nonnull Entity entity2) {
		if (parentTree1[0] != parentTree2[0])
			return null;
		for (int i = 1; i < Math.min(parentTree1.length, parentTree2.length); i++) {
			if (parentTree1[i] != parentTree2[i])
				return parentTree1[i - 1];
		}
		return parentTree1[Math.min(parentTree1.length, parentTree2.length) - 1];
	}

	@Nullable
	public final Entity getCommonParent(@Nonnull Entity entity) {
		return getCommonParent(getParentTree(), entity.getParentTree(), this, entity);
	}
}