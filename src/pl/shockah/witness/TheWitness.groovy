package pl.shockah.witness

import com.badlogic.gdx.ApplicationAdapter
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.witness.state.GameState

@CompileStatic
class TheWitness extends ApplicationAdapter {
	TheWitness() {
		GroovyMeta.setup()
	}

	@Override
	void create() {
		Godwit.instance.setState(new GameState())
	}

	@Override
	void render() {
		Godwit.instance.tick()
	}

	@Override
	void resize(int width, int height) {
		//System.out.println(String.format("W: %d, H: %d", width, height))
		Godwit.instance.gfx.updateCamera()
	}
}