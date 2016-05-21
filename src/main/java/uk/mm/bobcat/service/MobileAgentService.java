package uk.mm.bobcat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MobileAgentService {
	private final Logger logger = LoggerFactory.getLogger(MobileAgentService.class);

	private String[] mobileAgents = new String[] {
			".*(webos|palm|treo).*",
			".*(android).*",
			".*(kindle|pocket|o2|vodaphone|wap|midp|psp).*",
			".*(iphone|ipod).*",
			".*(blackberry|opera mini).*"
	};
	private List<Pattern> agentPatterns;

	public MobileAgentService() {
		initPatterns();
	}

	private void initPatterns() {
		agentPatterns = new ArrayList<>();
		for (final String ua : mobileAgents) {
			try {
				agentPatterns.add(Pattern.compile(ua, Pattern.CASE_INSENSITIVE));
			} catch (PatternSyntaxException e) {
				logger.debug("failed to compile pattern: " + ua, e);
			}
		}
	}

	/**
	 * Returns true of the given User-Agent string matches a suspected
	 * mobile device.
	 * @param agent
	 * @return
	 */
	public boolean isMobile(final String agent) {
		boolean mobile = false;
		for (final Pattern p : agentPatterns) {
			final Matcher m = p.matcher(agent);
			if (m.find()) {
				mobile = true;
				break;
			}
		}
		return mobile;
	}
}
