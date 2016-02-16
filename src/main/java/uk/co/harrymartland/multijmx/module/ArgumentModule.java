package uk.co.harrymartland.multijmx.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import uk.co.harrymartland.multijmx.domain.optionvalue.OptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connection.ConnectionOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionarg.ConnectionArgOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.connectionfile.ConnectionFileOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.delimiter.DelimiterOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.delimiter.DelimiterOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.help.HelpOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.help.HelpOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.maxthreads.MaxThreadsOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.object.ObjectOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.objectsignature.ObjectSignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.objectsignature.ObjectSignatureOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.order.OrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.order.OrderOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.orderconnection.OrderConnectionOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.ordervalue.OrderValueOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.password.PasswordOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.reverseorder.ReverseOrderOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.signature.SignatureOptionValueImpl;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValue;
import uk.co.harrymartland.multijmx.domain.optionvalue.username.UserNameOptionValueImpl;
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
        bindOptionValue(ConnectionOptionValue.class, ConnectionOptionValueImpl.class);
        bindOptionValue(OrderOptionValue.class, OrderOptionValueImpl.class);
        bindOptionValue(ObjectSignatureOptionValue.class, ObjectSignatureOptionValueImpl.class);
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
