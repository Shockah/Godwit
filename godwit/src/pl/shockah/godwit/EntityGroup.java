package pl.shockah.godwit;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.util.SortedLinkedList;

public class EntityGroup<T extends Entity> extends Entity {
	@Nonnull protected static final Comparator<? super Entity> depthComparator = (o1, o2) -> Float.compare(o1.getDepth(), o2.getDepth());

	@Nonnull public final List<T> entities = new SortedLinkedList<>(depthComparator);
	@Nonnull protected final List<T> toCreate = new SortedLinkedList<>(depthComparator);
	@Nonnull protected final List<T> toDestroy = new SortedLinkedList<>(depthComparator);

	final void markCreate(@Nonnull T entity) {
		if (isDestroyed())
			return;
		toCreate.add(entity);
	}

	final void markDestroy(@Nonnull T entity) {
		if (entity.parent != this || entity.isDestroyed())
			return;
		if (isDestroyed())
			entity.destroy();
		else
			toDestroy.add(entity);
	}

	@Override
	public void onDestroy() {
		for (T entity : entities) {
			entity.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		for (T entity : toCreate) {
			if (entity.isCreated())
				continue;
			if (entity.parent != null)
				throw new IllegalStateException(String.format("Entity %s is already attached to EntityGroup %s.", entity, entity.parent));
			if (entity.isDestroyed())
				throw new IllegalStateException(String.format("Entity %s is in a wrong state.", entity));
			entity.parent = this;
			entity.setCreated();
			preCreate(entity);
			entity.onCreate();
			postCreate(entity);
		}
		entities.addAll(toCreate);
		toCreate.clear();

		for (T entity : entities) {
			entity.update();
		}

		for (T entity : toDestroy) {
			if (entity.isDestroyed())
				continue;
			if (entity.parent != this)
				throw new IllegalStateException(String.format("Entity %s isn't attached to EntityGroup %s.", entity, this));
			if (!entity.isCreated())
				throw new IllegalStateException(String.format("Entity %s is in a wrong state.", entity));
			entity.onDestroy();
			onDestroy(entity);
			entity.setDestroyed();
			entity.parent = null;
		}
		entities.removeAll(toDestroy);
		toDestroy.clear();
	}

	@Override
	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.onRender(gfx, v);
		for (T entity : entities) {
			entity.render(gfx, v);
		}
	}

	protected void preCreate(@Nonnull T entity) {
	}

	protected void postCreate(@Nonnull T entity) {
	}

	protected void onDestroy(@Nonnull T entity) {
	}
}