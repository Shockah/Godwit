package pl.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import pl.shockah.godwit.animfx.Animatable
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.Renderable

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class Entity implements Renderable, Animatable {
	@Nullable protected EntityGroup<? extends Entity> group
	@PackageScope boolean created = false
	@PackageScope boolean destroyed = false

	final boolean isCreated() {
		return created
	}

	final boolean isDestroyed() {
		return destroyed
	}

	@PackageScope setCreated(boolean created) {
		this.created = created
	}

	@PackageScope setDestroyed(boolean destroyed) {
		this.destroyed = destroyed
	}

	final void create(@Nonnull Godwit godwit) {
		if (created || destroyed)
			return
		assert godwit, "No Godwit instance provided."
		create(godwit.state)
	}

	final void create(@Nonnull EntityGroup<? extends Entity> group) {
		if (created || destroyed)
			return
		assert group, "No EntityGroup provided."
		group.markCreate(this)
	}

	final void destroy() {
		if (!created || destroyed)
			return
		beforeDestroy()
		if (!group || group.destroyed) {
			destroyed = false
			onDestroy()
		} else {
			group.markDestroy(this)
		}
	}

	final void update() {
		if (!created || destroyed)
			return
		onUpdate()
		updateFx()
	}

	final void render(@Nonnull Gfx gfx) {
		render(gfx, 0f, 0f)
	}

	final void render(@Nonnull Gfx gfx, float x, float y) {
		if (!created || destroyed)
			return
		onRender(gfx, x, y)
	}

	void onCreate() {
	}

	void beforeDestroy() {
	}

	void onDestroy() {
	}

	void onUpdate() {
	}

	void onRender(@Nonnull Gfx gfx, float x, float y) {
	}
}