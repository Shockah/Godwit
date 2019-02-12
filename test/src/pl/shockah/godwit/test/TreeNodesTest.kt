package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import pl.shockah.godwit.geom.degrees
import pl.shockah.godwit.size
import pl.shockah.godwit.tree.GroupNode
import pl.shockah.godwit.tree.SpriteNode
import pl.shockah.godwit.tree.Stage
import pl.shockah.godwit.tree.TreeNodeGame

private object Assets {
	val cockpit = AssetDescriptor<Texture>("kenney/part-cockpit.png", Texture::class.java)
	val wing = AssetDescriptor<Texture>("kenney/part-wing.png", Texture::class.java)
	val engine = AssetDescriptor<Texture>("kenney/part-engine.png", Texture::class.java)
	val gun = AssetDescriptor<Texture>("kenney/part-gun.png", Texture::class.java)
}

class TreeNodesTest : TreeNodeGame({
	object : Stage() {
		private val manager = AssetManager()

		private val ship: GroupNode

		init {
			manager.load(Assets.cockpit)
			manager.load(Assets.wing)
			manager.load(Assets.engine)
			manager.load(Assets.gun)
			manager.finishLoading()

			ship = GroupNode().apply {
				root += this
			}

			SpriteNode(manager.get(Assets.engine)).apply {
				position.y = 44f
				ship += this
			}

			arrayOf(-1, 1).forEach {
				SpriteNode(manager.get(Assets.wing)).apply {
					scale.x = it.toFloat()
					position.x = -24f * it
					position.y = 8f
					ship += this
				}
			}

			SpriteNode(manager.get(Assets.cockpit)).apply {
				ship += this
			}
		}

		override fun update(delta: Float) {
			ship.position.set(Gdx.graphics.size * 0.5f)
			ship.rotation += (45f * delta).degrees
			super.update(delta)
		}
	}
})