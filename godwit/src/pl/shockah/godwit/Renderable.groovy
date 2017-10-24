package pl.shockah.godwit

import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx

@CompileStatic
abstract trait Renderable {
	float depth = 0f

	float getDepth() {
		return this.depth
	}

	void setDepth(float depth) {
		this.depth = depth
	}

	abstract void onRender(Gfx gfx)
}