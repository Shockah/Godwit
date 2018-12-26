package pl.shockah.godwit

class Godwit(
		val backend: Backend,
		private val initialStateFactory: () -> GameState
) {
	private var _state: GameState? = null
	var state: GameState
		get() = _state ?: throw IllegalStateException("Not yet initialized.")
		private set(value) {
			_state = value
		}

	fun launch() {
		backend.init()
		backend.loop()
		backend.dispose()
	}

	fun create() {
		state = initialStateFactory()
	}

	fun render() {
		state.render()
	}
}