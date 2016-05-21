package uk.mm.bobcat.controller;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import uk.mm.bobcat.service.EloService;

public class RankingControllerTest {

    private RankingController rankingController;
    private EloService eloServiceMock;

    @Before
    public void setup() {
        eloServiceMock = EasyMock.createMock(EloService.class);
        rankingController = new RankingController(eloServiceMock);
    }

    @Test
    public void testDeleteNameFromCompetition() {
        String name = "bladidbla";
        eloServiceMock.removeNameFromCompetition(name);
        EasyMock.replay(eloServiceMock);

        rankingController.postRemoveNameFromCompetition(name);
        EasyMock.verify(eloServiceMock);
    }

    @Test
    public void testModifyNameInCompetition() {
        String originalName = "old";
        String newName = "bladidbla";
        eloServiceMock.modifyName(originalName, newName);
        EasyMock.replay(eloServiceMock);

        rankingController.postModifyNameInCompetition(originalName, newName);
        EasyMock.verify(eloServiceMock);
    }
}