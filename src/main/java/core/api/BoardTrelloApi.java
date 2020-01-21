package core.api;

import core.constants.Endpoints;
import core.constants.TrelloConstants;
import io.restassured.RestAssured;
import io.restassured.http.Method;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardTrelloApi extends BaseAbstractApi {
    /**
     * Method create new Board with 3 default lists
     *
     * @return new board ID
     */
    public static String createBoard() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(TrelloConstants.PARAM_NAME, "Board " + new Date().toString());
        queryParams.put("defaultLists", "false");

        String boardId = RestAssured.with()
                .spec(baseRequestConfiguration)
                .queryParams(queryParams)
                .request(Method.POST, Endpoints.BOARDS)
                .prettyPeek()
                .then()
                .specification(baseResponse)
                .extract().body()
                .jsonPath()
                .get("id");
        return boardId;
    }

    public static void deleteBoards(List<String> boardsId) {
        for (String board : boardsId) {
            RestAssured.with()
                    .spec(baseRequestConfiguration)
                    .pathParam(TrelloConstants.PARAM_ID, board)
                    .request(Method.DELETE, Endpoints.BOARDS + "{id}")
                    .prettyPeek()
                    .then()
                    .specification(baseSuccessfulResponse);
        }
    }
}
