package uk.mm.bobcat.controller;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.mm.bobcat.service.NameService;

@Controller
public class NameOfTheDayController extends CommonDataController {
	private final Logger logger = LoggerFactory.getLogger(NameOfTheDayController.class);
	private NameService nameService;

	@Autowired
	public NameOfTheDayController(NameService nameService) {
		this.nameService = nameService;
	}

	@RequestMapping("/notd")
	public ModelAndView nameOfTheDayPage() {
		logger.info("{} [Name Of The Day]", LMT.PAGE_REQ);
		String notd = nameService.getNameOfTheDay(new GregorianCalendar());
		return new ModelAndView("notd", "name", notd);
	}

	@RequestMapping("/randomName")
	public ModelAndView newRandomName() {
		String randomName = nameService.getRandomName();
		logger.info("{} returning random name: [{}]", LMT.DATA_GET, randomName);
		return new ModelAndView("fragments/name_candidate", "name", randomName);
	}
}