package TestMersyIo_RestAssured._3_HumanResources._3_PositionCategories;

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

public class PositionCategories extends Variables {

    Map<String, String> positionCategories;

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
    public void createPositionCategories() {

        positionCategories = new HashMap<>();

        CategoriesName = "Test " + faker.number().digits(5);
        positionCategories.put("name", CategoriesName);


       CategoriesID =

                given()

                        .spec(reqSpec)
                        .body(positionCategories)
                        //.log().body()
                        .when()
                        .post(url+"position-category")
                        .then()
                       //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create PositionC ategories Test: Successfully passed !");

    }

    @Test(priority = 2, dependsOnMethods = "createPositionCategories")
    public void createPositionCategoriesNegative(){

        given()

                .spec(reqSpec)
                .body(positionCategories)
                //.log().body()
                .when()
                .post(url+"position-category")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Position CategoriesNegative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createPositionCategoriesNegative")
    public void updatePositionCategories(){

        CategoriesName = "QA - " + faker.number().digits(5);
        positionCategories.put("id", CategoriesID);
        positionCategories.put("name", CategoriesName);

        given()

                .spec(reqSpec)
                .body(positionCategories)
                // .log().body()
                .when()
                .put(url+"position-category")
                .then()
                //.log().body()
                .statusCode(200);
                //.body("message", equalTo("Position Category with this name already exists"));


        System.out.println("Update Position Categories Test: Successfully passed !");
    }


    @Test(priority = 4, dependsOnMethods = "updatePositionCategories")
    public void deletePositionCategories(){

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"position-category/" + CategoriesID)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Position Categories Test: Successfully passed !");
    }

    @Test(priority = 5, dependsOnMethods = "deletePositionCategories")
    public void deletePositionCategoriesNegative(){

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"position-category/"+CategoriesID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("PositionCategory not  found"));

        System.out.println("Delete Position Categories Negative Test: Successfully passed !");
    }
}

