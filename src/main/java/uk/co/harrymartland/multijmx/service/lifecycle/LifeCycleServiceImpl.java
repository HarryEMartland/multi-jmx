package uk.co.harrymartland.multijmx.service.lifecycle;

import com.google.inject.Inject;
import com.google.inject.Provider;
import uk.co.harrymartland.multijmx.domain.LifeCycleAble;

import java.util.Set;

import static uk.co.harrymartland.multijmx.ExceptionUtils.eat;

public class LifeCycleServiceImpl implements LifeCycleService {

    private Provider<Set<LifeCycleAble>> lifeCycleAbles;

    @Inject
    public LifeCycleServiceImpl(Provider<Set<LifeCycleAble>> lifeCycleAbles) {
        this.lifeCycleAbles = lifeCycleAbles;
    }

    @Override
    public void birthAll() {
        lifeCycleAbles.get().forEach(LifeCycleAble::birth);
    }

    @Override
    public void killAll() {
        lifeCycleAbles.get().forEach(eat(LifeCycleAble::die));
    }
}
