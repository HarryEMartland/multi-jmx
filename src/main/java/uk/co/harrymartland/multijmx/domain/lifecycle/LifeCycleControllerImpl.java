package uk.co.harrymartland.multijmx.domain.lifecycle;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Set;

public class LifeCycleControllerImpl implements LifeCycleController {

    private Provider<Set<LifeCycleAble>> lifeCycleAbles;

    @Inject
    public LifeCycleControllerImpl(Provider<Set<LifeCycleAble>> lifeCycleAbles) {
        this.lifeCycleAbles = lifeCycleAbles;
    }

    @Override
    public void birthAll() {
        lifeCycleAbles.get().forEach(LifeCycleAble::birth);
    }

    @Override
    public void killAll() {
        lifeCycleAbles.get().forEach(LifeCycleAble::die);
    }//todo move to service package and rename
}
