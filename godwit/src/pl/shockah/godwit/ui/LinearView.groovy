package pl.shockah.godwit.ui

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class LinearView extends BaseLinearView<BaseLinearView.Attributes> {
	LinearView(@Nonnull Orientation orientation) {
		super(orientation)
	}
}