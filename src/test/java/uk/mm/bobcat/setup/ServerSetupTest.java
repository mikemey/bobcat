package uk.mm.bobcat.setup;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import uk.mm.bobcat.ARunningServerTest;

public class ServerSetupTest extends ARunningServerTest {
	@Test
	public void testStaticContent() throws IOException {
		String docString = getResponseString("/styles/commons.css");
		Assert.assertTrue(docString.contains("html {"));
		Assert.assertTrue(docString.contains("body {"));
	}

	@Test
	public void testPingPage() throws Exception {
		String docString = getResponseString("/");
		Assert.assertTrue(docString.contains("<h1>Project Bobcat</h1>"));
		Assert.assertTrue(docString.contains("How to pick your baby girl's name"));
	}
}
