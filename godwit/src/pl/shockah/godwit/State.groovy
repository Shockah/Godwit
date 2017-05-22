package pl.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

@CompileStatic
class State extends EntityGroup<Entity> {
	@PackageScope final void create() {
		if (created || destroyed)
			return
		created = true
		onCreate()
	}
}