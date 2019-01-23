package pl.shockah.godwit.gl

import java.nio.*

@ExperimentalUnsignedTypes
interface GL11Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL11TextureTarget,
		PixelCopyType : GL11PixelCopyType,
		ClientStateCapability : GL11ClientStateCapability,
		DrawBufferMode : GL11DrawBufferMode,
		PixelFormat : GL11PixelFormat
		> : GLBinding {
	fun glEnable(target: State)

	fun glDisable(target: State)

	fun glAccum(operation: GLAccumulationOperation, value: Float)

	fun glAlphaFunc(alphaFunction: GLComparisonFunction, referenceValue: Float)

	fun glAreTexturesResident(textures: IntBuffer, residences: ByteBuffer)

	fun glAreTexturesResident(texture: Int, residences: ByteBuffer)

	fun glArrayElement(i: Int)

	fun glBegin(primitiveType: PrimitiveType)

	fun glBindTexture(target: TextureTarget, textureId: Int)

	fun glBitmap(width: Int, height: Int, xOrigin: Float, yOrigin: Float, xIncrement: Float, yIncrement: Float, data: ByteBuffer?)

	fun glBitmap(width: Int, height: Int, xOrigin: Float, yOrigin: Float, xIncrement: Float, yIncrement: Float, dataPointer: Long?)

	fun glBlendFunc(sourceFactor: GLBlendFactor, destinationFactor: GLBlendFactor)

	fun glCallList(listId: Int)

	fun glCallLists(type: GLType, lists: ByteBuffer)

	fun glCallLists(lists: ByteBuffer)

	fun glCallLists(lists: ShortBuffer)

	fun glCallLists(lists: IntBuffer)

	fun glClear(mask: GLClearMask)

	fun glClearAccum(red: Float, green: Float, blue: Float, alpha: Float)

	fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float)

	fun glClearDepth(depth: Double)

	fun glClearIndex(index: Float)

	fun glClearStencil(value: Int)

	fun glClipPlane(plane: Int, equation: DoubleBuffer)

	fun glColor3b(red: Byte, green: Byte, blue: Byte)

	fun glColor3s(red: Short, green: Short, blue: Short)

	fun glColor3i(red: Int, green: Int, blue: Int)

	fun glColor3f(red: Float, green: Float, blue: Float)

	fun glColor3d(red: Double, green: Double, blue: Double)

	fun glColor3ub(red: UByte, green: UByte, blue: UByte)

	fun glColor3us(red: UShort, green: UShort, blue: UShort)

	fun glColor3ui(red: UInt, green: UInt, blue: UInt)

	fun glColor3bv(values: ByteBuffer)

	fun glColor3sv(values: ShortBuffer)

	fun glColor3iv(values: IntBuffer)

	fun glColor3fv(values: FloatBuffer)

	fun glColor3dv(values: DoubleBuffer)

	fun glColor3ubv(values: ByteBuffer)

	fun glColor3usv(values: ShortBuffer)

	fun glColor3uiv(values: IntBuffer)

	fun glColor4b(red: Byte, green: Byte, blue: Byte, alpha: Byte)

	fun glColor4s(red: Short, green: Short, blue: Short, alpha: Short)

	fun glColor4i(red: Int, green: Int, blue: Int, alpha: Int)

	fun glColor4f(red: Float, green: Float, blue: Float, alpha: Float)

	fun glColor4d(red: Double, green: Double, blue: Double, alpha: Double)

	fun glColor4ub(red: UByte, green: UByte, blue: UByte, alpha: UByte)

	fun glColor4us(red: UShort, green: UShort, blue: UShort, alpha: UShort)

	fun glColor4ui(red: UInt, green: UInt, blue: UInt, alpha: UInt)

	fun glColor4bv(values: ByteBuffer)

	fun glColor4sv(values: ShortBuffer)

	fun glColor4iv(values: IntBuffer)

	fun glColor4fv(values: FloatBuffer)

	fun glColor4dv(values: DoubleBuffer)

	fun glColor4ubv(values: ByteBuffer)

	fun glColor4usv(values: ShortBuffer)

	fun glColor4uiv(values: IntBuffer)

	fun glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean)

	fun glColorMaterial(face: GLFace, mode: GLColorMaterialProperty)

	fun glColorPointer(size: Int, type: GLType, stride: Int, pointer: ByteBuffer)

	fun glColorPointer(size: Int, type: GLType, stride: Int, pointer: Long)

	fun glColorPointer(size: Int, type: GLType, stride: Int, pointer: ShortBuffer)

	fun glColorPointer(size: Int, type: GLType, stride: Int, pointer: IntBuffer)

	fun glColorPointer(size: Int, type: GLType, stride: Int, pointer: FloatBuffer)

	fun glCopyPixels(x: Int, y: Int, width: Int, height: Int, type: PixelCopyType)

	fun glCullFace(face: GLFace)

	fun glDeleteLists(listId: Int, range: Int)

	fun glDepthFunc(function: GLComparisonFunction)

	fun glDepthMask(flag: Boolean)

	fun glDepthRange(zNear: Double, zFar: Double)

	fun glDisableClientState(capability: ClientStateCapability)

	fun glDrawArrays(primitiveType: PrimitiveType, first: Int, count: Int)

	fun glDrawBuffer(buffer: DrawBufferMode)

	fun glDrawElements(primitiveType: PrimitiveType, count: Int, indiceType: GLType.Unsigned, indicesPointer: Long)

	fun glDrawElements(primitiveType: PrimitiveType, indiceType: GLType.Unsigned, indices: ByteBuffer)

	fun glDrawElements(primitiveType: PrimitiveType, indices: ByteBuffer)

	fun glDrawElements(primitiveType: PrimitiveType, indices: ShortBuffer)

	fun glDrawElements(primitiveType: PrimitiveType, indices: IntBuffer)

	fun glDrawPixels(width: Int, height: Int, format: PixelFormat, type: GLType /*todo*/, pixels: ByteBuffer)

	fun glDrawPixels(width: Int, height: Int, format: PixelFormat, type: GLType /*todo*/, pixelsPointer: Long)

	fun glDrawPixels(width: Int, height: Int, format: PixelFormat, type: GLType /*todo*/, pixels: ShortBuffer)

	fun glDrawPixels(width: Int, height: Int, format: PixelFormat, type: GLType /*todo*/, pixels: IntBuffer)

	fun glDrawPixels(width: Int, height: Int, format: PixelFormat, type: GLType /*todo*/, pixels: FloatBuffer)

	fun glEdgeFlag(flag: Boolean)

	fun glEdgeFlagv(flag: ByteBuffer)

	fun glEdgeFlagPointer(stride: Int, pointer: ByteBuffer)

	fun glEdgeFlagPointer(stride: Int, pointer: Long)

	fun glEnableClientState(capability: ClientStateCapability)

	fun glEnd()
}

