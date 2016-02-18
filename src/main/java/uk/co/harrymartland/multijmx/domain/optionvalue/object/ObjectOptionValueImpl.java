package uk.co.harrymartland.multijmx.domain.optionvalue.object;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Inject;
import org.apache.commons.cli.Option;
import uk.co.harrymartland.multijmx.domain.ValidationException;
import uk.co.harrymartland.multijmx.domain.optionvalue.AbstractMultiOptionValue;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectOptionValueImpl extends AbstractMultiOptionValue<List<ObjectName>> implements ObjectOptionValue {

    @Inject
    public ObjectOptionValueImpl(CommandLineService commandLineService) {
        super(commandLineService);
    }

    @Override
    public String getArg() {
        return "o";
    }

    @Override
    public Option lazyLoadOption() {
        return Option.builder(getArg())
                .longOpt("object-name")
                .argName("object name")
                .hasArg()
                .desc("JMX object name to read from e.g. 'java.lang:type=OperatingSystem'")
                .build();
    }

    @Override
    protected boolean isRequired() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        for (String objectName : getStringValues()) {
            validateObjectName(objectName);
        }
    }

    @Override
    protected String getRequiredError() {
        return "No object name provided";
    }

    @Override
    public List<ObjectName> lazyLoadValue() {
        return Arrays.stream(getStringValues())
                .map(this::createObjectNameUnchecked)
                .collect(Collectors.toList());
    }

    private void validateObjectName(String objectName) throws ValidationException {
        try {
            new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            throw new ValidationException("Could not parse object name: " + objectName, e);
        }
    }

    private ObjectName createObjectNameUnchecked(String objectName) {
        try {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException e) {
            throw new UncheckedExecutionException("Could not parse object name: " + objectName, e);
        }
    }
}
