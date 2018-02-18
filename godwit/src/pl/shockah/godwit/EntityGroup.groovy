package pl.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
class EntityGroup<T extends Entity> extends Entity {
	@Nonnull protected static final Comparator<Entity> depthComparator = { Entity o1, Entity o2 ->
		return Float.compare(o1.depth, o2.depth)
	} as Comparator<Entity>

	@Nonnull List<T> entities = new SortedLinkedList<>(depthComparator as Comparator<T>)
	@Nonnull protected List<T> toCreate = new SortedLinkedList<>(depthComparator as Comparator<T>)
	@Nonnull protected List<T> toDestroy = new SortedLinkedList<>(depthComparator as Comparator<T>)

	@PackageScope final void markCreate(@Nonnull T entity) {
		if (destroyed)
			return
		toCreate << entity
	}

	@PackageScope final void markDestroy(@Nonnull T entity) {
		if (entity.group != this || entity.destroyed)
			return
		if (destroyed)
			entity.destroy()
		else
			toDestroy << entity
	}

	@Override
	void onDestroy() {
		for (Entity entity : entities) {
			entity.destroy()
		}
		super.onDestroy()
	}

	@Override
	void onUpdate() {
		super.onUpdate()

		for (T entity : toCreate) {
			if (entity.created)
				continue
			assert !entity.group, "Entity ${entity} is already attached to EntityGroup ${entity.group}."
			assert !entity.created && !entity.destroyed, "Entity ${entity} is in a wrong state."
			entity.group = this
			entity.created = true
			preCreate(entity)
			entity.onCreate()
			postCreate(entity)
		}
		entities.addAll(toCreate)
		toCreate.clear()

		for (T entity : entities) {
			entity.update()
		}

		for (T entity : toDestroy) {
			if (entity.destroyed)
				continue
			assert entity.group == this, "Entity ${entity} isn't attached to EntityGroup ${this}."
			assert !entity.created, "Entity ${entity} is in a wrong state."
			entity.onDestroy()
			onDestroy(entity)
			entity.destroyed = true
			entity.group = null
		}
		entities.removeAll(toDestroy)
		toDestroy.clear()
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)
		for (Entity entity : entities) {
			entity.render(gfx, x, y)
		}
	}

	protected void preCreate(@Nonnull T entity) {
	}

	protected void postCreate(@Nonnull T entity) {
	}

	protected void onDestroy(@Nonnull T entity) {
	}
}