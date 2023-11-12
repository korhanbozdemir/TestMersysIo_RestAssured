package TestMersyIo_RestAssured._3_HumanResources._2_Attestations;

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

public class Attestations extends Variables {


    Map<String, String> attestation;

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
    public void createAttestation() {

        attestation = new HashMap<>();

        attestationName = "Degree Certificates Attestation - " + faker.number().digits(5);
        attestation.put("name", attestationName);


        attestationID =

                given()

                        .spec(reqSpec)
                        .body(attestation)
                        //log().body()
                        .when()
                        .post(url+"attestation")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Attestation Test: Successfully passed !");
    }

    @Test(priority = 2, dependsOnMethods = "createAttestation")
    public void createAttestationNegative() {

        given()

                .spec(reqSpec)
                .body(attestation)
                //.log().body()
                .when()
                .post(url+"attestation")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Attestation Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createAttestation")
    public void updateAttestation() {

        attestationName = "Graduation Certificates Attestation - " + faker.number().digits(5);

        attestation.put("id", attestationID);
        attestation.put("name", attestationName);

        given()

                .spec(reqSpec)
                .body(attestation)
                // .log().body()
                .when()
                .put(url+"attestation")
                .then()
                //.log().body() // show incoming body as log
                .statusCode(200)
                .body("name", equalTo(attestationName));

        System.out.println("Update Attestation Test: Successfully passed !");

    }

    @Test(priority = 4, dependsOnMethods = "updateAttestation")
    public void deleteAttestation() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"attestation/" + attestationID)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Attestation Test: Successfully passed !");
    }

    @Test(priority = 5, dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"attestation/" + attestationID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("attestation not found"));

        System.out.println("Delete Attestation Negative Test: Successfully passed !");
    }
}



