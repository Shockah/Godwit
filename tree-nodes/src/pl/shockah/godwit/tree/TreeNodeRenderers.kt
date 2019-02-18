package pl.shockah.godwit.tree

import pl.shockah.godwit.render.Renderers

class TreeNodeRenderers : Renderers() {
	val currentPassZLayers = mutableSetOf<Float>()
}