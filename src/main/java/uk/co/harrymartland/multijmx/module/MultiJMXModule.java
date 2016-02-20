package uk.co.harrymartland.multijmx.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.co.harrymartland.multijmx.domain.LifeCycleAble;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessor;
import uk.co.harrymartland.multijmx.processer.MultiJMXProcessorImpl;
import uk.co.harrymartland.multijmx.service.connection.ConnectionService;
import uk.co.harrymartland.multijmx.service.connection.ConnectionServiceImpl;
import uk.co.harrymartland.multijmx.service.connector.ConnectorService;
import uk.co.harrymartland.multijmx.service.connector.ConnectorServiceImpl;
import uk.co.harrymartland.multijmx.service.file.FileReaderService;
import uk.co.harrymartland.multijmx.service.file.FileReaderServiceImpl;
import uk.co.harrymartland.multijmx.service.lifecycle.LifeCycleService;
import uk.co.harrymartland.multijmx.service.lifecycle.LifeCycleServiceImpl;
import uk.co.harrymartland.multijmx.waitable.SystemWaitable;
import uk.co.harrymartland.multijmx.waitable.Waitable;
import uk.co.harrymartland.multijmx.writer.SystemOutWriter;
import uk.co.harrymartland.multijmx.writer.Writer;

public class MultiJMXModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ExpressionParser.class).to(SpelExpressionParser.class);
        bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
        bind(MultiJMXProcessor.class).to(MultiJMXProcessorImpl.class);
        bind(Writer.class).to(SystemOutWriter.class);
        bind(Waitable.class).to(SystemWaitable.class);
        bind(LifeCycleService.class).to(LifeCycleServiceImpl.class);
        bind(ConnectionService.class).to(ConnectionServiceImpl.class);
        bind(FileReaderService.class).to(FileReaderServiceImpl.class);
        bind(ConnectorService.class).to(ConnectorServiceImpl.class);

        Multibinder<LifeCycleAble> lifeCycleAbleMultibinder = Multibinder.newSetBinder(binder(), LifeCycleAble.class);
        lifeCycleAbleMultibinder.addBinding().to(SystemWaitable.class);
    }
}
