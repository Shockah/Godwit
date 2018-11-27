package pl.shockah.godwit

open class NodeGroup : Node() {
	protected val mutableChildren: MutableList<Node> = mutableListOf()
	val children: List<Node> = mutableChildren
	var renderOrder: RenderOrder = RenderOrder.BeforeChildren

	enum class RenderOrder {
		BeforeChildren,
		AfterChildren,
		OnlySelf,
		OnlyChildren
	}

	final override fun render(context: RenderContext) {
		if (renderOrder == RenderOrder.BeforeChildren)
			renderSelf(context)

		if (renderOrder == RenderOrder.OnlySelf)
			renderSelf(context)
		else
			renderChildren(context)

		if (renderOrder == RenderOrder.AfterChildren)
			renderSelf(context)
	}

	open fun renderSelf(context: RenderContext) {
	}

	open fun renderChildren(context: RenderContext) {
		for (child in children) {
			child.render(child.applyToContext(context))
		}
	}
}