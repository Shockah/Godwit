package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import pl.shockah.godwit.assetDescriptor
import pl.shockah.godwit.geom.degrees
import pl.shockah.godwit.size
import pl.shockah.godwit.tree.Node
import pl.shockah.godwit.tree.SpriteNode
import pl.shockah.godwit.tree.Stage
import pl.shockah.godwit.tree.TreeNodeGame

private object Assets {
	val cockpit = assetDescriptor<Texture>("kenney/part-cockpit.png")
	val wing = assetDescriptor<Texture>("kenney/part-wing.png")
	val engine = assetDescriptor<Texture>("kenney/part-engine.png")
	val gun = assetDescriptor<Texture>("kenney/part-gun.png")
}

class TreeNodesTest : TreeNodeGame({
	object : Stage() {
		private val manager = AssetManager()

		private val ship: Node

		init {
			manager.load(Assets.cockpit)
			manager.load(Assets.wing)
			manager.load(Assets.engine)
			manager.load(Assets.gun)
			manager.finishLoading()

			ship = SpriteNode(manager.get(Assets.cockpit)).apply {
				zLayer = 1f
				root += this
			}

			SpriteNode(manager.get(Assets.engine)).apply {
				zLayer = 0f
				position.y = 44f
				ship += this
			}

			arrayOf(-1f, 1f).forEach {
				SpriteNode(manager.get(Assets.wing)).apply {
					zLayer = 0f
					scale.x = it
					position.x = -24f * it
					position.y = 8f
					ship += this
				}
			}
		}

		override fun update(delta: Float) {
			ship.position.set(Gdx.graphics.size * 0.5f)
			ship.rotation += (45f * delta).degrees
			super.update(delta)
		}
	}
})