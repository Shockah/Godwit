package pl.shockah.witness.puzzle

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

@CompileStatic
class RectanglePuzzle extends Puzzle {
	final int width
	final int height

	RectanglePuzzle(int width, int height) {
		this(width, height, [[0, 0] as Vector2], [[width - 1, height - 1] as Vector2])
	}

	RectanglePuzzle(int width, int height, List<Vector2> startNodes, List<Vector2> endNodes) {
		this.width = width
		this.height = height
	}
}