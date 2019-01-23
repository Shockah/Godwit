package pl.shockah.godwit

import pl.shockah.godwit.gl.GLBinding

interface Backend {
	val glBinding: GLBinding

	fun init()

	fun dispose()

	fun pause()

	fun resume()

	fun loop()

	fun update()

	fun render()
}