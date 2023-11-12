package TestMersyIo_RestAssured._1_Setup._1_Parameters._3_GradeLevels;

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

public class GradeLevels extends Variables {

    Map<String, String> gradeLevel;

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
    @Test
    public void createGradeLevel() {

        gradeLevel = new HashMap<>();
        gradelevelName = faker.name().firstName() + faker.number().digits(5);
        gradelevelShortName = faker.name().lastName() + faker.number().digits(5);
        gradeLevel.put("name", gradelevelName);
        gradeLevel.put("shortName", gradelevelShortName);

        gradelevelID =

                given()
                        .spec(reqSpec)
                        .body(gradeLevel)
                        //.log().body()
                        .when()
                        .post(url+"grade-levels")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Grade Level Test: Successfully passed !");

    }


    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {

        gradeLevel.put("name", gradelevelName);
        gradeLevel.put("shortName", gradelevelShortName);

        given()

                .spec(reqSpec)
                .body(gradeLevel)
                //.log().body()
                .when()
                .post(url+"grade-levels")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Grade Level Negative Test: Successfully passed !");

    }


    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {

        gradeLevel.put("id", gradelevelID);
        gradelevelName = ("Test" + faker.number().digits(5));
        gradeLevel.put("name", gradelevelName);
        gradeLevel.put("shortName", gradelevelShortName);

        given()

                .spec(reqSpec)
                .body(gradeLevel)
                // .log().body()
                .when()
                .put(url+"grade-levels")
                .then()
                //.log().body()
                .statusCode(200)
                .body("id", equalTo(gradelevelID));

        System.out.println("Update Grade Level Test: Successfully passed !");

    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"grade-levels/"+gradelevelID)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete Grade Level Test: Successfully passed !");

    }

    @Test(priority = 5,dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative() {
        given()

                .spec(reqSpec)
                .when()
                .delete(url+"grade-levels/"+gradelevelID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("Grade Level not found."));

        System.out.println("Delete Grade Level Negative Test: Successfully passed !");
    }
}