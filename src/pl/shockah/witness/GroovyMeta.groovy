package pl.shockah.witness

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import pl.shockah.godwit.geom.Vec2

class GroovyMeta {
	private static boolean setup = false

	static void setup() {
		if (setup)
			return

		def oldAsType

		oldAsType = List.metaClass.getMetaMethod("asType", [Class] as Class[])
		List.metaClass.asType = { Class clazz ->
			if (clazz.isAssignableFrom(Vector2.class) && delegate.size() == 2)
				return new Vector2(delegate[0], delegate[1])
			else if (clazz.isAssignableFrom(Vec2.class) && delegate.size() == 2)
				return new Vec2(delegate[0], delegate[1])
			else
				return oldAsType.invoke(delegate, [type] as Class[])
		}

		oldAsType = List.metaClass.getMetaMethod("asType", [Class] as Class[])
		List.metaClass.asType = { Class clazz ->
			if (clazz.isAssignableFrom(Vector3.class) && delegate.size() == 3)
				return new Vector3(delegate[0], delegate[1], delegate[2])
			else
				return oldAsType.invoke(delegate, [type] as Class[])
		}

		setup = true
	}
}