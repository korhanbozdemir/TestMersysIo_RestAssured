package TestMersyIo_RestAssured._1_SubjectCategories;

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

public class SubjectCategories extends Variables {

    Map<String, String> subject;


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
    public void createSubject() {

        subject = new HashMap<>();
        subjectName = faker.country().name() + faker.number().digits(5);
        subjectCode = faker.country().countryCode2() + faker.number().digits(5);

        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        subjectID =

                given()

                        .spec(reqSpec)
                        .body(subject)
                        //.log().body()
                        .when()
                        .post(url+"subject-categories")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Subject Categories Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "createSubject")
    public void createSubjectNegative() {

        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        given()

                .spec(reqSpec)
                .body(subject)
                //.log().body()
                .when()
                .post(url+"subject-categories")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Subject Categories Negative Test: Successfully passed !");


    }

    @Test(dependsOnMethods = "createSubjectNegative")
    public void updateSubject() {

        subject.put("id", subjectID);

        subjectName = ("TechnoStudy" + faker.number().digits(5));
        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        given()

                .spec(reqSpec)
                .body(subject)
                // .log().body()
                .when()
                .put(url+"subject-categories")
                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(subjectName));

        System.out.println("Update Subject Categories  Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "updateSubject")
    public void deleteSubject() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"subject-categories/"+subjectID)

                .then()
               // .log().body()
                .statusCode(200);

        System.out.println("Delete Subject Categories  Test: Successfully passed !");
    }

    @Test(dependsOnMethods = "deleteSubject")
    public void deleteSubjectNegative() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"subject-categories/"+subjectID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("SubjectCategory not  found"));

        System.out.println("Delete Subject Categories Negative Test: Successfully passed !");
    }
}

