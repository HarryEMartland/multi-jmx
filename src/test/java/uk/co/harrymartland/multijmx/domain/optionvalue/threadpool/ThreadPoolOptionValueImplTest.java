package uk.co.harrymartland.multijmx.domain.optionvalue.threadpool;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

public class ThreadPoolOptionValueImplTest extends AbstractOptionValueTest<ThreadPoolOptionValueImpl, ExecutorService> {

    private MaxThreadsOptionValue maxThreadsOptionValue = Mockito.mock(MaxThreadsOptionValue.class);

    @Test
    public void testShouldReturnCachedThreadPoolWhenNoMaxSet() throws Exception {
        thenShouldReturnExecutorWithMaxThreads(Integer.MAX_VALUE);
    }

    @Test
    public void testShouldReturnThreadPoolWithSize5WheMaxSizeIsSetTo5() throws Exception {
        Mockito.when(maxThreadsOptionValue.getValue()).thenReturn(5);
        thenShouldReturnExecutorWithMaxThreads(5);
    }

    private void thenShouldReturnExecutorWithMaxThreads(Integer expectedSize) {
        ExecutorService executorService = getOptionValue().getValue();
        Integer actualSize = reflectionGet(executorService, "maximumPoolSize");
        Assert.assertEquals(expectedSize, actualSize);
    }


    private <T> T reflectionGet(Object object, String fieldName) {
        Field field = ReflectionUtils.findField(object.getClass(), fieldName);
        ReflectionUtils.makeAccessible(field);
        //noinspection unchecked
        return (T) ReflectionUtils.getField(field, object);
    }

    @Override
    protected ThreadPoolOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new ThreadPoolOptionValueImpl(commandLineService, maxThreadsOptionValue);
    }
}