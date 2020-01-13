package core.api;

import beans.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.constants.Endpoints;
import core.constants.Requests;
import core.constants.TrelloConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

public class CardTrelloApi extends BaseAbstractApi {
    private CardTrelloApi() {
    }

    private HashMap<String, String> params = new HashMap<>();
    private Requests requestMethod = Requests.GET;
    private String cardId = null;

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

        public ApiBuilder request(Requests req) {
            cardTrelloApi.requestMethod = req;
            return this;
        }

        public ApiBuilder id(String id) {
            cardTrelloApi.cardId = id;
            return this;
        }


        public Response callApi() {

            if (cardTrelloApi.cardId != null) {
                return RestAssured.given(baseRequestConfiguration)
                        .with()
                        .queryParams(cardTrelloApi.params)
                        .pathParam(TrelloConstants.PARAM_ID, cardTrelloApi.cardId)
                        .request(cardTrelloApi.requestMethod.toString(), Endpoints.CARD).prettyPeek()
                        .then()
                        .specification(baseSuccessfullResponse)
                        .extract().response();
            } else {
                return RestAssured.given(baseRequestConfiguration)
                        .with()
                        .queryParams(cardTrelloApi.params)
                        .request(cardTrelloApi.requestMethod.toString(), Endpoints.CARDS).prettyPeek();
            }
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
        return getTrelloCards(ListTrelloApi.with()
                .request(Requests.GET)
                .id(listId)
                .getCards()
                .callApi()
                .then().specification(BaseAbstractApi.baseSuccessfullResponse)
                .extract().response());
    }


}
