package uk.co.harrymartland.multijmx.domain.OptionValue.signature;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.OptionValue.AbstractOptionValueTest;
import uk.co.harrymartland.multijmx.domain.typeable.ObjectType;
import uk.co.harrymartland.multijmx.domain.typeable.Typeable;
import uk.co.harrymartland.multijmx.domain.valueretriver.AttributeValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.JMXValueRetriever;
import uk.co.harrymartland.multijmx.domain.valueretriver.SpelMethodValueRetriever;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import java.util.Collections;
import java.util.List;

public class SignatureOptionValueImplTest extends AbstractOptionValueTest<SignatureOptionValueImpl, List<JMXValueRetriever>> {

    @Override
    protected SignatureOptionValueImpl createOptionValue(CommandLineService commandLineService) {
        return new SignatureOptionValueImpl(commandLineService, new SpelExpressionParser());
    }

    @Override
    protected String getRequiredValidation() {
        return "No signature provided";
    }

    @Test
    public void testShouldNotThrowExceptionWhenValidField() throws Exception {
        givenCommandLineArguments("field");
        getOptionValue().validate();
    }

    @Test
    public void testShouldNotThrowExceptionWhenValidMethod() throws Exception {
        givenCommandLineArguments("method()");
        getOptionValue().validate();
    }

    @Test
    public void testShouldNotThrowExceptionWhenValidMethodWithArgs() throws Exception {
        givenCommandLineArguments("method(1,\"test\")");
        getOptionValue().validate();
    }

    @Test
    public void testShouldNotThrowExceptionWhenValidFields() throws Exception {
        givenCommandLineArguments("field", "field2", "field3");
        getOptionValue().validate();
    }

    @Test
    public void testShouldThrowExceptionWhenInValidField() throws Exception {
        givenCommandLineArguments("field*");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: field*");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidLastField() throws Exception {
        givenCommandLineArguments("field", "field2", "field*");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: field*");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidMethodName() throws Exception {
        givenCommandLineArguments("method*()");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: method*");
    }


    @Test
    public void testShouldThrowExceptionWhenInValidMethodBrackets() throws Exception {
        givenCommandLineArguments("method(");
        thenShouldThrowValidationExceptionOnValidation("Method signature should match methodName(arg1,arg2): method(");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidMethodArgs() throws Exception {
        givenCommandLineArguments("method(d)");
        thenShouldThrowValidationExceptionOnValidation("Incorrect argument parameter: d");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidLastMethodName() throws Exception {
        givenCommandLineArguments("method()", "method()", "method*()");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: method*");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidLastMethodNameWithField() throws Exception {
        givenCommandLineArguments("method()", "field", "method*()");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: method*");
    }

    @Test
    public void testShouldThrowExceptionWhenInValidLastFieldWithMethods() throws Exception {
        givenCommandLineArguments("method()", "field", "field*");
        thenShouldThrowValidationExceptionOnValidation("Name must not contain special characters: field*");
    }

    @Test
    public void testShouldNotThrowExceptionWhenInValidFieldsAndMethods() throws Exception {
        givenCommandLineArguments("method()", "field", "field");
        getOptionValue().validate();
    }

    @Test
    public void testShouldReturnFieldValueRetrieverWhenFieldPassed() throws Exception {
        givenCommandLineArguments("field");
        Assert.assertEquals(Collections.singletonList(new AttributeValueRetriever("field")), getOptionValue().getValue());
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodPassed() throws Exception {
        givenCommandLineArguments("test()");
        Assert.assertEquals(Collections.singletonList(new SpelMethodValueRetriever("test", new Typeable[]{})), getOptionValue().getValue());
    }

    @Test
    public void testShouldReturnMethodValueRetrieverWhenMethodPassedWithArgs() throws Exception {
        givenCommandLineArguments("test(1, \"test\")");
        Assert.assertEquals(Collections.singletonList(new SpelMethodValueRetriever("test", new Typeable[]{new ObjectType(1), new ObjectType("test")})), getOptionValue().getValue());
    }
}