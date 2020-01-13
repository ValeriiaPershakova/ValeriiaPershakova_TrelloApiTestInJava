package core.api;

import beans.Board;
import beans.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.constants.Endpoints;
import core.constants.Requests;
import core.constants.TrelloConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

public class ListTrelloApi extends BaseAbstractApi {
    private ListTrelloApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    private Requests requestMethod = Requests.GET;
    private String listId = null;
    private boolean getCards = false;

    public static class ApiBuilder {
        private ListTrelloApi listTrelloApi;

        private ApiBuilder(ListTrelloApi api) {
            this.listTrelloApi = api;
        }

        public ApiBuilder name(String name) {
            listTrelloApi.params.put(TrelloConstants.PARAM_NAME, name);
            return this;
        }

        public ApiBuilder position(String position) {
            listTrelloApi.params.put(TrelloConstants.PARAM_POSITION, position);
            return this;
        }

        public ApiBuilder fields(String... fieldNames) {
            listTrelloApi.params.put(TrelloConstants.PARAM_FIELD, String.join(",", fieldNames));
            return this;
        }

        public ApiBuilder request(Requests req) {
            listTrelloApi.requestMethod = req;
            return this;
        }

        public ApiBuilder id(String id) {
            listTrelloApi.listId = id;
            return this;
        }

        public ApiBuilder boardId(String boardId) {
            listTrelloApi.params.put(TrelloConstants.PARAM_BOARD_ID, boardId);
            return this;
        }

        public ApiBuilder getCards() {
            listTrelloApi.getCards = true;
            return this;
        }

        public ApiBuilder closed(Boolean bool) {
            listTrelloApi.params.put(TrelloConstants.PARAM_CLOSED, bool.toString());
            return this;
        }


        public Response callApi() {
            if (listTrelloApi.listId != null) {
                if (listTrelloApi.getCards) {

                    return RestAssured.given(baseRequestConfiguration)
                            .with()
                            .queryParams(listTrelloApi.params)
                            .pathParam(TrelloConstants.PARAM_ID, listTrelloApi.listId)
                            .request(listTrelloApi.requestMethod.toString(), Endpoints.LIST + "/cards").prettyPeek();
                } else {
                    return RestAssured.given(baseRequestConfiguration)
                            .with()
                            .queryParams(listTrelloApi.params)
                            .pathParam(TrelloConstants.PARAM_ID, listTrelloApi.listId)
                            .request(listTrelloApi.requestMethod.toString(), Endpoints.LIST).prettyPeek();
                }
            } else {
                return RestAssured.given(baseRequestConfiguration)
                        .with()
                        .queryParams(listTrelloApi.params)
                        .request(listTrelloApi.requestMethod.toString(), Endpoints.LISTS).prettyPeek();
            }
        }

    }

    public static ApiBuilder with() {
        ListTrelloApi api = new ListTrelloApi();
        return new ApiBuilder(api);
    }


    public static beans.List getTrelloList(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List>() {
        }.getType());
    }

    public static java.util.List<beans.List> getAllListsOnTheBoard(String boardId, String filter) {
        String response = RestAssured.given(baseRequestConfiguration)
                .with()
                .pathParam(TrelloConstants.PARAM_ID, boardId)
                .queryParam("lists", filter)
                .get(Endpoints.BOARD).prettyPeek()
                .then()
                .specification(baseSuccessfullResponse)
                .extract().response().asString();
        Board board = new Gson().fromJson(response.trim(), Board.class);
        return board.getLists();
    }
}
