package uk.mm.bobcat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.mm.bobcat.domain.NamePair;
import uk.mm.bobcat.service.EloService;

@Controller
@RequestMapping("/competition")
public class CompetitionController extends CommonDataController {
	private final Logger logger = LoggerFactory.getLogger(CompetitionController.class);
	private EloService eloService;

	@Autowired
	public CompetitionController(EloService eloService) {
		this.eloService = eloService;
	}

	@RequestMapping("")
	public ModelAndView getCompetitionPage() {
		logger.info("{} [Competition]", LMT.PAGE_REQ);
		NamePair competingPair = eloService.getCompetingPair();
		return new ModelAndView("competition", "names", competingPair);
	}

	@RequestMapping("/getnamepair")
	@ResponseBody
	public NamePair getNamePair() {
		NamePair pair = eloService.getCompetingPair();
		logger.info("{} returning competing pair: [{}]", LMT.DATA_GET, pair);
		return pair;
	}

	@RequestMapping(value = "/matchresult", method = RequestMethod.POST)
	@ResponseBody
	public String postPreferredName(String winner, String loser) {
		checkString(logger, "Winner name can't be null or empty!", winner);
		checkString(logger, "Loser name can't be null or empty!", loser);
		String winnerTrimmed = winner.trim();
		String loserTrimmed = loser.trim();
		logger.info("{} Match result: [{}] wins over [{}]", LMT.DATA_POST, winnerTrimmed, loserTrimmed);
		eloService.newCompareResult(winnerTrimmed, loserTrimmed);
		return String.format("\"%s\" selected over \"%s\"", winnerTrimmed, loserTrimmed);
	}
}