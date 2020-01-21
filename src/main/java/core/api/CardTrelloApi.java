package core.api;

import beans.Card;
import com.google.gson.Gson;
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

public class CardTrelloApi extends BaseAbstractApi {
    private CardTrelloApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    private Method requestMethod = Method.GET;
    private String path = Endpoints.CARDS;

    public static class ApiBuilder {
        private CardTrelloApi cardTrelloApi;

        private ApiBuilder(CardTrelloApi api) {
            this.cardTrelloApi = api;
        }

        public ApiBuilder name(String name) {
            cardTrelloApi.params.put(TrelloConstants.PARAM_NAME, name);
            return this;
        }

        public ApiBuilder position(String position) {
            cardTrelloApi.params.put(TrelloConstants.PARAM_POSITION, position);
            return this;
        }

        public ApiBuilder listId(String listId) {
            cardTrelloApi.params.put(TrelloConstants.PARAM_LIST_ID, listId);
            return this;
        }

        public ApiBuilder fields(String... fieldNames) {
            cardTrelloApi.params.put(TrelloConstants.PARAM_FIELD, String.join(",", fieldNames));
            return this;
        }

        public ApiBuilder request(Method req) {
            cardTrelloApi.requestMethod = req;
            return this;
        }

        public ApiBuilder path(String path) {
            cardTrelloApi.path = cardTrelloApi.path + path;
            return this;
        }

        //set id as query parameter. To address exact card (= set as path parameters) change path: ApiBuilder.path("/{card id}")
        public ApiBuilder id(String id) {
            cardTrelloApi.params.put(TrelloConstants.PARAM_ID, id);
            return this;
        }

        public ApiBuilder param(String key, String value) {
            cardTrelloApi.params.put(key, value);
            return this;
        }


        public Response callApi() {

                return RestAssured.given(baseRequestConfiguration)
                        .with()
                        .queryParams(cardTrelloApi.params)
                        .request(cardTrelloApi.requestMethod, cardTrelloApi.path).prettyPeek();

        }
    }

    public static ApiBuilder with() {
        CardTrelloApi api = new CardTrelloApi();
        return new ApiBuilder(api);
    }

    public static java.util.List<beans.Card> getTrelloCards(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<java.util.List<Card>>() {
        }.getType());
    }

    public static Card getTrelloCard(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<Card>() {
        }.getType());
    }

    public static java.util.List<Card> getCardsOnTheList(String listId) {
        return getTrelloCards(
                ListTrelloApi.with()
                        .request(Method.GET)
                        .path(String.format("/%s/cards", listId))
                        .callApi()
                        .then().specification(BaseAbstractApi.baseSuccessfulResponse)
                        .extract().response());
    }

    public static java.util.List<String> createCardsOnList(String listId, int cardsQuantity) {
        java.util.List<String> cardsId = new ArrayList<>();
        for (int i = 0; i < cardsQuantity; i++) {
            String cardName = TestData.CARD_NAME + i;
            ValidatableResponse response = CardTrelloApi.with()
                    .request(Method.POST)
                    .name(cardName)
                    .listId(listId)
                    .callApi()
                    .then().specification(BaseAbstractApi.baseSuccessfulResponse);
            String id = response
                    .extract().body()
                    .jsonPath()
                    .get(TrelloConstants.PARAM_ID);
            cardsId.add(id);
        }
        return cardsId;
    }


}
