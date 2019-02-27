package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.compare
import pl.shockah.godwit.render.Renderers
import java.util.*
import kotlin.properties.Delegates

class TreeNodeRenderers : Renderers() {
	val transformationMatrixCache = TransformationMatrixCache()

	var currentViewport: Viewport by Delegates.notNull()
		internal set

	val zOrderedNodes = TreeSet<Triple<Float, Node, Matrix4>> { (a, n1, _), (b, n2, _) ->
		if (a < b)
			return@TreeSet -1
		else if (a > b)
			return@TreeSet 1
		else
			return@TreeSet System.identityHashCode(n1) compare System.identityHashCode(n2)
	}

	inner class TransformationMatrixCache {
		val cache = mutableMapOf<Viewport, MutableMap<Node, Matrix4>>()

		operator fun get(node: Node): Matrix4? {
			return this[currentViewport, node]
		}

		operator fun get(viewport: Viewport, node: Node): Matrix4? {
			return cache[viewport]?.get(node)
		}

		operator fun set(node: Node, matrix: Matrix4) {
			this[currentViewport, node] = matrix
		}

		operator fun set(viewport: Viewport, node: Node, matrix: Matrix4) {
			cache.getOrPut(viewport) { mutableMapOf() }[node] = matrix
		}

		fun clear() {
			cache.clear()
		}
	}
}