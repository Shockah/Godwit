package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL32Binding<
		State : GL11State,
		PrimitiveType : GL32PrimitiveType,
		TextureTarget : GL32TextureTarget,
		PixelCopyType : GL30PixelCopyType,
		ClientStateCapability : GL15ClientStateCapability,
		DrawBufferMode : GL30DrawBufferMode,
		PixelFormat : GL30PixelFormat
		> : GL31Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL32PrimitiveType internal constructor(
		glConstant: Int
) : GL11PrimitiveType(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL32PrimitiveType(glConstant)

		val linesAdjacency = new(0xA) // GL_LINES_ADJACENCY
		val lineStripAdjacency = new(0xB) // GL_LINE_STRIP_ADJACENCY
		val trianglesAdjacency = new(0xC) // GL_TRIANGLES_ADJACENCY
		val triangleStripAdjacency = new(0xD) // GL_TRIANGLE_STRIP_ADJACENCY
	}
}

open class GL32TextureTarget internal constructor(
		glConstant: Int
) : GL31TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL32TextureTarget(glConstant)

		val texture2DMultisample = new(0x9100) // GL_TEXTURE_2D_MULTISAMPLE
		val texture2DMultisampleArray = new(0x9102) // GL_TEXTURE_2D_MULTISAMPLE_ARRAY
	}
}