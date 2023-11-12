package TestMersyIo_RestAssured._1_Setup._1_Parameters._5_Fields;

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

public class Fields extends Variables {

    Map<String,String> fields=new HashMap<>();

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
    public void createFields() {


        fieldName="field-"+faker.number().digits(3);
        fieldCode=faker.number().digits(5);
        fields.put("name",fieldName);
        fields.put("code",fieldCode);
        fields.put("type","STRING");
        fields.put("schoolId","6390f3207a3bcb6a7ac977f9");

        fieldID=
                given()
                        .spec(reqSpec)
                        .body(fields)
                        //.log().body()
                        .when()
                        .post(url+"entity-field")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Fields Test: Successfully passed !");
    }

    @Test(priority = 2,dependsOnMethods = "createFields")
    public void createFieldsNegative() {

        given()

                .spec(reqSpec)
                .body(fields)
                //.log().body()
                .when()
                .post(url+"entity-field")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));

        System.out.println("Create Fields Negative Test: Successfully passed !");


    }
    @Test(priority = 3, dependsOnMethods = "createFields")
    public void updateFields() {

        newfieldName="field-"+faker.number().digits(2);
        newfieldCode=faker.number().digits(3);
        fields.put("name",newfieldName);
        fields.put("code",newfieldCode);
        fields.put("id", fieldID);

        given()
                .spec(reqSpec)
                .body(fields)
                //.log().body()
                .when()
                .put(url+"entity-field")
                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(newfieldName));

        System.out.println("Update Fields Test: Successfully passed !");

    }

    @Test(priority = 4, dependsOnMethods = "updateFields")
    public void deleteFields()  {

        given()
                .spec(reqSpec)
                .when()
                .delete(url+"entity-field/"+ fieldID)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Fields Test: Successfully passed !");

    }

    @Test(priority = 5, dependsOnMethods = "deleteFields")
    public void deleteFieldsNegative() {

        given()
                .spec(reqSpec)
                .when()
                .delete(url+"entity-field/"+ fieldID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"));

        System.out.println("Delete Fields Negative Test: Successfully passed !");

    }
}


