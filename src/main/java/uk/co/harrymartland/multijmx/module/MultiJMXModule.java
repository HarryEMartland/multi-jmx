package uk.co.harrymartland.multijmx.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParser;
import uk.co.harrymartland.multijmx.argumentparser.MultiJMXArgumentParserImpl;
import uk.co.harrymartland.multijmx.domain.lifecycle.LifeCycleAble;
import uk.co.harrymartland.multijmx.domain.lifecycle.LifeCycleController;
import uk.co.harrymartland.multijmx.domain.lifecycle.LifeCycleControllerImpl;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;
import uk.co.harrymartland.multijmx.service.valueretriever.ValueRetrieverService;
import uk.co.harrymartland.multijmx.service.valueretriever.ValueRetrieverServiceImpl;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidator;
import uk.co.harrymartland.multijmx.validator.MultiJMXOptionValidatorImpl;
import uk.co.harrymartland.multijmx.waitable.SystemWaitable;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.SystemOutWriter;
import uk.co.harrymartland.multijmx.writer.Writer;

public class MultiJMXModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(ExpressionParser.class).to(SpelExpressionParser.class);
        bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
        bind(MultiJMXOptionValidator.class).to(MultiJMXOptionValidatorImpl.class);
        bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
        bind(Writer.class).to(SystemOutWriter.class);
        bind(Waitable.class).to(SystemWaitable.class);
        bind(MultiJMXArgumentParser.class).to(MultiJMXArgumentParserImpl.class);
        bind(ExpressionParser.class).to(SpelExpressionParser.class);
        bind(ValueRetrieverService.class).to(ValueRetrieverServiceImpl.class);
        bind(LifeCycleController.class).to(LifeCycleControllerImpl.class);

        Multibinder<LifeCycleAble> lifeCycleAbleMultibinder = Multibinder.newSetBinder(binder(), LifeCycleAble.class);
        lifeCycleAbleMultibinder.addBinding().to(SystemWaitable.class);
    }
}
