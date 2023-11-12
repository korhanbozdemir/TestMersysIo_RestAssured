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
import static org.hamcrest.Matchers.equalTo;

public class _03_DocumentTypes {

    RequestSpecification reqSpec;
    String documentTypesId="";
    Faker faker=new Faker();
    @BeforeClass
    public void Setup(){

        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies=
        given()
                .contentType(ContentType.JSON)
                .body(userCredential)


                .when()
                .post("/auth/login")


                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();

        reqSpec=new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }
    @Test
    public void CreatDocumentTypes(){

        String name=faker.name().firstName();
        String description=faker.job().title();

        documentTypesId=

        given()
                .spec(reqSpec)
                .body("{\n" +
                        "  \"id\": null,\n" +
                        "  \"name\": \""+name+"\",\n" +
                        "  \"description\": \""+description+"\",\n" +
                        "  \"attachmentStages\": [\n" +
                        "    \"EXAMINATION\"\n" +
                        "  ],\n" +
                        "  \"active\": true,\n" +
                        "  \"required\": true,\n" +
                        "  \"useCamera\": false,\n" +
                        "  \"translateName\": [],\n" +
                        "  \"schoolId\": \"646cbb07acf2ee0d37c6d984\"\n" +
                        "}")

                .when()
                .post("school-service/api/attachments/create")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

        ;
    }
    @Test(dependsOnMethods = "CreatDocumentTypes")
    public void EditDcumentType(){
        String newName=faker.name().firstName();
        String newdescription=faker.job().title();

        given()
                .spec(reqSpec)
                .body("{\n" +
                        "  \"id\": \""+documentTypesId+"\",\n" +
                        "  \"name\": \""+newName+"\",\n" +
                        "  \"description\": \""+newdescription+"\",\n" +
                        "  \"attachmentStages\": [\n" +
                        "    \"EXAMINATION\"\n" +
                        "  ],\n" +
                        "  \"active\": false,\n" +
                        "  \"required\": true,\n" +
                        "  \"useCamera\": false,\n" +
                        "  \"translateName\": [],\n" +
                        "  \"schoolId\": \"646cbb07acf2ee0d37c6d984\"\n" +
                        "}")

                .when()
                .put("/school-service/api/attachments")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(newName))
        ;

    }
    @Test(dependsOnMethods = "EditDcumentType")
    public void DeleteDocumentType(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attachments/"+documentTypesId)


                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "DeleteDocumentType")
    public void DeleteDocumentTypeNegative(){

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attachments/"+documentTypesId)

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Attachment Type not found"))
        ;
    }
}
