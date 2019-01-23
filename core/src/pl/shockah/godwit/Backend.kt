package pl.shockah.godwit

interface Backend {
	fun init()

	fun dispose()

	fun pause()

	fun resume()

	fun loop()

	fun update()

	fun render()
}