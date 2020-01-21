package core.api;

import core.constants.Endpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import service.PropertyReader;

import java.util.Map;

import static org.hamcrest.Matchers.lessThan;

public abstract class BaseAbstractApi {
    protected static Map<String, String> creditParams = PropertyReader.getProperties(Endpoints.PATH_TO_CREDITS_FILE);
    protected static Map<String, String> url = PropertyReader.getProperties(Endpoints.PATH_TO_URL_FILE);

    public static RequestSpecification baseRequestConfiguration =
            new RequestSpecBuilder()
                    .setAccept(ContentType.JSON)
                    .setContentType(ContentType.ANY)
                    .setRelaxedHTTPSValidation()
                    .addQueryParams(creditParams)
                    .setBaseUri(url.get("baseUrl"))
                    .setBasePath(url.get("basePath"))
                    .log(LogDetail.ALL)
                    .build();

    public static ResponseSpecification baseResponse =
            new ResponseSpecBuilder()
                    .expectContentType(ContentType.JSON)
                    .expectHeader("Connection", "keep-alive")
                    .expectResponseTime(lessThan(20000L))
                    .build();

    public static ResponseSpecification baseSuccessfulResponse =
            new ResponseSpecBuilder()
                    .addResponseSpecification(baseResponse)
                    .expectStatusCode(HttpStatus.SC_OK)
                    .build();

    public static ResponseSpecification notFoundResponse =
            new ResponseSpecBuilder()
                    .addResponseSpecification(baseResponse)
                    .expectContentType(ContentType.TEXT)
                    .expectStatusCode(HttpStatus.SC_NOT_FOUND)
                    .build();
}
