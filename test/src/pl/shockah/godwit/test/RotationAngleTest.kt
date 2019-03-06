package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import pl.shockah.godwit.Alignment
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.node.*
import pl.shockah.godwit.size

class RotationAngleTest : NodeGame({ Stage(object : StageLayer() {
	private val group: Node
	private val vectorLine: ShapeNode.Outline<Line>
	private val transformationLine: ShapeNode.Outline<Line>
	private val textNode: TextNode

	private var degrees = 0f.degrees

	init {
		group = Node().apply { root += this }

		Circle(radius = 32f).asOutlineNode().apply {
			val parent = this
			position.x = -54f
			group += this

			vectorLine = Line(point2 = ImmutableVec2(32f, 0f)).asOutlineNode().apply { parent += this }
		}

		Circle(radius = 32f).asOutlineNode().apply {
			val parent = this
			position.x = 54f
			group += this

			transformationLine = Line(point2 = ImmutableVec2(32f, 0f)).asOutlineNode().apply { parent += this }
		}

		textNode = TextNode(BitmapFont()).apply {
			alignment = Alignment.Centered
			group += this
		}
	}

	override fun update(delta: Float) {
		group.position.set(Gdx.graphics.size * 0.5f)
		degrees += (delta * 30f).degrees
		textNode.text = String.format("%.0f*", degrees.value)
		vectorLine.shape.point2 = MutableVec2(degrees, 32f)
		transformationLine.rotation = degrees
		super.update(delta)
	}
}) })