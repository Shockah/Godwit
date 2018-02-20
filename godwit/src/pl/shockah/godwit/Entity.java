package pl.shockah.godwit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.Renderable;

public class Entity implements Renderable, Animatable<Entity> {
	@Nullable protected EntityGroup<? extends Entity> parent;

	@Getter
	private boolean created = false;

	@Getter
	private boolean destroyed = false;

	@Getter
	private float depth = 0f;

	void setCreated() {
		this.created = true;
	}

	void setDestroyed() {
		this.destroyed = true;
	}

	@SuppressWarnings("unchecked")
	public final void setDepth(float depth) {
		if (parent != null)
			parent.entities.remove(this);
		this.depth = depth;
		if (parent != null)
			((EntityGroup<Entity>)parent).entities.add(this);
	}

	public final void create(@Nonnull Godwit godwit) {
		if (created || destroyed)
			return;
		State state = godwit.getState();
		if (state == null)
			throw new NullPointerException();
		create(state);
	}

	@SuppressWarnings("unchecked")
	public final void create(@Nonnull EntityGroup<? extends Entity> parent) {
		if (created || destroyed)
			return;
		((EntityGroup<Entity>)parent).markCreate(this);
	}

	@SuppressWarnings("unchecked")
	public final void destroy() {
		if (!created || destroyed)
			return;
		beforeDestroy();
		if (parent == null || parent.isDestroyed()) {
			destroyed = false;
			onDestroy();
		} else {
			((EntityGroup<Entity>)parent).markDestroy(this);
		}
	}

	public final void update() {
		if (!created || destroyed)
			return;
		onUpdate();
		updateFx();
	}

	@Override
	public final void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!created || destroyed)
			return;
		onRender(gfx, v);
	}

	@Override
	public final void render(@Nonnull Gfx gfx, float x, float y) {
		render(gfx, new ImmutableVec2(x, y));
	}

	@Override
	public final void render(@Nonnull Gfx gfx) {
		render(gfx, ImmutableVec2.zero);
	}

	public void onCreate() {
	}

	public void beforeDestroy() {
	}

	public void onDestroy() {
	}

	public void onUpdate() {
	}

	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
	}
}