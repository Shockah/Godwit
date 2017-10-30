package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSlice

import javax.annotation.Nonnull

@CompileStatic
abstract class ViewGroup extends View implements ViewHolder {
	@Nonnull private final List<View> views = new ArrayList<>()

	List<View> getViews() {
		return Collections.unmodifiableList(views)
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
}