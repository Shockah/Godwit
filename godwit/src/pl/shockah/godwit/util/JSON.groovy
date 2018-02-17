package pl.shockah.godwit.util

import com.badlogic.gdx.utils.JsonValue
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class JSON {
	@Nonnull @Delegate final JsonValue jsonValue

	JSON(@Nonnull JsonValue jsonValue) {
		this.jsonValue = jsonValue
	}

	JSON getAt(int index) {
		JsonValue value = jsonValue.get(index)
		return value ? new JSON(value) : null
	}

	JSON getAt(String name) {
		JsonValue value = jsonValue.get(name)
		return value ? new JSON(value) : null
	}
}