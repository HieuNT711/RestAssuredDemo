package stepDefinitions;

import java.util.List;
import java.util.Map;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.responses.Token;
import com.google.gson.GsonBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.google.gson.Gson;

public class StepsV2 {

    private static final String USER_ID = "521bb92f-54b8-4b3f-9fe8-dc8588440bed";
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    private static String token;
    private static Response response;
    private static String jsonString;
    private static String bookId;

    private static final String USERNAME = "TOOLSQA-Test-Vix";
    private static final String PASSWORD = "Test@@123";

    private static Token tokenResponse;


    @Given("^I am an authorized user$")
    public void iAmAnAuthorizedUser() {
        AuthorizationRequest authRequest = new AuthorizationRequest();
        authRequest.setUsername(USERNAME);
        authRequest.setPassword(PASSWORD);
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        response = request.body(new GsonBuilder().setPrettyPrinting().create().toJson(authRequest)).post("/Account/v1/GenerateToken");
        String jsonString = response.asString();
        token = JsonPath.from(jsonString).get("token");

        tokenResponse = response.getBody().as(Token.class);

        System.out.println("Step1:" + authRequest);
        System.out.println(jsonString);
        System.out.println(tokenResponse.token);
    }

    @Given("^A list of books are available$")
    public void listOfBooksAreAvailable() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        response = request.get("/BookStore/v1/Books");

        jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");

        bookId = books.get(7).get("isbn");
    }

    @When("^I add a book to my reading list$")
    public void addBookInList() {

        AddBooksRequest addBooksRequest = new AddBooksRequest(USER_ID, new ISBN(bookId));

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.body(new GsonBuilder().setPrettyPrinting().create().toJson(addBooksRequest)).post("/BookStore/v1/Books");
        System.out.printf("Step2:" + response.asString());
        System.out.printf(token);

    }

    @Then("^The book is added$")
    public void bookIsAdded() {
        Assert.assertEquals(201, response.getStatusCode());
        //hieu
    }

    @When("^I remove a book from my reading list$")
    public void removeBookFromList() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        RemoveBookRequest removeBookRequest = new RemoveBookRequest(USER_ID, bookId);
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.body(removeBookRequest).delete("/BookStore/v1/Book");
    }

    @Then("^The book is removed$")
    public void bookIsRemoved(){
        Assert.assertEquals(204, response.getStatusCode());

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.get("/Account/v1/User/" + USER_ID);
        Assert.assertEquals(200, response.getStatusCode());

        jsonString = response.asString();
        List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
        Assert.assertEquals(0, booksOfUser.size());
    }

}