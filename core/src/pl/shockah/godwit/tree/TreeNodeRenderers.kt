package pl.shockah.godwit.tree

import pl.shockah.godwit.render.Renderers
import java.util.*

class TreeNodeRenderers : Renderers() {
	val currentPassZLayers = TreeMap<Float, MutableList<Node>>()
}