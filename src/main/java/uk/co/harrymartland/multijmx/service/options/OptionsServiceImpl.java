package uk.co.harrymartland.multijmx.service.options;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.cli.Options;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;

import java.util.Objects;
import java.util.Set;

@Singleton
public class OptionsServiceImpl implements OptionsService {

    private Set<OptionValue> optionValues;
    private Options options;

    @Inject
    public OptionsServiceImpl(Set<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }

    @Override
    public Options getOptions() {
        if (options == null) {
            options = new Options();
            optionValues.stream()
                    .map(OptionValue::getOption)
                    .filter(Objects::nonNull)
                    .forEach(option -> options.addOption(option));
        }
        return options;
    }
}
