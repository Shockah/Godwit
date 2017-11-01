package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import groovy.transform.SelfType

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
@SelfType(View)
trait ViewHolder<T> {
	void add(@Nonnull View view) {
		add(view, null)
	}

	void add(@Nonnull View view, @Nullable T attributes) {
		assert view.parent == null
		view.parent = this
	}

	void remove(@Nonnull View view) {
		assert view.parent == this
		view.parent = null
	}
}