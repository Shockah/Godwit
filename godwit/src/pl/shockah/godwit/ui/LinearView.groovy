package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class LinearView<T extends Attributes> extends ViewGroup<T> {
	@Nonnull final View.Orientation orientation

	LinearView(@Nonnull View.Orientation orientation) {
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

			Vec2 basePosition = bounds.position + orientation.vector * offset
			Vec2 alignmentPosition = (bounds.size - view.bounds.size) * orientation.perpendicular.vector * Extensions.getLinearViewAttributes(view).alignment.vector
			view.bounds.position = basePosition + alignmentPosition

			offset += (size * orientation.vector).length
		}
	}

	static class Attributes extends ViewGroup.Attributes {
		@Nonnull View.Alignment alignment

		Attributes(@Nonnull View view, View.Alignment alignment) {
			super(view)
			this.alignment = alignment
		}
	}

	static final class Extensions {
		static Attributes getLinearViewAttributes(@Nonnull View self) {
			assert self.parent instanceof LinearView<Attributes>
			return (self.parent as LinearView<Attributes>).getAttributesForView(self)
		}
	}
}