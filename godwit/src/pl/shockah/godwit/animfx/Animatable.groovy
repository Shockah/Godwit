package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
trait Animatable<T extends Animatable<T>> {
    @Nonnull final List<FxInstance<? extends T>> fxes = []

    void updateFx() {
        for (int i = 0; i < fxes.size(); i++) {
            FxInstance<? extends T> fx = fxes[i]
            fx.updateDelta((T)this)
            if (fx.stopped)
                fxes.remove(i--)
        }
    }
}