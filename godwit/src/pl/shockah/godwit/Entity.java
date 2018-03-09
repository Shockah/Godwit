package pl.shockah.godwit;

import java.util.Comparator;
import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.fx.Animatables;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;
import pl.shockah.util.SafeList;
import pl.shockah.util.SortedLinkedList;

public class Entity implements Renderable, Animatable<Entity> {
	@Nonnull public static final Comparator<? super Entity> depthComparator = (o1, o2) -> -Float.compare(o1.getDepth(), o2.getDepth());

	@Nonnull public final SafeList<Entity> children = new SafeList<>(new SortedLinkedList<>(depthComparator));
	@Nonnull public MutableVec2 position = new MutableVec2();

	@Nullable private Entity parent;

	@Getter
	private float depth = 0f;

	@Nullable private Animatables.Properties<Entity> animatableProperties;

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

	@Nonnull public final Entity getParent() {
		if (parent == null)
			throw new IllegalStateException(String.format("Entity %s doesn't have a parent.", this));
		return parent;
	}

	public void addChild(@Nonnull Entity entity) {
		if (entity.parent != null)
			throw new IllegalStateException(String.format("Entity %s already has a parent %s.", entity, entity.parent));
		entity.parent = this;
		children.add(entity);
		callAddedToHierarchy(entity);
		entity.onAddedToParent();
	}

	protected void callAddedToHierarchy(@Nonnull Entity entity) {
		try {
			callAddedToHierarchy(entity, entity.getRenderGroup());
		} catch (IllegalStateException ignored) {
		}
	}

	protected void callAddedToHierarchy(@Nonnull Entity entity, @Nonnull RenderGroupEntity renderGroup) {
		entity.onAddedToHierarchy(renderGroup);
		for (Entity child : entity.children.get()) {
			callAddedToHierarchy(child, renderGroup);
		}
	}

	protected void callRemovedFromHierarchy(@Nonnull Entity entity) {
		try {
			callRemovedFromHierarchy(entity, entity.getRenderGroup());
		} catch (IllegalStateException ignored) {
		}
	}

	protected void callRemovedFromHierarchy(@Nonnull Entity entity, @Nonnull RenderGroupEntity renderGroup) {
		for (Entity child : entity.children.get()) {
			callRemovedFromHierarchy(child, renderGroup);
		}
		entity.onRemovedFromHierarchy(renderGroup);
	}

	public void removeFromParent() {
		if (parent == null)
			throw new IllegalStateException(String.format("Entity %s doesn't have a parent.", this));
		onRemovedFromParent();
		callRemovedFromHierarchy(this);
		parent.children.remove(this);
		parent = null;
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
	}

	public void onRemovedFromParent() {
	}

	public void onAddedToHierarchy(@Nonnull RenderGroupEntity renderGroup) {
		if (getClass() == Entity.class)
			return; // the default implementation is blank - no point rendering

		renderGroup.renderOrder.add(this);
	}

	public void onRemovedFromHierarchy(@Nonnull RenderGroupEntity renderGroup) {
		if (getClass() == Entity.class)
			return; // the default implementation is blank - no point rendering

		renderGroup.renderOrder.remove(this);
	}

	@Nonnull public final IVec2 getAbsolutePoint() {
		return getAbsolutePoint(Vec2.zero);
	}

	@Nonnull public final IVec2 getAbsolutePoint(@Nonnull IVec2 point) {
		Entity entity = this;
		while (entity != null) {
			point = entity.getTranslatedPoint(point);
			entity = entity.parent;
		}
		return point;
	}

	@Nonnull public final IVec2 getPointIn(@Nonnull Entity root) {
		return getPointIn(Vec2.zero, root);
	}

	@Nonnull public final IVec2 getPointIn(@Nonnull IVec2 point, @Nonnull Entity root) {
		Entity entity = this;
		while (entity != null) {
			point = entity.getTranslatedPoint(point);
			if (entity == root)
				return point;
			entity = entity.parent;
		}
		throw new IllegalStateException(String.format("%s is not a parent of %s.", root, this));
	}

	@Nonnull public final Entity[] getParentTree() {
		LinkedList<Entity> tree = new LinkedList<>();
		Entity entity = this;
		while (entity != null) {
			tree.addFirst(entity);
			entity = entity.parent;
		}
		return tree.toArray(new Entity[tree.size()]);
	}

	public final boolean isAddedToRenderGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof RenderGroupEntity)
				return true;
			entity = entity.parent;
		}
		return false;
	}

	@Nonnull public final RenderGroupEntity getRenderGroup() {
		Entity entity = this;
		while (entity != null) {
			if (entity instanceof RenderGroupEntity)
				return (RenderGroupEntity)entity;
			entity = entity.parent;
		}
		throw new IllegalStateException(String.format("Entity %s is not added to a RenderGroupEntity.", this));
	}

	@Nonnull public final Entity getCommonParent(@Nonnull Entity entity) {
		Entity[] parents1 = getParentTree();
		Entity[] parents2 = entity.getParentTree();

		if (parents1[0] != parents2[0])
			throw new IllegalStateException(String.format("Entities %s and %s don't have a common parent.", this, entity));
		for (int i = 1; i < Math.min(parents1.length, parents2.length); i++) {
			if (parents1[i] != parents2[i])
				return parents1[i - 1];
		}
		return parents1[Math.min(parents1.length, parents2.length) - 1];
	}

	@Nonnull protected IVec2 getTranslatedPoint(@Nonnull IVec2 point) {
		return point + position;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public Animatables.Properties<Entity> getAnimatableProperties() {
		if (animatableProperties == null)
			animatableProperties = Animatables.getAnimatableProperties(this);
		return animatableProperties;
	}
}