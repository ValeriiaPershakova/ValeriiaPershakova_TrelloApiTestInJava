import beans.Card;
import core.api.BaseAbstractApi;
import core.api.CardTrelloApi;
import core.api.ListTrelloApi;
import core.constants.Endpoints;
import core.constants.Filters;
import core.constants.Requests;
import core.constants.TestData;
import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ListTrelloApiTest {
    private static String BOARD_ID;
    private static java.util.List<beans.List> allOpenListsOnBoard;
    private static java.util.List<beans.List> allListsOnBoard;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.trello.com";
        RestAssured.basePath = "/1";
        java.util.List<String> boardsId = RestAssured.given(BaseAbstractApi.baseRequestConfiguration)
                .get(Endpoints.BOARD_ID).
                        then().log().all()
                .extract().response()
                .jsonPath()
                .getList("idBoards");
        if (boardsId != null) {
            BOARD_ID = boardsId.get(0);
        } else {
            throw new RuntimeException("Current user has no board");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void methodSetUp() {
        allOpenListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.OPEN.getParam());
        assertThat(allOpenListsOnBoard, not(empty()));
    }

    @AfterClass
    public void tearDown() {
        RestAssured.reset();
    }

    @Test
    public void addNewListOnBoardTest() {
        String listName = TestData.LIST_NAME + new Random().nextInt();
        ListTrelloApi.with()
                .request(Requests.POST)
                .name(listName)
                .boardId(BOARD_ID)
                .position("top")
                .callApi()
                .then()
                .specification(BaseAbstractApi.baseSuccessfullResponse);
        allOpenListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.OPEN.getParam());
        assertThat(allOpenListsOnBoard, hasItem(hasProperty("name", equalTo(listName))));
    }

    @Test
    public void changeListPositionTest() {
        beans.List list = allOpenListsOnBoard.get(0);
        Integer initialPosition = list.getPos();

        beans.List updatedList = ListTrelloApi.getTrelloList(
                ListTrelloApi.with()
                .request(Requests.PUT)
                .id(list.getId())
                .position("bottom")
                .callApi()
                .then()
                .specification(BaseAbstractApi.baseSuccessfullResponse)
                .extract().response());
        assertThat(updatedList.getPos(), greaterThan(initialPosition));
    }

    @Test
    public void addNewCardOnTheListTest() {
        String cardName = TestData.CARD_NAME + new Random().nextInt();
        CardTrelloApi.with()
                .request(Requests.POST)
                .name(cardName)
                .listId(allOpenListsOnBoard.get(0).getId())
                .callApi()
                .then().specification(BaseAbstractApi.baseSuccessfullResponse)
                .extract().response();

        java.util.List<Card> cardOnTheList = CardTrelloApi.getCardsOnTheList(allOpenListsOnBoard.get(0).getId());

        assertThat(cardOnTheList, hasItem(hasProperty("name", equalTo(cardName))));
    }

    @Test
    public void moveCardOnListTest() {
        for (int i = 0; i < allOpenListsOnBoard.size(); i++) {
            java.util.List<Card> cardsOnTheList = CardTrelloApi.getCardsOnTheList(allOpenListsOnBoard.get(i).getId());
            if (cardsOnTheList != null && cardsOnTheList.size() >= 2) {
                Card card = cardsOnTheList.get(cardsOnTheList.size() - 1);
                Double initPosition = card.getPos();
                Card modifiedCard = CardTrelloApi.getTrelloCard(
                        CardTrelloApi.with()
                        .request(Requests.PUT)
                        .id(card.getId())
                        .position("top")
                        .callApi()
                        .then().specification(BaseAbstractApi.baseSuccessfullResponse)
                        .extract().response());

                assertThat(modifiedCard.getPos(), lessThan(initPosition));
                break;
            } else if (i == allOpenListsOnBoard.size() - 1) {
                assertThat("there is no list with 2 or more cards", false);
            }
        }
    }

    @Test
    public void deleteCardTest() {
        for (int i = 0; i < allOpenListsOnBoard.size(); i++) {
            java.util.List<Card> cardsOnTheList = CardTrelloApi.getCardsOnTheList(allOpenListsOnBoard.get(i).getId());
            if (cardsOnTheList != null) {
                CardTrelloApi.with()
                        .request(Requests.DELETE)
                        .id(cardsOnTheList.get(0).getId())
                        .callApi()
                        .then().specification(BaseAbstractApi.baseSuccessfullResponse)
                        .extract().response();
                assertThat(CardTrelloApi.getCardsOnTheList(allOpenListsOnBoard.get(i).getId()), hasSize(lessThan(cardsOnTheList.size())));
                break;
            } else if (i == allOpenListsOnBoard.size() - 1) {
                assertThat("there is no list with cards", false);
            }
        }
    }

    @Test
    public void archiveListTest() {
        beans.List modifiedList = ListTrelloApi.getTrelloList(ListTrelloApi.with()
                .request(Requests.PUT)
                .id(allOpenListsOnBoard.get(0).getId())
                .closed(true)
                .callApi()
                .then().specification(BaseAbstractApi.baseSuccessfullResponse)
                .extract().response());
        allListsOnBoard = ListTrelloApi.getAllListsOnTheBoard(BOARD_ID, Filters.ALL.getParam());
        for (beans.List list : allListsOnBoard) {
            if (list.getId().equals(modifiedList.getId())) {
                assertThat(modifiedList.getClosed(), is(true));
                break;
            }
        }

    }

    @Test
    public void deleteListTest() {
        ListTrelloApi.with()
                .request(Requests.DELETE)
                .id(allOpenListsOnBoard.get(0).getId())
                .callApi()
                .then().specification(BaseAbstractApi.notFoundResponse);
    }
}
