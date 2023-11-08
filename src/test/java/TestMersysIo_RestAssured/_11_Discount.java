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
public class _11_Discount {

    String discountID="";
    Faker faker=new Faker();
    RequestSpecification reqSpec;

    @BeforeClass
    public void Setup(){

        baseURI="https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies=
        given()
                .body(userCredential)
                .contentType(ContentType.JSON)

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
    public void CreatDiscount(){

        String description=faker.name().firstName();
        String code=faker.number().digits(3);
        String priority=faker.number().digits(4);

        Map<String,Object> creatDiscount=new HashMap<>();
        creatDiscount.put("description",description);
        creatDiscount.put("code",code);
        creatDiscount.put("priority",priority);

        discountID=
        given()
                .spec(reqSpec)
                .body(creatDiscount)

                .when()
                .post("/school-service/api/discounts")


                .then()
                .statusCode(201)
                .log().body()
                .extract().path("id")

        ;

    }

    @Test(dependsOnMethods = "CreatDiscount")
    public void EditDiscount(){

        Map<String,String> editDiscount=new HashMap<>();
        editDiscount.put("id",discountID);
        editDiscount.put("description","mahmudov");
        editDiscount.put("code","code4450");
        editDiscount.put("priority","353535");

        given()
                .spec(reqSpec)
                .body(editDiscount)

                .when()
                .put("/school-service/api/discounts")

                .then()
                .log().body()
                .statusCode(200)
                .body("description",equalTo("mahmudov"))


        ;

    }

    @Test(dependsOnMethods = "EditDiscount")
    public void DeleteDiscount(){

        given()
                .spec(reqSpec)


                .when()
                .delete("/school-service/api/discounts/"+discountID)


                .then()
                .log().body()
                .statusCode(200)

        ;

    }

    @Test(dependsOnMethods = "DeleteDiscount")
    public void DeleteDiscountNegative(){

        given()
                .spec(reqSpec)


                .when()
                .delete("/school-service/api/discounts/"+discountID)


                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Discount not found"))

        ;

    }




}
