package io.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class EntityGroup<T extends Entity> extends Entity {
    protected static final Comparator<Entity> depthComparator = { Entity o1, Entity o2 ->
        Float.compare(o1.depth, o2.depth)
    } as Comparator<Entity>

    protected List<T> entities = new SortedLinkedList<T>(depthComparator as Comparator<T>)
    protected List<T> toCreate = new SortedLinkedList<T>(depthComparator as Comparator<T>)
    protected List<T> toDestroy = new SortedLinkedList<T>(depthComparator as Comparator<T>)

    @PackageScope final void markCreate(T entity) {
        if (destroyed)
            return
        toCreate << entity
    }

    @PackageScope final void markDestroy(T entity) {
        if (entity.group != this || entity.destroyed)
            return
        if (destroyed) {
            entity.destroy()
        } else {
            toDestroy << entity
        }
    }

    @Override
    protected void onDestroy() {
        for (Entity entity : entities) {
            entity.destroy()
        }
        super.onDestroy()
    }

    @Override
    protected void onUpdate() {
        super.onUpdate()

        for (T entity : toCreate) {
            if (entity.created)
                continue
            assert entity.group, "Entity ${entity} is already attached to EntityGroup ${entity.group}."
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
    protected void onRender(Gfx gfx) {
        super.onRender(gfx)
        for (Entity entity : entities) {
            entity.render(gfx)
        }
    }

    protected void preCreate(T entity) {
    }

    protected void postCreate(T entity) {
    }

    protected void onDestroy(T entity) {
    }
}