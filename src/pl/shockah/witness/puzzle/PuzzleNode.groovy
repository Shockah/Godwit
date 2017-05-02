package pl.shockah.witness.puzzle

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
class PuzzleNode {
	final Puzzle puzzle
	final Vector2 position

	PuzzleNode(Puzzle puzzle, Vector2 position) {
		this.puzzle = puzzle
		this.position = position
	}

	Set<PuzzleLine> getLines() {
		return puzzle.lines.findAll { it.contains(this) }
	}

	Set<PuzzleNode> getNeighbors() {
		return lines.collect { it.nodes[it.nodes[0] == this ? 1 : 0] } as Set
	}

	PuzzleLine getLineTo(PuzzleNode node) {
		return lines.find { it.nodes.contains(node) }
	}
}