package pl.shockah.witness.puzzle

import groovy.transform.CompileStatic

@CompileStatic
class Puzzle {
	final Set<PuzzleNode> nodes = new LinkedHashSet<>()
	final Set<PuzzleLine> lines = new LinkedHashSet<>()
	final Set<PuzzleTile> tiles = new LinkedHashSet<>()

	Set<PuzzleStartNode> getStartNodes() {
		return nodes.findResults { it as PuzzleStartNode } as Set
	}

	Set<PuzzleEndNode> getEndNodes() {
		return nodes.findResults { it as PuzzleEndNode } as Set
	}
}