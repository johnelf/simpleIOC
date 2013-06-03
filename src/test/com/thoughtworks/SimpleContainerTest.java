package com.thoughtworks;

import com.thoughtworks.ioc.SimpleContainer;
import com.thoughtworks.test.ClassWithDefaultConstructor;
import com.thoughtworks.test.ElectricalPrinter;
import com.thoughtworks.test.Print;
import com.thoughtworks.test.SimpleService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleContainerTest {
    private SimpleContainer container;

    @Before
    public void setUp() throws Exception {
        container = new SimpleContainer("com.thoughtworks.test");
    }

    @Test
    public void should_get_instance_by_default_constructor() {
        ClassWithDefaultConstructor instance = container.getComponent(ClassWithDefaultConstructor.class);
        assertNotNull(instance);
    }

    @Test
    public void should_get_instance_with_multiple_arguments() {
        container.addComponent(Print.class, ElectricalPrinter.class);

        SimpleService service = container.getComponent(SimpleService.class);
        assertThat(service instanceof SimpleService, is(true));
        assertThat(service.getPrinter() instanceof Print, is(true));
    }
}
