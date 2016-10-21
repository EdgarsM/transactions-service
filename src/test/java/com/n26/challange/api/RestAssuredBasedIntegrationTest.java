package com.n26.challange.api;

import static com.n26.challange.api.JerseyConfig.*;
import static com.n26.challange.api.TransactionsResource.*;
import static io.restassured.config.JsonConfig.*;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class RestAssuredBasedIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TransactionPreconditions transactionPreconditions;

    @BeforeClass
    public static void setUpRestAssured() {
        RestAssured.basePath = APPLICATION_PATH + RESOURCE_PATH;
        RestAssured.config = RestAssuredConfig.newConfig().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    }

    @Before
    public void setUpPort() {
        RestAssured.port = port;
    }
}
