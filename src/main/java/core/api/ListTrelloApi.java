package core.api;

import beans.Board;
import beans.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import core.constants.Endpoints;
import core.constants.TestData;
import core.constants.TrelloConstants;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class ListTrelloApi extends BaseAbstractApi {
    private ListTrelloApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    private Method requestMethod = Method.GET;
    private String path = Endpoints.LISTS;

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

        public ApiBuilder request(Method req) {
            listTrelloApi.requestMethod = req;
            return this;
        }

        //set id as query parameter. To address exact card (= set as path parameters) change path: ApiBuilder.path("/{list id}")
        public ApiBuilder id(String id) {
            listTrelloApi.params.put(TrelloConstants.PARAM_ID, id);
            return this;
        }

        public ApiBuilder boardId(String boardId) {
            listTrelloApi.params.put(TrelloConstants.PARAM_BOARD_ID, boardId);
            return this;
        }

        public ApiBuilder closed(Boolean bool) {
            listTrelloApi.params.put(TrelloConstants.PARAM_CLOSED, bool.toString());
            return this;
        }

        public ApiBuilder path(String path) {
            listTrelloApi.path = listTrelloApi.path + path;
            return this;
        }

        public ApiBuilder param(String key, String value) {
            listTrelloApi.params.put(key, value);
            return this;
        }


        public Response callApi() {
            return RestAssured.given(baseRequestConfiguration)
                    .with()
                    .queryParams(listTrelloApi.params)
                    .request(listTrelloApi.requestMethod, listTrelloApi.path).prettyPeek();

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
        JsonObject response = RestAssured.given(baseRequestConfiguration)
                .with()
                .pathParam(TrelloConstants.PARAM_ID, boardId)
                .queryParam("lists", filter)
                .get(Endpoints.BOARD)
                .prettyPeek()
                .then()
                .specification(baseSuccessfulResponse)
                .extract().body().as(JsonObject.class);
        System.out.println(response);
        Board board = new Gson().fromJson(response, Board.class);
        return board.getLists();
    }

    public static java.util.List<String> createListsOnBoard(String boardId, int listQuantity) {
        java.util.List<String> listsId = new ArrayList<>();
        for (int i = 0; i < listQuantity; i++) {
            String listName = TestData.LIST_NAME + i;
            ValidatableResponse response = ListTrelloApi.with()
                    .request(Method.POST)
                    .name(listName)
                    .boardId(boardId)
                    .callApi()
                    .then()
                    .specification(BaseAbstractApi.baseSuccessfulResponse);
            String listId = response.extract().body()
                    .jsonPath()
                    .get(TrelloConstants.PARAM_ID);
            listsId.add(listId);
        }
        return listsId;
    }
}
