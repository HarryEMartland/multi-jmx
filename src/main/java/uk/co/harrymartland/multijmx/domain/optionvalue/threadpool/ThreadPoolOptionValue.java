package uk.co.harrymartland.multijmx.domain.optionvalue.threadpool;

import uk.co.harrymartland.multijmx.domain.LifeCycleAble;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;

import java.util.concurrent.ExecutorService;

public interface ThreadPoolOptionValue extends OptionValue<ExecutorService>, LifeCycleAble {
}
