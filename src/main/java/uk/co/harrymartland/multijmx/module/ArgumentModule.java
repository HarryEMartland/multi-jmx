package uk.co.harrymartland.multijmx.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import uk.co.harrymartland.multijmx.domain.OptionValue.OptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.connectionarg.ConnectionArgOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.connectionfile.ConnectionFileOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.delimiter.DelimiterOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.delimiter.DelimiterOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.help.HelpOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.help.HelpOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.maxthreads.MaxThreadsOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.object.ObjectOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.orderconnection.OrderConnectionOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.ordervalue.OrderValueOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.password.PasswordOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.reverseorder.ReverseOrderOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.signature.SignatureOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.OptionValue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.domain.OptionValue.username.UserNameOptionValueImpl;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineService;
import uk.co.harrymartland.multijmx.service.commandline.CommandLineServiceImpl;
import uk.co.harrymartland.multijmx.service.options.OptionsService;
import uk.co.harrymartland.multijmx.service.options.OptionsServiceImpl;
import uk.co.harrymartland.multijmx.service.validator.ValidatorService;
import uk.co.harrymartland.multijmx.service.validator.ValidatorServiceImpl;

public class ArgumentModule extends AbstractModule {

    private Multibinder<OptionValue> optionValueMultiBinder;

    @Override
    protected void configure() {

        bind(OptionsService.class).to(OptionsServiceImpl.class);
        bind(CommandLineService.class).to(CommandLineServiceImpl.class);
        bind(CommandLineParser.class).to(DefaultParser.class);
        bind(ValidatorService.class).to(ValidatorServiceImpl.class);

        bindOptionValue(OrderValueOptionValue.class, OrderValueOptionValueImpl.class);
        bindOptionValue(ObjectOptionValue.class, ObjectOptionValueImpl.class);
        bindOptionValue(SignatureOptionValue.class, SignatureOptionValueImpl.class);
        bindOptionValue(OrderConnectionOptionValue.class, OrderConnectionOptionValueImpl.class);
        bindOptionValue(ReverseOrderOptionValue.class, ReverseOrderOptionValueImpl.class);
        bindOptionValue(MaxThreadsOptionValue.class, MaxThreadsOptionValueImpl.class);
        bindOptionValue(UserNameOptionValue.class, UserNameOptionValueImpl.class);
        bindOptionValue(PasswordOptionValue.class, PasswordOptionValueImpl.class);
        bindOptionValue(DelimiterOptionValue.class, DelimiterOptionValueImpl.class);
        bindOptionValue(ConnectionArgOptionValue.class, ConnectionArgOptionValueImpl.class);
        bindOptionValue(ConnectionFileOptionValue.class, ConnectionFileOptionValueImpl.class);
        bindOptionValue(HelpOptionValue.class, HelpOptionValueImpl.class);
    }

    private Multibinder<OptionValue> getOptionBinder() {
        if (optionValueMultiBinder == null) {
            optionValueMultiBinder = Multibinder.newSetBinder(binder(), OptionValue.class);
        }
        return optionValueMultiBinder;
    }

    private <Interface extends OptionValue, Implementation extends Interface> void bindOptionValue(Class<Interface> optionInterface, Class<Implementation> optionImplementation) {
        bind(optionInterface).to(optionImplementation);
        getOptionBinder().addBinding().to(optionImplementation);
    }
}
