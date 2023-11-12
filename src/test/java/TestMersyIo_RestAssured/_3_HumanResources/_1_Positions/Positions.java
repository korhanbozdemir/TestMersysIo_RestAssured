package TestMersyIo_RestAssured._3_HumanResources._1_Positions;

import TestMersyIo_RestAssured._0_Variables.Variables;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Positions extends Variables {

    Map<String, String> positions = new HashMap<>();

    @BeforeClass
    public void Setup(){

        baseURI = "https://test.mersys.io/";
        Map<String, String> userCredential=new HashMap<>();
        userCredential.put("username",username);
        userCredential.put("password",password);
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post(urlLog)
                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

        System.out.println("Login Test: Successfully passed !");

    }

    @Test(priority = 1)
    public void createPositions() {
        positionsName = "Test1" + faker.number().digits(5);
        positionsShort = "Test" + faker.number().digits(5);
        positions.put("name", positionsName);
        positions.put("shortName", positionsShort);
        positions.put("tenantId", "6390ef53f697997914ec20c2");
        positions.put("active", "true");

        positionsID =

                given()

                        .spec(reqSpec)
                        .body(positions)
                        //.log().body()
                        .when()
                        .post(url+"employee-position")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Positions Test: Successfully passed !");

    }

    @Test(priority = 2, dependsOnMethods = "createPositions")
    public void createPositionsNegative() {

        given()

                .spec(reqSpec)
                .body(positions)
                //.log().body()
                .when()
                .post(url+"employee-position")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Positions Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createPositionsNegative")
    public void updatePositions() {

        positions.put("id", positionsID);
        positionsName = ("Techno" + faker.number().digits(5));
        positions.put("name", positionsName);
        positions.put("shortName", positionsShort);

        given()

                .spec(reqSpec)
                .body(positions)
                // .log().body()
                .when()
                .put(url+"employee-position/")
                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(positionsName));

        System.out.println("Update Positions Test: Successfully passed !");
    }

    @Test(priority = 4, dependsOnMethods = "updatePositions")
    public void deletePositions() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"employee-position/"+positionsID)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Positions Test: Successfully passed !");

    }

    @Test(priority = 5, dependsOnMethods = "deletePositions")
    public void deletePositionsNegative() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"employee-position/"+positionsID)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Positions Negative Test: Successfully passed !");
    }
}