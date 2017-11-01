package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class BaseLinearView<T extends Attributes> extends ViewGroup<T> {
	@Nonnull final Orientation orientation
	float spacing = 0f

	BaseLinearView(@Nonnull Orientation orientation) {
		this.orientation = orientation
	}

	@Override
	void onLayout() {
		super.onLayout()

		float offset = 0f
		for (View view : views) {
			Vec2 size = bounds.size - orientation.vector * offset
			size = view.getIntrinsicSize(size)
			view.bounds.size = size

			Vec2 basePosition = orientation.vector * offset
			Vec2 alignmentPosition = (bounds.size - view.bounds.size) * orientation.perpendicular.vector * getAttributesForView(view).alignment.getNonNanVector()
			view.bounds.position = basePosition + alignmentPosition

			offset += (size * orientation.vector).length + spacing
		}
	}

	static class Attributes {
		@Nonnull Alignment alignment

		Attributes(@Nonnull Alignment alignment) {
			this.alignment = alignment
		}
	}
}