package TestMersysIo_RestAssured;

import Model.School;
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
import static org.hamcrest.Matchers.equalTo;
public class _07_AddLocationToSchoolSetup {
    RequestSpecification reqSpec;
    Faker faker=new Faker();
    String schoolId="";
    @BeforeClass
    public void Setup(){

        baseURI="https://test.mersys.io/";

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

        reqSpec= new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void creatSchoolLocation(){

        Map<String,String> creatSchool=new HashMap<>();

        String name = faker.name().firstName() + faker.number().digits(5);
        String shortName = faker.name().lastName() + faker.number().digits(3);
        String capacity = faker.number().digits(5);

        creatSchool.put("name", name );
        creatSchool.put("shortName", shortName);
        creatSchool.put("capacity", capacity);
        creatSchool.put("type", "LABORATORY");
        creatSchool.put("school", "6390f3207a3bcb6a7ac977f9");

        schoolId =

                given()

                        .spec(reqSpec)
                        .body(creatSchool)
                        //.log().body()
                        .when()
                        .post("/school-service/api/location")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id")
        ;
    }
    @Test(dependsOnMethods = "creatSchoolLocation")
    public void EditSchool(){

        Map<String,String> editSchool=new HashMap<>();

        editSchool.put("id",schoolId);
        editSchool.put("name", "yasko32" );
        editSchool.put("shortName", "kaya67");
        editSchool.put("capacity", "44353");
        editSchool.put("type", "CLASS");
        editSchool.put("school", "6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .body(editSchool)

                .when()
                .put("/school-service/api/location")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo("yasko32"))
        ;
    }
    @Test(dependsOnMethods = "EditSchool")
    public void DeleteSchool(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/location/"+schoolId)

                .then()
                .log().body()
                .statusCode(200)

        ;
    }
    @Test(dependsOnMethods = "DeleteSchool")
    public void DeleteSchoolNegative(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/location/"+schoolId)


                .then()
                .log().body()
                .statusCode(400)
        ;

    }



}
