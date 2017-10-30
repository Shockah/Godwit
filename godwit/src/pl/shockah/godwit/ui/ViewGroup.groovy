package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSlice

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
abstract class ViewGroup<T extends Attributes> extends View implements ViewHolder {
	@Nonnull private final List<View> views = []
	@Nonnull protected final Map<View, T> attributes = [:]

	@Nonnull List<View> getViews() {
		return Collections.unmodifiableList(views)
	}

	@Nullable T getAttributesForView(@Nonnull View view) {
		assert view in views
		return attributes[view]
	}

	void add(@Nonnull View view) {
		ViewHolder.super.add(view)
		views.add(view)
	}

	void remove(@Nonnull View view) {
		ViewHolder.super.remove(view)
		views.remove(view)
	}

	@Override
	void onRender(@Nonnull Gfx gfx) {
		super.onRender(gfx)

		Gfx passedGfx = new GfxSlice(gfx, bounds)
		for (View view : views) {
			view.onRender(passedGfx)
		}
	}

	static abstract class Attributes {
		@Nonnull final View view

		Attributes(@Nonnull View view) {
			this.view = view
		}
	}
}