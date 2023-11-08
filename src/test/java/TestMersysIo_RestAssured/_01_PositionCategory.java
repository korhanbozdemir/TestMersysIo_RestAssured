package TestMersysIo_RestAssured;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class _01_PositionCategory {


    Faker random=new Faker();
    RequestSpecification reqSpec;

    String PosCatID="";

    String rndPosCatName="";


    @BeforeClass

    public void  Login (){

        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public  void createPositionCat(){

        rndPosCatName= random.address().firstName();

        Map<String,String> FirstName= new HashMap<>();
        FirstName.put("name", rndPosCatName);

        PosCatID=
                given()
                        .spec(reqSpec)
                        .body(FirstName)
                        .when()
                        .post("school-service/api/position-category")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        ;
    }

    @Test (dependsOnMethods = "createPositionCat")
    public void updatePositionCat(){
        String newFirstName=random.address().firstName();
        Map<String,String> updateName=new HashMap<>();
        updateName.put("id",PosCatID);
        updateName.put("name",newFirstName);


        given()
                .spec(reqSpec)
                .body(updateName)

                .when()
                .put("school-service/api/position-category")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newFirstName))
        ;

    }


    @Test(dependsOnMethods = "updatePositionCat")
    public void deletePositionCat(){
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/position-category/"+PosCatID)

                .then()
                .log().body()
                .statusCode(204)
        ;

    }



}
