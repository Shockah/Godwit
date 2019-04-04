package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import pl.shockah.godwit.color.*
import pl.shockah.godwit.ease.ease
import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.inCycle
import pl.shockah.godwit.node.NodeGame
import pl.shockah.godwit.node.ShapeNode
import pl.shockah.godwit.node.Stage
import pl.shockah.godwit.node.StageLayer
import pl.shockah.godwit.size
import java.util.*
import kotlin.math.sin

class ColorSpaceGradientTest : NodeGame({ Stage(object : StageLayer() {
	val steps = 32

	var rotatingProviders = ColorSpaceProvider.providers
	var componentRotation = 0

	val provider: ColorSpaceProvider<*>
		get() = rotatingProviders.first()

	init {
		for (y in 0 until steps) {
			for (x in 0 until steps) {
				root += object : ShapeNode.Filled<Rectangle>(Rectangle(size = ImmutableVec2.ZERO)) {
					override fun updateSelf(delta: Float) {
						val screenSize = Gdx.graphics.size
						shape.size.x = screenSize.x / (steps - 1)
						shape.size.y = screenSize.y / (steps - 1)
						shape.position.x = shape.size.x * x
						shape.position.y = shape.size.y * y

						val timeF = sin(System.currentTimeMillis() * 0.002).toFloat() * 0.5f + 0.5f
						shape.topLeftColor = provider.getColorAtPoint(listOf(shape.topLeft.x / screenSize.x, shape.topLeft.y / screenSize.y, timeF), componentRotation).alpha()
						shape.topRightColor = provider.getColorAtPoint(listOf(shape.topRight.x / screenSize.x, shape.topRight.y / screenSize.y, timeF), componentRotation).alpha()
						shape.bottomLeftColor = provider.getColorAtPoint(listOf(shape.bottomLeft.x / screenSize.x, shape.bottomLeft.y / screenSize.y, timeF), componentRotation).alpha()
						shape.bottomRightColor = provider.getColorAtPoint(listOf(shape.bottomRight.x / screenSize.x, shape.bottomRight.y / screenSize.y, timeF), componentRotation).alpha()
					}
				}
			}
		}
	}

	override fun update(delta: Float) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
			Collections.rotate(rotatingProviders, 1)
		if (Gdx.input.isKeyJustPressed(Input.Keys.X))
			Collections.rotate(rotatingProviders, -1)
		if (Gdx.input.isKeyJustPressed(Input.Keys.C))
			componentRotation = (componentRotation - 1).inCycle(0, 3)
		if (Gdx.input.isKeyJustPressed(Input.Keys.V))
			componentRotation = (componentRotation + 1).inCycle(0, 3)
		super.update(delta)
	}
}) }) {
	sealed class ColorSpaceProvider<CS : IGColor<CS>> {
		companion object {
			val providers = listOf(RGBProvider, HSVProvider, HSLProvider, XYZProvider, LabProvider, LCHProvider, HSLuvProvider)
		}

		abstract fun getColorAtPoint(xF: Float, yF: Float, timeF: Float): CS

		fun getColorAtPoint(components: List<Float>, rotation: Int): CS {
			return getColorAtPoint(components[rotation % 3], components[(rotation + 1) % 3], components[(rotation + 2) % 3])
		}

//		abstract fun ease(a: CS, b: CS, f: Float): CS

		object RGBProvider : ColorSpaceProvider<RGBColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float) = RGBColor(xF, yF, timeF)
		}

		object HSVProvider : ColorSpaceProvider<HSVColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float) = HSVColor(Degrees.of(xF * 360f), yF, timeF)
		}

		object HSLProvider : ColorSpaceProvider<HSLColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float) = HSLColor(Degrees.of(xF * 360f), yF, timeF)
		}

		object XYZProvider : ColorSpaceProvider<XYZColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float) = XYZColor(XYZColor.xRange.ease(xF), XYZColor.yRange.ease(yF), XYZColor.zRange.ease(timeF))
		}

		object LabProvider : ColorSpaceProvider<LabColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float): LabColor {
				val ranges = LabColor.ReferenceRanges.D65_2
				return LabColor(ranges.l.ease(xF), ranges.a.ease(yF), ranges.b.ease(timeF))
			}
		}

		object LCHProvider : ColorSpaceProvider<LCHColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float): LCHColor {
				val ranges = LCHColor.ReferenceRanges.D65_2
				return LCHColor(ranges.l.ease(xF), ranges.c.ease(yF), Degrees.of(timeF * 360f))
			}
		}

		object HSLuvProvider : ColorSpaceProvider<HSLuvColor>() {
			override fun getColorAtPoint(xF: Float, yF: Float, timeF: Float): HSLuvColor {
				val ranges = HSLuvColor.ReferenceRanges.D65_2
				return HSLuvColor(Degrees.of(xF * 360f), yF, ranges.luv.ease(timeF))
			}
		}
	}
}