package TestMersyIo_RestAssured._1_Setup._1_Parameters._4_DocumentTypes;

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
import static org.hamcrest.Matchers.equalTo;


public class DocumentTypes extends Variables {


    String documentTypesId;

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
    public void createDocumentType() {

        documentTypesId =

                given()

                        .spec(reqSpec)
                        .body("{\n" +
                                "    \"id\":null,\n" +
                                "    \"name\": \"Test\",\n" +
                                "    \"description\": \"\",\n" +
                                "    \"attachmentStages\": [\n" +
                                "        \"STUDENT_REGISTRATION\"\n" +
                                "    ],\n" +
                                "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                                "    \"active\": true,\n" +
                                "    \"required\": true,\n" +
                                "    \"translateName\": [],\n" +
                                "    \"useCamera\": false\n" +
                                "}")
                        //.log().body()

                        .when()
                        .post(url+"attachments/create")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Document Type Test: Successfully passed !");
    }


    @Test(priority = 2,dependsOnMethods = "createDocumentType")
    public void createDocumentTypeNegative(){

        given()
                .spec(reqSpec)
                .body        ("{\n" +
                        "    \"id\":null,\n" +
                        "    \"name\": \"Test\",\n" +
                        "    \"description\": \"\",\n" +
                        "    \"attachmentStages\": [\n" +
                        "        \"Certificate\"\n" +
                        "    ],\n" +
                        "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                        "    \"active\": true,\n" +
                        "    \"required\": true,\n" +
                        "    \"translateName\": [],\n" +
                        "    \"useCamera\": false\n" +
                        "}")
                //.log().body()
                .when()
                .post(url+"attachments/create")
                .then()
                //.log().body()
                .statusCode(400)
                .extract().path("id");

        System.out.println("Create Document Type Negative Test: Successfully passed !");
    }

    @Test(priority = 3,dependsOnMethods = "createDocumentTypeNegative")
    public void updateDocumentType() {

        given()

                .spec(reqSpec)
                .body("{\n" +
                "  \"id\": \""+documentTypesId+"\",\n" +
                "  \"name\": \"entrance examination\",\n" +
                "  \"description\": \"\",\n" +
                "  \"attachmentStages\": [\n" +
                "    \"EMPLOYMENT\"\n" +
                "  ],\n" +
                "  \"active\": true,\n" +
                "  \"required\": true,\n" +
                "  \"useCamera\": false,\n" +
                "  \"translateName\": [],\n" +
                "  \"schoolId\": \"6390f3207a3bcb6a7ac977f9\"\n" +
                "}")
                 // .log().body()
                .when()
                .put("/school-service/api/attachments")

                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Update Document Type  Test: Successfully passed !");
    }

    @Test(priority = 4,dependsOnMethods = "updateDocumentType")
    public void deleteDocumentType() {

        given()
                .spec(reqSpec)
                .when()
                .delete(url+"attachments/" + documentTypesId)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete Document Type  Test: Successfully passed !");
    }


    @Test(priority = 5,dependsOnMethods = "deleteDocumentType")
    public void deleteDocumentNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete(url+"attachments/" + documentTypesId)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("Attachment Type not found"));

        System.out.println("Delete Document Type Negative Test: Successfully passed !");
    }

}

