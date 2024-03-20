import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
public class APIKey {

    /**
     * Use https://www.weatherapi.com/docs/ as a reference.
     * First, You need to signup to weatherapi.com, and then you can find your API key under your account
     * after that, you can use Java to request: http://api.weatherapi.com/v1/current.json?key=[YOUR-APIKEY]&q=Indianapolis&aqi=no
     * Parse the json and print the current temperature in F and C.
     **/


    @Test
    void weatherAPI(){
        Response response = given()
                .param("key", "562a5e00c27640f7a5e02704241503")
                .param("q", "NewJersey")
                .log().uri()
                .when()
                .get("https:api//www.weatherapi.com/docs/ as a reference")
                .then()
                .log().body()
                .extract().response();
                double tempC=response.jsonPath().getDouble("current.temp_c");
                double tempF=response.jsonPath().getDouble("current.temp_f");

    }
}
