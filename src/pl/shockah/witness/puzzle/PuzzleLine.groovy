package pl.shockah.witness.puzzle

import groovy.transform.CompileStatic

@CompileStatic
class PuzzleLine {
	final PuzzleNode[] nodes
	final boolean traversible

	PuzzleLine(PuzzleNode nodeA, PuzzleNode nodeB, boolean traversible = true) {
		assert nodeA != null
		assert nodeB != null
		assert nodeA != nodeB
		assert nodeA.puzzle == nodeB.puzzle

		nodes = [nodeA, nodeB]
		this.traversible = traversible
	}

	Puzzle getPuzzle() {
		return nodes[0].puzzle
	}

	boolean contains(PuzzleNode node) {
		return nodes.contains(node)
	}
}