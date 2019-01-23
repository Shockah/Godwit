package pl.shockah.godwit.gl

interface GLBinding {
}

open class GLEnum internal constructor(
		val glConstant: Int
)

inline class GLClearMask(
		val glConstant: Int
) {
	companion object {
		private fun new(glConstant: Int) = GLClearMask(glConstant)

		val none = new(0)
		val color = new(0x4000) // GL_COLOR_BUFFER_BIT
		val depth = new(0x100) // GL_DEPTH_BUFFER_BIT
		val stencil = new(0x400) // GL_STENCIL_BUFFER_BIT
	}

	infix fun or(mask: GLClearMask) = GLClearMask(glConstant or mask.glConstant)
}

enum class GLFace(
		val glConstant: Int
) {
	Front(0x404),
	Back(0x405),
	FrontAndBack(0x408)
}

enum class GLColorMaterialProperty(
		val glConstant: Int
) {
	Emission(0x1600), // GL_EMISSION
	Ambient(0x1200), // GL_AMBIENT
	Diffuse(0x1201), // GL_DIFFUSE
	Specular(0x1202), // GL_SPECULAR
	AmbientAndDiffuse(0x1602), // GL_AMBIENT_AND_DIFFUSE
}

interface GLType {
	enum class Signed(
			val glConstant: kotlin.Int
	) : GLType {
		Byte(0x1400), // GL_BYTE
		Short(0x1402), // GL_SHORT
		Int(0x1404) // GL_INT
	}

	enum class Unsigned(
			val glConstant: kotlin.Int
	) : GLType {
		Byte(0x1401), // GL_UNSIGNED_BYTE
		Short(0x1403), // GL_UNSIGNED_SHORT
		Int(0x1405) // GL_UNSIGNED_INT
	}

	enum class Floating(
			val glConstant: kotlin.Int
	) : GLType {
		Single(0x1406), // GL_FLOAT
		Double(0x140A) // GL_DOUBLE
	}

	enum class Misc(
			val glConstant: kotlin.Int
	) : GLType {
		TwoBytes(0x1407), // GL_2_BYTES
		ThreeBytes(0x1408), // GL_3_BYTES
		FourBytes(0x1409) // GL_4_BYTES
	}
}

enum class GLComparisonFunction(
		val glConstant: Int
) {
	Never(0x200), // GL_NEVER
	Less(0x201), // GL_LESS
	Equal(0x202), // GL_EQUAL
	LessOrEqual(0x203), // GL_LEQUAL
	Greater(0x204), // GL_GREATER
	NotEqual(0x205), // GL_NOTEQUAL
	GreaterOrEqual(0x206), // GL_GEQUAL
	Always(0x207) // GL_ALWAYS
}

enum class GLBlendFactor(
		val glConstant: Int
) {
	Zero(0), // GL_ZERO
	One(1), // GL_ONE
	SourceColor(0x300), // GL_SRC_COLOR
	OneMinusSourceColor(0x301), // GL_ONE_MINUS_SRC_COLOR
	SourceAlpha(0x302), // GL_SRC_ALPHA
	OneMinusSourceAlpha(0x303), // GL_ONE_MINUS_SRC_ALPHA
	DestinationAlpha(0x304), // GL_DST_ALPHA
	OneMinusDestinationAlpha(0x305) // GL_ONE_MINUS_DST_ALPHA
}

enum class GLAccumulationOperation(
		val glConstant: Int
) {
	Accumulate(0x100), // GL_ACCUM
	Load(0x101), // GL_LOAD
	Return(0x102), // GL_RETURN
	Multiply(0x103), // GL_MULT
	Add(0x104) // GL_ADD
}