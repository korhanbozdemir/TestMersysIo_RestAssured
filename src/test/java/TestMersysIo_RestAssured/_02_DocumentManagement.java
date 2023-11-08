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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _02_DocumentManagement {


RequestSpecification reqspec;
String attestionsId="";
Faker faker=new Faker();

    @BeforeClass
    public void Setup(){

        baseURI = "https://test.mersys.io/";

        Map<String,String> userCredential=new HashMap<>();
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
                .statusCode(200)
                .extract().response().getDetailedCookies();

        reqspec=new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void CreatAttestions(){

        String name=faker.name().firstName();

        Map<String,String> creatAttestion=new HashMap<>();
        creatAttestion.put("name",name);

        attestionsId=
        given()
                .spec(reqspec)
                .body(creatAttestion)


                .when()
                .post("/school-service/api/attestation")



                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

        ;
    }

    @Test(dependsOnMethods = "CreatAttestions")
    public void Editattestions(){

        String newAttestionsName=faker.name().firstName();
        Map<String,String> newAttestionsEdit=new HashMap<>();
        newAttestionsEdit.put("id",attestionsId);
        newAttestionsEdit.put("name",newAttestionsName);

        given()
                .spec(reqspec)
                .body(newAttestionsEdit)


                .when()
                .put("/school-service/api/attestation")


                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(newAttestionsName))


        ;


    }

    @Test(dependsOnMethods = "Editattestions")
    public void DeleteAttestions(){

        given()
                .spec(reqspec)



                .when()
                .delete("/school-service/api/attestation/"+attestionsId)


                .then()
                .log().body()
                .statusCode(204)

        ;
    }

    @Test(dependsOnMethods = "DeleteAttestions")
    public void DeleteAttestionsNegative(){

        given()
                .spec(reqspec)



                .when()
                .delete("/school-service/api/attestation/"+attestionsId)


                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("attestation not found"))

        ;
    }

}
