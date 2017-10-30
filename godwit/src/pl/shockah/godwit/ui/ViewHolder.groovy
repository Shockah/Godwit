package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import groovy.transform.SelfType

import javax.annotation.Nonnull

@CompileStatic
@SelfType(View)
trait ViewHolder {
	void add(@Nonnull View view) {
		assert view.parent == null
		view.parent = this
	}

	void remove(@Nonnull View view) {
		assert view.parent == this
		view.parent = null
	}
}