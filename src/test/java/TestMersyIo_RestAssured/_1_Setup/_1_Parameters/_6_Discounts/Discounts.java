package TestMersyIo_RestAssured._1_Setup._1_Parameters._6_Discounts;

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

public class Discounts extends Variables {


    Map<String, String> discount;

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
    public void createDiscounts() {
        discount = new HashMap<>();
        discountDescription = faker.nation().nationality() + faker.number().digits(5);
        discountCode = faker.code().asin() + faker.number().digits(5);

        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        discountID =
                given()

                        .spec(reqSpec)
                        .body(discount)
                        //.log().body()
                        .when()
                        .post(url+"discounts")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Discounts Test: Successfully passed !");

    }

    @Test(priority = 2,dependsOnMethods = "createDiscounts")
    public void createDiscountsNegative() {
        discount.put("description", discountDescription);
        discount.put("code", discountCode);


        given()

                .spec(reqSpec)
                .body(discount)
                //.log().body()
                .when()
                .post(url+"discounts")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Discount Negative Test: Successfully passed !");

    }

    @Test(priority = 3,dependsOnMethods = "createDiscountsNegative")
    public void updateDiscounts() {
        discount.put("id", discountID);

        discountDescription = ("Test" + faker.number().digits(5));
        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        given()

                .spec(reqSpec)
                .body(discount)
                // .log().body()
                .when()
                .put(url+"discounts")
                .then()
                //.log().body()
                .statusCode(200)
                .body("description", equalTo(discountDescription));

        System.out.println("Update Discount Test: Successfully passed !");

    }

    @Test(priority = 4,dependsOnMethods = "updateDiscounts")
    public void deleteDiscounts() {
        given()

                .spec(reqSpec)
                .when()
                .delete(url+"discounts/"+discountID)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete Discount Test: Successfully passed !");
    }

    @Test(priority = 5,dependsOnMethods = "deleteDiscounts")
    public void deleteDiscountsNegative() {
        given()

                .spec(reqSpec)
                .when()
                .delete(url+"discounts/"+discountID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("Discount not found"));

        System.out.println("Delete Discounts Negative Test: Successfully passed !");

    }
}
