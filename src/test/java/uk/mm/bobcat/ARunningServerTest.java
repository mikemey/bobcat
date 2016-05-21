package uk.mm.bobcat;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;

public abstract class ARunningServerTest extends ARunningContextTest {
	protected static final String SERVER_URL = "https://localhost:22699";

	@BeforeClass
	public static final void setup() {
		System.setProperty("javax.net.ssl.keyStore", "keystore/keystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "caterpillar");
		System.setProperty("javax.net.ssl.trustStore", "keystore/keystore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "caterpillar");
	}

	protected static final String getResponseString(String relativePath) throws IOException {
		return getResponseDocument(relativePath).toString();
	}

	protected static final Document getResponseDocument(String relativePath) throws IOException {
		return Jsoup.connect(SERVER_URL + relativePath).get();
	}
}
