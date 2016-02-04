package uk.co.harrymartland.multijmx.domain.lifecycle;

import com.google.inject.Inject;

import java.util.Set;

public class LifeCycleControllerImpl implements LifeCycleController {

    private Set<LifeCycleAble> lifeCycleAbles;

    @Inject
    public LifeCycleControllerImpl(Set<LifeCycleAble> lifeCycleAbles) {
        this.lifeCycleAbles = lifeCycleAbles;
    }

    @Override
    public void birthAll() {
        lifeCycleAbles.forEach(LifeCycleAble::birth);
    }

    @Override
    public void killAll() {
        lifeCycleAbles.forEach(LifeCycleAble::die);
    }
}
