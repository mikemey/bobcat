package uk.mm.bobcat;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import uk.mm.bobcat.domain.BobcatUser;
import uk.mm.bobcat.domain.NameCandidate;
import uk.mm.bobcat.domain.Rating;

public abstract class ARunningMongoDBTest extends ARunningContextTest {
	@Autowired
	protected MongoTemplate mongoTemplate;

	@Before
	public final void setup() {
		mongoTemplate.dropCollection(Rating.class);
		mongoTemplate.dropCollection(NameCandidate.class);
		mongoTemplate.dropCollection(BobcatUser.class);
		beforeTest();
	}

	protected void beforeTest() {
		// instead of @BeforeTest
	}
}