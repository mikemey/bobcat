package uk.mm.bobcat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class ARunningServerTest extends ARunningContextTest {
    protected static final String SERVER_URL = "http://localhost:22699";

    protected static String getResponseString(String relativePath) throws IOException {
        return getResponseDocument(relativePath).toString();
    }

    protected static Document getResponseDocument(String relativePath) throws IOException {
        return Jsoup.connect(SERVER_URL + relativePath).get();
    }
}
