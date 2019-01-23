package pl.shockah.godwit.gl

import pl.shockah.godwit.gl.raw.GLCompileShaderException
import pl.shockah.godwit.gl.raw.GLShaderType

class Shader private constructor(
		private val binding: GLBinding,
		val id: Int
) : AutoCloseable {
	companion object {
		@Throws(GLCompileShaderException::class)
		operator fun invoke(binding: GLBinding, type: GLShaderType, source: CharSequence): Shader {
			val shaderId = binding.createShader(type)
			binding.shaderSource(shaderId, source)
			try {
				binding.compileShader(shaderId)
				return Shader(binding, shaderId)
			} catch (e: GLCompileShaderException) {
				binding.deleteShader(shaderId)
				throw e
			}
		}
	}

	override fun close() {
		binding.deleteShader(id)
	}
}