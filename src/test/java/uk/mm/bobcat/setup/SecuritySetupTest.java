package uk.mm.bobcat.setup;

import java.io.IOException;

import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import uk.mm.bobcat.ARunningServerTest;

public class SecuritySetupTest extends ARunningServerTest {

	@Test
	public void testForwardToLoginPage() throws IOException {
		String responsePage = getResponseString("/competition");
		Assert.assertTrue(responsePage.contains("Overlay content"));
		
	}
	@Test
	public void testLoginOverlayForm() throws IOException {
		Document doc = getResponseDocument("/login");
		String responsePage = doc.toString();
		Assert.assertTrue(responsePage.contains("Overlay content"));

		Elements forms = doc.getElementsByTag("form");
		Assert.assertEquals("No login form found!", 1, forms.size());
		Element formElement = forms.get(0);
		Assert.assertEquals("Form action unknown:", FormAuthenticator.__J_SECURITY_CHECK, formElement.attr("action"));

		Elements inputs = formElement.getElementsByTag("input");
		String[] found = new String[] { "user", "password" };
		for (Element input : inputs) {
			String name = input.attr("name");
			if (FormAuthenticator.__J_USERNAME.equals(name)) {
				found[0] = null;
			}
			if (FormAuthenticator.__J_PASSWORD.equals(name)) {
				found[1] = null;
			}
		}
		for (String inputFound : found) {
			Assert.assertNull(inputFound + " input element (identified by name) not found.", inputFound);
		}
	}
}
