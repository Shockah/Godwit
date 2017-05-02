package pl.shockah.witness.puzzle

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
class PuzzleEndNode extends PuzzleNode {
	PuzzleEndNode(Puzzle puzzle, Vector2 position) {
		super(puzzle, position)
	}
}