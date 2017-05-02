package pl.shockah.witness.puzzle

import groovy.transform.CompileStatic

@CompileStatic
class Solution {
	final Puzzle puzzle
	final List<PuzzleNode> nodes = new ArrayList<>()
	final float lastNodeProgress

	Solution(Puzzle puzzle, List<PuzzleNode> nodes, float lastNodeProgress = 1.0f) {
		assert lastNodeProgress >= 0f && lastNodeProgress <= 1f

		PuzzleNode previous = null
		for (PuzzleNode node : nodes) {
			if (previous != null)
				assert previous.getLineTo(node) != null
			previous = node
		}

		this.puzzle = puzzle
		this.nodes.addAll(nodes)
		this.lastNodeProgress = lastNodeProgress
	}

	boolean isValid() {
		if (!(nodes.first() instanceof PuzzleStartNode))
			return false
		if (!(nodes.last() instanceof PuzzleEndNode))
			return false

		return true
	}
}