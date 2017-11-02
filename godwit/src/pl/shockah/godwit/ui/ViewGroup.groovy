package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSlice

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
abstract class ViewGroup<T> extends View implements ViewHolder<T> {
	@Nonnull private final List<View> views = []
	@Nonnull protected final Map<View, T> attributes = [:]

	@Nonnull List<View> getViews() {
		return Collections.unmodifiableList(views)
	}

	@Nullable T getAttributesForView(@Nonnull View view) {
		assert view in views
		return attributes[view]
	}

	void add(@Nonnull View view, @Nullable T attributes) {
		ViewHolder.super.add(view, attributes)
		views.add(view)
		this.attributes[view] = attributes
	}

	void remove(@Nonnull View view) {
		ViewHolder.super.remove(view)
		views.remove(view)
		attributes.remove(view)
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)

		for (View view : views) {
			view.onRender(new GfxSlice(gfx, view.bounds), x, y)
		}
	}
}