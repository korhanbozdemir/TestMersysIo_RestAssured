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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class _6_EducationTest {

    Faker randomUreteci=new Faker();
    RequestSpecification reqSpec;
    String subjectID ="";

    String subjectName ="";
    String subjectCode ="";

    @BeforeClass
    public void Setup(){
        baseURI ="https://test.mersys.io/";

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
                .statusCode(200)
                .extract().response().getDetailedCookies();
        ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void createSubject(){

        subjectName = randomUreteci.name().name();
        subjectCode = randomUreteci.code().ean8();

        Map<String,String> newSubject=new HashMap<>();
        newSubject.put("name", subjectName);
        newSubject.put("code", subjectCode);

         subjectID =
         given()
                 .spec(reqSpec)
                 .body(newSubject)
                 .when()
                 .post("school-service/api/subject-categories")

                 .then()
                 .log().body()
                 .statusCode(201)
                 .extract().path("id");
        ;
    }

   @Test(dependsOnMethods = "createSubject")
   public void createSubjectNegative()
   {
       Map<String,String> newSubject=new HashMap<>();
       newSubject.put("name", subjectName);
       newSubject.put("code", subjectCode);

       given()
               .spec(reqSpec)
               .body(newSubject)

               .when()
               .post("school-service/api/subject-categories")

               .then()
               .log().body()
               .statusCode(400)
               .body("message", containsString("already"))
       ;
   }

    @Test(dependsOnMethods = "createSubjectNegative")
    public void updateSubject(){
        String newSubjectName=randomUreteci.name().name();
        Map<String,String> updSubject=new HashMap<>();
        updSubject.put("id", subjectID);
        updSubject.put("name",newSubjectName);
        updSubject.put("code",subjectCode);

        given()
                .spec(reqSpec)
                .body(updSubject)

                .when()
                .put("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newSubjectName))
        ;
    }

    @Test(dependsOnMethods = "updateSubject")
    public void deleteSubject()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/"+ subjectID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteSubject")
    public void deleteSubjectNegative()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/"+ subjectID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("SubjectCategory not  found"))
        ;
    }


}
