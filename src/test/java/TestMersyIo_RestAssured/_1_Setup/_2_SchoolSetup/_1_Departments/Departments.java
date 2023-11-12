package TestMersyIo_RestAssured._1_Setup._2_SchoolSetup._1_Departments;

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

public class Departments extends Variables {

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
    public void createDepartments() {
        Map<String,String> departments = new HashMap<>();
        departmentsName=faker.country().countryCode2()+faker.number().digits(3);
        departments.put("name", departmentsName);
        departments.put("code", faker.number().digits(4));
        departments.put("school","6390f3207a3bcb6a7ac977f9");

        departmentsId=
                given()
                        .spec(reqSpec)
                        .body(departments)
                        //.log().body()
                        .when()
                        .post(url+"department")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Department Test: Successfully passed !");
    }
    @Test(priority = 2,dependsOnMethods = "createDepartments")
    public void createDepartmentsNegative(){


        Map<String,String> departments = new HashMap<>();
        departments.put("name", departmentsName);
        departments.put("code", faker.number().digits(4));
        departments.put("school","6390f3207a3bcb6a7ac977f9");


        given()
                .spec(reqSpec)
                .body(departments)
                //.log().body()
                .when()
                .post(url+"department")
                .then()
                //.log().body()
                .statusCode(400)
                .extract().path("id");

        System.out.println("Create Department Negative Test: Successfully passed !");
    }

    @Test (priority = 3,dependsOnMethods = "createDepartmentsNegative")
    public void updateParameters()
    {

        Map<String,String> departments = new HashMap<>();
        departmentsName="departName"+faker.number().digits(3);
        departments.put("name", departmentsName);
        departments.put("id", departmentsId);
        departments.put("code", faker.number().digits(4));
        departments.put("school","6390f3207a3bcb6a7ac977f9");


        given()
                .spec(reqSpec)
                .body(departments)
                //.log().body()
                .when()
                .put(url+"department")
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Update Department Test: Successfully passed !");

    }

    @Test(priority = 4,dependsOnMethods = "updateParameters")
    public void deleteParameters()    {

        given()
                .spec(reqSpec)
                .when()
                .delete(url+"department/"+departmentsId)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Department Test: Successfully passed !");

    }

    @Test(priority = 5,dependsOnMethods = "deleteParameters")
    public void deleteDepartmentsNegative(){
        given()

                .spec(reqSpec)
                .when()
                .delete(url+"department/"+departmentsId)
                .then()
                //.log().body()
                .statusCode(204);

        System.out.println("Delete Department Negative Test: Successfully passed !");
    }
}