package pl.shockah.godwit

open class NodeGameState() : GameState() {
	val nodeGroup: NodeGroup = NodeGroup()

	override fun render() {
		nodeGroup.render(Node.RenderContext())
	}
}