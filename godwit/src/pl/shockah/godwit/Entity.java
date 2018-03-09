package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.fx.Animatables;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;
import pl.shockah.util.SafeList;
import pl.shockah.util.SortedLinkedList;

public class Entity implements Renderable, Animatable<Entity> {
	@Nonnull private static final Comparator<? super Entity> depthComparator = (o1, o2) -> -Float.compare(o1.getDepth(), o2.getDepth());

	@Nonnull public final SafeList<Entity> children = new SafeList<>(new SortedLinkedList<>(depthComparator));
	@Nonnull public MutableVec2 position = new MutableVec2();

	@Getter
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

	public void addChild(@Nonnull Entity entity) {
		if (entity.parent != null)
			throw new IllegalStateException(String.format("Entity %s already has a parent %s.", entity, entity.parent));
		entity.parent = this;
		children.add(entity);
		callAddedToHierarchy(entity);
		entity.onAddedToParent();
	}

	private static void callAddedToHierarchy(@Nonnull Entity entity) {
		entity.onAddedToHierarchy();
		for (Entity child : entity.children.get()) {
			callAddedToHierarchy(child);
		}
	}

	private static void callRemovedFromHierarchy(@Nonnull Entity entity) {
		for (Entity child : entity.children.get()) {
			callRemovedFromHierarchy(child);
		}
		entity.onRemovedFromHierarchy();
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
		v = v + position;
		renderChildren(gfx, v, true);
		renderSelf(gfx, v);
		renderChildren(gfx, v, false);
	}

	@Override
	public final void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new Vec2(x, y));
	}

	@Override
	public final void render(@Nonnull Gfx gfx) {
		render(gfx, Vec2.zero);
	}

	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
	}

	public final void renderSelf(@Nonnull Gfx gfx, float x, float y) {
		renderSelf(gfx, new Vec2(x, y));
	}

	public final void renderSelf(@Nonnull Gfx gfx) {
		renderSelf(gfx, Vec2.zero);
	}

	public void renderChildren(@Nonnull Gfx gfx, @Nonnull IVec2 v, boolean behind) {
		for (Entity entity : children.get()) {
			if ((entity.depth > depth) == behind)
				entity.render(gfx, v);
		}
	}

	public final void renderChildren(@Nonnull Gfx gfx, float x, float y, boolean behind) {
		renderChildren(gfx, new Vec2(x, y), behind);
	}

	public final void renderChildren(@Nonnull Gfx gfx, boolean behind) {
		renderChildren(gfx, Vec2.zero, behind);
	}

	public void onAddedToParent() {
	}

	public void onRemovedFromParent() {
	}

	public void onAddedToHierarchy() {
	}

	public void onRemovedFromHierarchy() {
	}

	@Nonnull public final IVec2 getAbsolutePoint() {
		return getAbsolutePoint(Vec2.zero);
	}

	@Nonnull public IVec2 getAbsolutePoint(@Nonnull IVec2 point) {
		Entity entity = this;
		while (entity != null) {
			point = entity.getTranslatedPoint(point);
			entity = entity.getParent();
		}
		return point;
	}

	@Nonnull public Entity[] getParentTree() {
		List<Entity> tree = new ArrayList<>();
		Entity entity = this;
		while (entity != null) {
			if (tree.isEmpty())
				tree.add(entity);
			else
				tree.add(0, entity);
			entity = entity.getParent();
		}
		return tree.toArray(new Entity[tree.size()]);
	}

	@Nonnull public Entity getCommonParent(@Nonnull Entity entity) {
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