package TestMersyIo_RestAssured._1_Setup._2_SchoolSetup._2_Locations;

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

public class Locations extends Variables {

    Map<String, String> SchoolLocation;

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
    public void CreateSchoolLocations() {

        SchoolLocation = new HashMap<>();

        SchoolLocationName = faker.name().firstName() + faker.number().digits(5);
        SchoolLocationShortName = faker.name().lastName() + faker.number().digits(3);
        SchoolLocationCapacity = faker.number().digits(5);

        SchoolLocation.put("name", SchoolLocationName);
        SchoolLocation.put("shortName", SchoolLocationShortName);
        SchoolLocation.put("capacity", SchoolLocationCapacity);
        SchoolLocation.put("type", "LABORATORY");
        SchoolLocation.put("school", "6390f3207a3bcb6a7ac977f9");

        SchoolLocationID =

                given()

                        .spec(reqSpec)
                        .body(SchoolLocation)
                        //.log().body()
                        .when()
                        .post(url+"location")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create School Location Test: Successfully passed !");
    }

    @Test(priority = 2, dependsOnMethods = "CreateSchoolLocations")
    public void createSchoolLocationsNegative() {

        SchoolLocation.put("name", SchoolLocationName);
        SchoolLocation.put("shortName", SchoolLocationShortName);

        given()

                .spec(reqSpec)
                .body(SchoolLocation)
                //.log().body()
                .when()
                .post(url+"location")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create School Location Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createSchoolLocationsNegative")
    public void updateSchoolLocations() {

        SchoolLocation.put("id", SchoolLocationID);
        SchoolLocationName = ("Test" + faker.number().digits(5));
        SchoolLocation.put("name", SchoolLocationName);
        SchoolLocation.put("shortName", SchoolLocationShortName);

        given()

                .spec(reqSpec)
                .body(SchoolLocation)
                // .log().body()
                .when()
                .put(url+"location")
                .then()
                //.log().body()
                .statusCode(200)
                .body("id", equalTo(SchoolLocationID));

        System.out.println("Update School Location Test: Successfully passed !");

    }

    @Test(priority = 4, dependsOnMethods = "updateSchoolLocations")
    public void deleteSchoolLocations() {

        given()

                .spec(reqSpec)
                //.log().uri()
                .when()
                .delete(url+"location/"+SchoolLocationID)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete School Location Test: Successfully passed !");
    }

    @Test(priority = 5, dependsOnMethods = "deleteSchoolLocations")
    public void deleteSchoolLocationNegative() {
        given()

                .spec(reqSpec)
                //.log().uri()
                .when()
                .delete(url+"location/"+SchoolLocationID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("School Location not found"));

        System.out.println("Delete School Location Negative Test: Successfully passed !");
    }
}
