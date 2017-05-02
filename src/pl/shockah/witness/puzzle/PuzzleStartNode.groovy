package pl.shockah.witness.puzzle

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
class PuzzleStartNode extends PuzzleNode {
	PuzzleStartNode(Puzzle puzzle, Vector2 position) {
		super(puzzle, position)
	}
}