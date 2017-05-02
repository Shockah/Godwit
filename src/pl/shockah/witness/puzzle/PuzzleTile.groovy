package pl.shockah.witness.puzzle

import groovy.transform.CompileStatic

@CompileStatic
class PuzzleTile {
	final List<PuzzleNode> nodes = new ArrayList<>()

	PuzzleTile(List<PuzzleNode> nodes) {
		PuzzleNode previous = null
		for (PuzzleNode node : nodes) {
			if (previous != null)
				assert previous.getLineTo(node) != null
			previous = node
		}

		this.nodes.addAll(nodes)
	}

	Puzzle getPuzzle() {
		return nodes[0].puzzle
	}
}