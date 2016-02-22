package uk.co.harrymartland.multijmx.domain.optionvalue.threadpool;

import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolOptionValueImpl extends AbstractOptionValue<ExecutorService> implements ThreadPoolOptionValue {

    private MaxThreadsOptionValue maxThreadsOptionValue;

    @Inject
    public ThreadPoolOptionValueImpl(CommandLineService commandLineService, MaxThreadsOptionValue maxThreadsOptionValue) {
        super(commandLineService);
        this.maxThreadsOptionValue = maxThreadsOptionValue;
    }

    @Override
    protected ExecutorService lazyLoadValue() {
        if (maxThreadsOptionValue.getValue() != null) {
            return Executors.newFixedThreadPool(maxThreadsOptionValue.getValue());
        } else {
            return Executors.newCachedThreadPool();
        }
    }

    @Override
    protected Option lazyLoadOption() {
        return null;
    }

    @Override
    public String getArg() {
        return null;
    }

    @Override
    public void birth() {
        //not needed
    }

    @Override
    public void die() {
        if (getValue() != null) {
            getValue().shutdown();
        }
    }
}
