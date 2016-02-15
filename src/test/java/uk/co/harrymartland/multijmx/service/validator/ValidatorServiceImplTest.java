package uk.co.harrymartland.multijmx.service.validator;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;

public class ValidatorServiceImplTest {


    @Inject
    private ValidatorService validatorService;
    private OptionValue optionValue1 = Mockito.mock(OptionValue.class);
    private OptionValue optionValue2 = Mockito.mock(OptionValue.class);

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                Multibinder<OptionValue> optionValueMultibinder = Multibinder.newSetBinder(binder(), OptionValue.class);
                optionValueMultibinder.addBinding().toInstance(optionValue1);
                optionValueMultibinder.addBinding().toInstance(optionValue2);

                bind(ValidatorService.class).to(ValidatorServiceImpl.class);

            }
        });
        injector.injectMembers(this);
    }

    @Test
    public void testShouldCallValidateOnceOnAllOptionValues() throws Exception {
        validatorService.validate();
        Mockito.verify(optionValue1, Mockito.times(1)).validate();
        Mockito.verify(optionValue2, Mockito.times(1)).validate();
    }
}