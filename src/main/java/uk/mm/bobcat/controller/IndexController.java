package uk.mm.bobcat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends CommonDataController {
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping({ "/" })
	public String getIndex() {
		logger.info("{} [Index]", LMT.PAGE_REQ);
		return "index";
	}
}