package uk.mm.bobcat.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.mm.bobcat.domain.Rating;
import uk.mm.bobcat.service.EloService;

@Controller
@RequestMapping("/ranking")
public class RankingController extends CommonDataController {
	private final Logger logger = LoggerFactory.getLogger(RankingController.class);
	private EloService eloService;

	@Autowired
	public RankingController(EloService eloService) {
		this.eloService = eloService;
	}

	@RequestMapping("")
	public ModelAndView getNameRanking() {
		logger.info("{} [Ranking]", LMT.PAGE_REQ);
		List<Rating> ranking = eloService.getRatings();
		return new ModelAndView("ranking", "ranking", ranking);
	}

	@RequestMapping(value = "/add_name", method = RequestMethod.POST)
	@ResponseBody
	public void postAddNameToCompetition(String name) {
		checkString(logger, "Name can't be null or empty!", name);
		logger.info("{} Adding name to competition: [{}]", LMT.DATA_POST, name.trim());
		eloService.addNameInCompetition(name.trim());
	}

	@RequestMapping(value = "/remove_name", method = RequestMethod.POST)
	public String postRemoveNameFromCompetition(@RequestParam String name) {
		checkString(logger, "Name can't be null or empty!", name);
		logger.info("{} Removing name from competition: [{}]", LMT.DATA_POST, name);
		eloService.removeNameFromCompetition(name);
		return "redirect:/";
	}

	@RequestMapping(value = "/modify_name", method = RequestMethod.POST)
	public String postModifyNameInCompetition(String originalName, String newName) {
		checkString(logger, "Original name can't be null or empty!", originalName);
		checkString(logger, "New name can't be null or empty!", newName);
		logger.info("{} Modify name in competition: [{}] --> [{}]", LMT.DATA_POST, originalName, newName);
		eloService.modifyName(originalName, newName);
		return "redirect:/";
	}
}