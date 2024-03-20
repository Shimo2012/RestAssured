import POJOClasses.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSenderOptions;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;




import static io.restassured.RestAssured.*;

public class ZippoAPITest {

    @Test
    void test1() {
        given()  // Preparation(Token, Request Body, parameters, cookies...
                .when()  // To send the request(Request method, request url)
                .then();  // Response(Response body, tests, extract data, set local or global variables)
    }

    @Test
    void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210") //Set up request method and url

                .then()
                .log().body()  // prints response body
                .log().status()
                .statusCode(200); // tests if the status code is 200


    }

    @Test
    void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .contentType(ContentType.JSON); // Tests if the response is in correct form(JSON)

    }

    @Test
    void countryInformationTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")); // Tests if the country value is correct

    }

    @Test
    void stateInformationTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"));


    }

    // Send a request to "http://api.zippopotam.us/us/90210"
    // and check if the state abbreviation is "CA"

    @Test
    void stateAbbreviationTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'state abbreviation'", equalTo("CA"));
    }

    // Send a request to "http://api.zippopotam.us/tr/01000"
    // and check if the body has "Büyükdikili Köyü"
    @Test
    void bodyHasItem() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Büyükdikili Köyü"));
        // When we don't use index it gets all values/places from the response and creates an array with them.
        // hasItem checks if the array contains "........" value in it

    }

    @Test
    void arrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasSize(71)); //Tests if the size of the list is correct
    }

    @Test
    void multipleTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places", hasSize(71))  //Tests if places array's size is 71
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .body("country", equalTo("Turkey"));
        // If one test fails the entire @Test fails

    }

// Parameters
    //There are 2 types of parameters
    //  1) Path parameters   -> http://api.zippopotam.us/tr/01000 -> They are parts of the url
    //  2) Query Parameters  -> https://gorest.co.in/public/v1/users?page=3  -> They are separated by a ? mark

    @Test
    void pathParametersTest1() {

        String countryCode = "us";
        String zipCode = "90210";

        given()
                .pathParam("country", countryCode)
                .pathParam("zip", zipCode)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{zip}")
                .then()
                .log().body()
                .statusCode(200);
    }
    // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
    // of the place array is 1

    @Test
    void pathParametersTest2() {

        for (int i = 90210; i <= 90213; i++) {
            given()
                    .pathParam("zip", i)

                    .when()
                    .get("http://api.zippopotam.us/us/{zip}")
                    .then()
                    .log().body()
                    .body("places", hasSize(1));
        }
    }

    @Test
    void queryParameterTest1() {
            given()
                    .param("page", 2)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200);
        }


        // send the same request for the pages between 1-10 and check if
        // the page number we send from request and page number we get from response are the same
    // Write the same test with Data Provider

    @Test
    void queryParameterTest2() {
        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .pathParam("apiName", "users")
                    .pathParam("version", "v1")
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/{version}/{apiName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));
        }


    }

    // Write the same test with Data Provider
    @Test(dataProvider = "parameters")
    void queryParametersTestWithDataProvider(int pageNumber, String apiName, String version) {
        given()
                .param("page", pageNumber)
                .pathParam("apiName", apiName)
                .pathParam("version", version)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/{version}/{apiName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(pageNumber));
    }

    @DataProvider
    public Object[][] parameters() {
        Object[][] parametersList = {
                {1, "users", "v1"},
                {2, "users", "v1"},
                {3, "users", "v1"},
                {4, "users", "v1"},
                {5, "users", "v1"},
                {6, "users", "v1"},
                {7, "users", "v1"},
                {8, "users", "v1"},
                {9, "users", "v1"},
                {10, "users", "v1"},
        };

        return parametersList;
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {
        baseURI = "https://gorest.co.in/public";
        // if the request url in the request method doesn't have http part
        // rest assured puts baseURI to the beginning of the url in the request method

        // if we are using the same things for our request in our tests we can put them in requestSpecification,
        // so we don't have to write them again and again

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .addPathParam("apiName", "users")
                .addPathParam("version", "v1")
                .addParam("page", 3)
                .build();
        // if we are using the same things for our request in our tests we can put them in responseSpecification,
        // so we don't have to write them again and again

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("meta.pagination.page", equalTo(3))
                .build();

    }

    @Test
    void baseUTITest() {
        given()
                .param("page", 3)
                .log().uri()
                .when()
                .get("/v1/users")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    void specificationTest() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification);


    }
    @Test
    void extractStringTest(){
    String placeName =   given()
                .pathParam("country", "us")
                .pathParam("zip", "90210")
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{zip}")
                .then()
                .log().body()
                .extract().path("places[0].'place name'" );

    // with extract method our request returns a value.
        // extract returns only one part of the response(the part that we specified in the path method) or list of that value
        // we can assign it to a variable and use it however we want

        System.out.println("placeName = " + placeName);

    }


    @Test
    void extractInTest(){
      int pageNumber =   given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.page");

        System.out.println("pageNumber = " + pageNumber);
        Assert.assertTrue(pageNumber==3);
        // we are not assign an int to a String(cannot assign a type to another type)
    }


    @Test
    void
    extractListTest1(){
    List<String> nameList =     given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.name");

        System.out.println("nameList.size() = "  + nameList.size());
        System.out.println("nameList.size(4) = "  + nameList.get(4));
        System.out.println("nameList.contains(\"Ravi Adiga\") = "  + nameList.contains("Ravi Adiga"));

        Assert.assertTrue(nameList.contains("Ravi Adiga"));
    }


    // Send a request to https://gorest.co.in/public/v1/users?page=3
    // and extract email values from the response and check if they contain patel_atreyee_jr@gottlieb.test




    @Test
    void extractListTest2(){
     List<String> emailList   =   given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.email");

     Assert.assertTrue(emailList.contains("patel_atreyee_jr@gottlieb.test"));

    }
// Send a request to https://gorest.co.in/public/v1/users?page=3
    // and check if the next link value contains page=4

    @Test
    void nextLinkTest(){
     String nextLink =    given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.links.next");
        System.out.println("nextLink = " + nextLink);
        Assert.assertTrue(nextLink.contains("page=4"));
    }

    @Test
    void extractResponse(){
      Response response =  given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().response();
        // The entire request returns the entire response as a Response object
        // By using this object we are  able to extract multiple values with one request

        int page = response.path("meta.pagination.page");
        System.out.println("page = " + page);

        String currentUrl = response.path("meta.pagination.links.current");
        System.out.println("currentUrl = " + currentUrl);

        String name = response.path("data[1].name");
        System.out.println("name = " + name);

        List<String> emailList  = response.path("data.email");
        System.out.println("emailList ="  + emailList);


        // extract.path           vs                 extract.response
        // extract.path() can only give us one part of the response. If you need different values
        // from different parts of the response (names and page)
        // you need to write two different request.
        // extract.response() gives us the entire response as an object so if you need different values
        // from different parts of the response (names and page)
        // you can get them with only one request

    }

    @Test
    void extractJsonPOJO(){
        Location location = given()
                .pathParam("countryCode", "us")
                .pathParam("zipCode", "90210")
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .extract().as(Location.class);

        System.out.println("location.getPostCode() = " + location.getPostCode());
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces().get(0).getPlaceName() = " + location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getState() = " + location.getPlaces().get(0).getState());

    }
    // This request extracts the entire response and assigns it to Location class as a Location object
    // We cannot extract the body partially (e.g. cannot extract place object separately)

}



