package uk.mm.bobcat;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.mm.bobcat.config.RootConfiguration;
import uk.mm.bobcat.config.WebAppInitializer;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
		@ContextConfiguration(classes = { BobcatTestConfig.class, RootConfiguration.class }),
		@ContextConfiguration(classes = { WebAppInitializer.class })
})
/*package*/abstract class ARunningContextTest {
	// common annotations
}