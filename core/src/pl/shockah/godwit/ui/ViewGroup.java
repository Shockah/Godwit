package pl.shockah.godwit.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSlice;

public abstract class ViewGroup<T> extends ViewHolder<T> {
	@Nonnull private final List<View> views = new ArrayList<>();
	@Nonnull protected final Map<View, T> attributes = new HashMap<>();

	@Nonnull public List<View> getViews() {
		return Collections.unmodifiableList(views);
	}

	@Nullable public T getAttributesForView(@Nonnull View view) {
		if (!views.contains(view))
			throw new IllegalStateException();
		return attributes[view];
	}

	public void add(@Nonnull View view, @Nullable T attributes) {
		super.add(view, attributes);
		views.add(view);
		this.attributes[view] = attributes;
	}

	public void remove(@Nonnull View view) {
		super.remove(view);
		views.remove(view);
		attributes.remove(view);
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);

		for (View view : views) {
			view.render(new GfxSlice(gfx, view.bounds), v);
		}
	}
}