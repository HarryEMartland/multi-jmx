package uk.co.harrymartland.multijmx.domain.optionvalue;

import org.junit.Test;

public abstract class AbstractSingleOptionValueTest<T extends AbstractSingleOptionValue<K>, K> extends AbstractOptionValueTest<T, K> {

    @Test
    public void testShouldNotPassValidationWhenMoreThanOneArgumentPassed() throws Exception {
        givenCommandLineArguments("str1", "str2");
        thenShouldThrowValidationExceptionOnValidation(getMoreThanOneArgumentError());
    }

    protected abstract String getMoreThanOneArgumentError();

}
