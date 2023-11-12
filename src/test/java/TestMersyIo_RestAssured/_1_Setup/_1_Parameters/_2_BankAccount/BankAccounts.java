package TestMersyIo_RestAssured._1_Setup._1_Parameters._2_BankAccount;

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

public class BankAccounts extends Variables {

    Map<String, String> bankAccount;


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
    public void createBankAccount() {

        bankAccount=new HashMap<>();

        bankAccountUserName= faker.address().firstName()+" "+faker.address().lastName();
        bankAccount.put("name", bankAccountUserName);

        bankAccount.put("iban", "test" + faker.number().digits(12));
        bankAccount.put("integrationCode", faker.number().digits(4));

        bankAccount.put("currency", "EUR");
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");


        bankAccountID =

                given()

                        .spec(reqSpec)
                        .body(bankAccount)
                        //.log().body()
                        .when()
                        .post(url+"bank-accounts")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Bank Account Test: Successfully passed !");
    }

    @Test(priority = 2,dependsOnMethods = "createBankAccount")
    public void createBankAccountNegative() {

        given()

                .spec(reqSpec)
                .body(bankAccount)
                //.log().body()
                .when()
                .post(url+"bank-accounts")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already")) ;

        System.out.println("Create Bank Account Negative Test: Successfully passed !");


    }

    @Test(priority = 3,dependsOnMethods = "createBankAccount")
    public void updateBankAccount() {

        bankAccountUserName= faker.address().firstName()+" "+faker.address().lastName()+" "+faker.address().lastName();
        bankAccount.put("name", bankAccountUserName);

        bankAccount.put("iban", "DE" + faker.number().digits(16));
        bankAccount.put("integrationCode", faker.number().digits(8));

        bankAccount.put("currency", "USD");
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccount.put("id",bankAccountID);

        given()

                .spec(reqSpec)
                .body(bankAccount)
                // .log().body()
                .when()
                .put(url+"bank-accounts")
                .then()
                //.log().body()
                .statusCode(200)
                .body("name", equalTo(bankAccountUserName));

        System.out.println("Update Bank Account Test: Successfully passed !");

    }

    @Test(priority = 4,dependsOnMethods = "updateBankAccount")
    public void deleteBankAccount() {

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"bank-accounts/" + bankAccountID)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete Bank Account Test: Successfully passed !");
    }

    @Test(priority = 5,dependsOnMethods = "deleteBankAccount")
    public void deleteBankAccountNegative() {

        given()

                .spec(reqSpec)
                //.log().uri()
                .when()
                .delete(url+"bank-accounts/" + bankAccountID)
                .then()
               //.log().body()
                .statusCode(400)
                .body("message", containsString("must be exist"));

        System.out.println("Delete Bank Account Negative Test: Successfully passed !");
    }
}
