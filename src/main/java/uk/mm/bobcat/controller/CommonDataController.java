package uk.mm.bobcat.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import uk.mm.bobcat.service.MobileAgentService;
import uk.mm.bobcat.service.NameService;

@Controller
public class CommonDataController {
	private static final Logger logger = LoggerFactory.getLogger(CommonDataController.class);
	private static final String USER_AGENT_HEADER = "User-Agent";

	private NameService nameService;
	private MobileAgentService mobileAgentService;

	@Autowired
	public void setNameService(NameService nameService) {
		this.nameService = nameService;
	}

	@Autowired
	public void setMobileAgentService(MobileAgentService mobileAgentService) {
		this.mobileAgentService = mobileAgentService;
	}

	@ModelAttribute("nameCount")
	public long nameCount() {
		return nameService.getNameCount();
	}

	@ModelAttribute("isMobile")
	public boolean isMobile(HttpServletRequest request) {
		final String userAgent = request.getHeader(USER_AGENT_HEADER);
		if (userAgent != null) {
			logger.debug("USER AGENT: {}", userAgent);
			return mobileAgentService.isMobile(userAgent);
		}
		logger.debug("USER AGENT not provided.", userAgent);
		return false;
	}

	protected void checkString(Logger log, String msg, String... data) {
		for (String dataItem : data) {
			if (StringUtils.isBlank(dataItem)) {
				log.error(msg);
				throw new IllegalArgumentException(msg);
			}
		}
	}
}