open class GL11State internal constructor(
		glConstant: Int
) : GLEnum(glConstant)

open class GL11AccumulationOperation internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11AccumulationOperation(glConstant)

		val accumulate = new(0x100) // GL_ACCUM
		val load = new(0x101) // GL_LOAD
		val `return` = new(0x102) // GL_RETURN
		val multiply = new(0x103) // GL_MULT
		val add = new(0x104) // GL_ADD
	}
}

open class GL11PrimitiveType internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11PrimitiveType(glConstant)

		val points = new(0x0) // GL_POINTS
		val lines = new(0x1) // GL_LINES
		val lineLoop = new(0x2) // GL_LINE_LOOP
		val lineStrip = new(0x3) // GL_LINE_STRIP
		val triangles = new(0x4) // GL_TRIANGLES
		val triangleStrip = new(0x5) // GL_TRIANGLE_STRIP
		val triangleFan = new(0x6) // GL_TRIANGLE_FAN
		val quads = new(0x7) // GL_QUADS
		val quadStrip = new(0x8) // GL_QUAD_STRIP
		val polygon = new(0x9) // GL_POLYGON
	}
}

open class GL11TextureTarget internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11TextureTarget(glConstant)

		val texture1D = new(0xDE0) // GL_TEXTURE_1D
		val texture2D = new(0xDE1) // GL_TEXTURE_2D
	}
}

open class GL11PixelCopyType internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11PixelCopyType(glConstant)

		val color = new(0x1800) // GL_COLOR
		val stencil = new(0x1802) // GL_STENCIL
		val depth = new(0x1801) // GL_DEPTH
	}
}

open class GL11ClientStateCapability internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11ClientStateCapability(glConstant)

		val colorArray = new(0x8076) // GL_COLOR_ARRAY
		val edgeFlagArray = new(0x8079) // GL_EDGE_FLAG_ARRAY
		val indexArray = new(0x8077) // GL_INDEX_ARRAY
		val normalArray = new(0x8075) // GL_NORMAL_ARRAY
		val textureCoordArray = new(0x8078) // GL_TEXTURE_COORD_ARRAY
		val vertexArray = new(0x8074) // GL_VERTEX_ARRAY
		val vertexAttributeArrayUnifiedNV = new(0x8F1E) // GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV
		val elementArrayUnifiedNV = new(0x8F1F) // GL_ELEMENT_ARRAY_UNIFIED_NV
	}
}

open class GL11DrawBufferMode internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11DrawBufferMode(glConstant)

		val none = new(0) // GL_NONE
		val frontLeft = new(0x400) // GL_FRONT_LEFT
		val frontRight = new(0x401) // GL_FRONT_RIGHT
		val backLeft = new(0x402) // GL_BACK_LEFT
		val backRight = new(0x403) // GL_BACK_RIGHT
		val front = new(0x404) // GL_FRONT
		val back = new(0x405) // GL_BACK
		val left = new(0x406) // GL_LEFT
		val right = new(0x407) // GL_RIGHT
		val frontAndBack = new(0x408) // GL_FRONT_AND_BACK
	}
}

open class GL11PixelFormat internal constructor(
		glConstant: Int
) : GLEnum(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL11PixelFormat(glConstant)

		val red = new(0x1903) // GL_RED
		val green = new(0x1904) // GL_GREEN
		val blue = new(0x1905) // GL_BLUE
		val alpha = new(0x1906) // GL_ALPHA
		val rgb = new(0x1907) // GL_RGB
		val rgba = new(0x1908) // GL_RGBA
		val stencilIndex = new(0x1901) // GL_STENCIL_INDEX
		val depthComponent = new(0x1902) // GL_DEPTH_COMPONENT
		val luminance = new(0x1909) // GL_LUMINANCE
		val luminanceAlpha = new(0x190A) // GL_LUMINANCE_ALPHA
	}
}