package org.acme.spring.web;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
@QuarkusIntegrationTest
public class GreetingControllerIT extends GreetingControllerTest {
    // Execute the same tests but in packaged mode.
}
