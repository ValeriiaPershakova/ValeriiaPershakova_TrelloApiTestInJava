package core;

import core.constants.Endpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Properties;

import static org.hamcrest.Matchers.lessThan;

public abstract class BaseAbstractApi {
    protected static HashMap<String, String> creditParams = new HashMap<>();

    static {
        Properties credentials = Utils.getCredentialProperties(Endpoints.PATH_TO_CREDITS_FILE);
        creditParams.put("key", credentials.getProperty("key"));
        creditParams.put("token", credentials.getProperty("token"));
    }

    public static RequestSpecification baseRequestConfiguration =
            new RequestSpecBuilder()
                    .setAccept(ContentType.JSON)
                    .setContentType(ContentType.ANY)
                    .setRelaxedHTTPSValidation()
                    .addQueryParams(creditParams)
                    .log(LogDetail.ALL)
                    .build();

    public static ResponseSpecification baseSuccessfullResponse =
            new ResponseSpecBuilder()
                    .expectContentType(ContentType.JSON)
                    .expectHeader("Connection", "keep-alive")
                    .expectResponseTime(lessThan(20000L))
                    .expectStatusCode(HttpStatus.SC_OK)
                    .build();

    public static ResponseSpecification notFoundResponse =
            new ResponseSpecBuilder()
                    .expectHeader("Connection", "keep-alive")
                    .expectResponseTime(lessThan(20000L))
                    .expectStatusCode(HttpStatus.SC_NOT_FOUND)
                    .build();
}
