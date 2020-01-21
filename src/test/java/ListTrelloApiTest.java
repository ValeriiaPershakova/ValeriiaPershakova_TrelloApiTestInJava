import beans.Card;
import core.api.BaseAbstractApi;
import core.api.BoardTrelloApi;
import core.api.CardTrelloApi;
import core.api.ListTrelloApi;
import core.constants.Filters;
import core.constants.TrelloConstants;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListTrelloApiTest {
    private static String BOARD_ID;
    private static java.util.List<beans.List> allOpenListsOnBoard;
    private static java.util.List<beans.List> allListsOnBoard;
    private static java.util.List<String> allCreatedBoards = new ArrayList<>();


    @BeforeMethod(alwaysRun = true)
    public void methodSetUp() {
        BOARD_ID = BoardTrelloApi.createBoard();
        allCreatedBoards.add(BOARD_ID);
        ListTrelloApi.createListsOnBoard(BOARD_ID, 3);
        allOpenListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.OPEN.getParam());
        assertThat(allOpenListsOnBoard, not(empty()));
    }

    @AfterClass
    public void tearDown() {
        System.out.println();
        BoardTrelloApi.deleteBoards(allCreatedBoards);
        RestAssured.reset();
    }

    @Test
    public void addNewListOnBoardTest() {
        String id = ListTrelloApi.createListsOnBoard(BOARD_ID, 1).get(0);

        allOpenListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.OPEN.getParam());
        assertThat(allOpenListsOnBoard, hasItem(hasProperty(TrelloConstants.PARAM_ID, equalTo(id))));
    }

    @Test
    public void changeListPositionTest() {
        beans.List list = allOpenListsOnBoard.get(0);
        Integer initialPosition = list.getPos();
        Response response = ListTrelloApi.with()
                .request(Method.PUT)
                .path(format("/%s", list.getId()))
                .position("bottom")
                .callApi()
                .then()
                .specification(BaseAbstractApi.baseSuccessfulResponse)
                .extract().response();
        beans.List updatedList = ListTrelloApi.getTrelloList(response);

        assertThat(updatedList.getPos(), greaterThan(initialPosition));
    }

    @Test
    public void addNewCardOnTheListTest() {
        String listId = (allOpenListsOnBoard.get(0).getId());
        String cardId = CardTrelloApi.createCardsOnList(listId, 1).get(0);
        java.util.List<Card> cardOnTheList = CardTrelloApi.getCardsOnTheList(listId);

        assertThat(cardOnTheList, hasItem(hasProperty(TrelloConstants.PARAM_ID, equalTo(cardId))));
    }

    @Test
    public void moveCardOnListTest() {
        String listId = (allOpenListsOnBoard.get(0).getId());
        CardTrelloApi.createCardsOnList(listId, 3);
        java.util.List<Card> cardsOnTheList = CardTrelloApi.getCardsOnTheList(listId);

        Card card = cardsOnTheList.get(0);
        Double initPosition = card.getPos();
        Response response = CardTrelloApi.with()
                .request(Method.PUT)
                .path(format("/%s", card.getId()))
                .position("bottom")
                .callApi();
        response
                .then().specification(BaseAbstractApi.baseSuccessfulResponse);
        Card modifiedCard = CardTrelloApi.getTrelloCard(response);

        assertThat(modifiedCard.getPos(), greaterThan(initPosition));

    }

    @Test
    public void deleteCardTest() {
        String listId = (allOpenListsOnBoard.get(0).getId());
        CardTrelloApi.createCardsOnList(listId, 2);
        java.util.List<Card> cardsOnTheList = CardTrelloApi.getCardsOnTheList(listId);
        String deletedCardId = cardsOnTheList.get(0).getId();
        CardTrelloApi.with()
                .request(Method.DELETE)
                .path(format("/%s", cardsOnTheList.get(0).getId()))
                .callApi()
                .then().specification(BaseAbstractApi.baseSuccessfulResponse);
        assertThat(CardTrelloApi.getCardsOnTheList(listId), not(hasItem(hasProperty(TrelloConstants.PARAM_ID, equalTo(deletedCardId)))));

    }

    @Test
    public void archiveListTest() {
        Response response = ListTrelloApi.with()
                .request(Method.PUT)
                .path(format("/%s", allOpenListsOnBoard.get(0).getId()))
                .closed(true)
                .callApi()
                .then().specification(BaseAbstractApi.baseSuccessfulResponse)
                .extract().response();
        beans.List modifiedList = ListTrelloApi.getTrelloList(response);
        allListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.ALL.getParam());
        assertThat(allListsOnBoard, hasItem(hasProperty(TrelloConstants.PARAM_CLOSED, equalTo(true))));

    }

    @Test
    public void impossibleToDeleteListTest() {
        String deletedListid = allOpenListsOnBoard.get(0).getId();
        ListTrelloApi.with()
                .request(Method.DELETE)
                .path(format("/%s", deletedListid))
                .callApi()
                .then().specification(BaseAbstractApi.notFoundResponse);
        allOpenListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.ALL.getParam());
        assertThat(allOpenListsOnBoard, (hasItem(hasProperty(TrelloConstants.PARAM_ID, equalTo(deletedListid)))));
    }
}
