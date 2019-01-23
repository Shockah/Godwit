package pl.shockah.godwit.gl

import pl.shockah.godwit.gl.raw.GLCompileShaderException
import pl.shockah.godwit.gl.raw.GLLinkShaderProgramException

class ShaderProgram private constructor(
		private val binding: GLBinding,
		val id: Int
) : AutoCloseable {
	companion object {
		@Throws(GLLinkShaderProgramException::class)
		operator fun invoke(binding: GLBinding, vararg shaders: Shader): ShaderProgram {
			val programId = binding.createProgram()
			shaders.forEach { binding.attachShader(programId, it.id) }
			binding.linkProgram(programId)
			try {
				binding.linkProgram(programId)
				return ShaderProgram(binding, programId)
			} catch (e: GLCompileShaderException) {
				binding.deleteProgram(programId)
				throw e
			}
		}
	}

	override fun close() {
		binding.deleteProgram(id)
	}
}