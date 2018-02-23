package pl.shockah.godwit;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;
import pl.shockah.util.SafeList;
import pl.shockah.util.SortedLinkedList;

public class Entity implements Renderable, Animatable<Entity> {
	@Nonnull private static final Comparator<? super Entity> depthComparator = (o1, o2) -> -Float.compare(o1.getDepth(), o2.getDepth());
	@Nonnull public final SafeList<Entity> children = new SafeList<>(new SortedLinkedList<>(depthComparator));

	@Getter
	@Nullable private Entity parent;

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

	public void addChild(@Nonnull Entity entity) {
		if (entity.parent != null)
			throw new IllegalStateException(String.format("Entity %s already has a parent %s.", entity, entity.parent));
		entity.parent = this;
		children.add(entity);
		entity.onAddedToParent();
		callAddedToHierarchy(entity);
	}

	private static void callAddedToHierarchy(@Nonnull Entity entity) {
		entity.onAddedToHierarchy();
		entity.children.iterate(Entity::callAddedToHierarchy);
	}

	private static void callRemovedFromHierarchy(@Nonnull Entity entity) {
		entity.children.iterate(Entity::callRemovedFromHierarchy);
		entity.onRemovedFromHierarchy();
	}

	public void removeFromParent() {
		if (parent == null)
			throw new IllegalStateException(String.format("Entity %s doesn't have a parent.", this));
		callRemovedFromHierarchy(this);
		onRemovedFromParent();
		parent.children.remove(this);
		parent = null;
	}

	public void update() {
		updateSelf();
		updateFx();
		updateChildren();
	}

	public void updateSelf() {
	}

	public void updateChildren() {
		children.iterate(Entity::update);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		renderSelf(gfx, v);
		renderChildren(gfx, v);
	}

	@Override
	public final void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new ImmutableVec2(x, y));
	}

	@Override
	public final void render(@Nonnull Gfx gfx) {
		render(gfx, ImmutableVec2.zero);
	}

	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
	}

	public final void renderSelf(@Nonnull Gfx gfx, float x, float y) {
		renderSelf(gfx, new ImmutableVec2(x, y));
	}

	public final void renderSelf(@Nonnull Gfx gfx) {
		renderSelf(gfx, ImmutableVec2.zero);
	}

	public void renderChildren(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		children.iterate(entity -> entity.render(gfx, v));
	}

	public final void renderChildren(@Nonnull Gfx gfx, float x, float y) {
		renderChildren(gfx, new ImmutableVec2(x, y));
	}

	public final void renderChildren(@Nonnull Gfx gfx) {
		renderChildren(gfx, ImmutableVec2.zero);
	}

	public void onAddedToParent() {
	}

	public void onRemovedFromParent() {
	}

	public void onAddedToHierarchy() {
	}

	public void onRemovedFromHierarchy() {
	}
}