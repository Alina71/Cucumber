package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExample {
    String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";
    static String employee_id;
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MTA4ODg5NjksImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTcxMDkzMjE2OSwidXNlcklkIjoiNjQ1MSJ9.EN_yqkctGG1bh3ZpG6rgJuo1xZjM_75YZQSy7wmQNVI";

    @Test
    public void aCreateEmployee() {
        //it will create the request
        RequestSpecification request = given().
                header("Content-Type", "application/json").
                header("Authorization", token).
                body("{\n" +
                        "  \"emp_firstname\": \"Andreea\",\n" +
                        "  \"emp_lastname\": \"Marin\",\n" +
                        "  \"emp_middle_name\": \"Elena\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"1985-05-05\",\n" +
                        "  \"emp_status\": \"permanent\",\n" +
                        "  \"emp_job_title\": \"QA Engineer\"\n" +
                        "}");

        // it will give us the response after hitting the endpoint
        Response response = request.when().post("/createEmployee.php");

        //assert that is the method we use to validate the response
        response.then().assertThat().statusCode(201);

        //this method is used to print the response in console
        response.prettyPrint();

        //hamcrest matchers
        response.then().assertThat().body("Message", equalTo("Employee Created"));
        response.then().assertThat().body("Employee.emp_firstname", equalTo("Andreea"));
        response.then().assertThat().body("Employee.emp_lastname", equalTo("Marin"));
        response.then().assertThat().header("Connection", equalTo("Keep-Alive"));

        //to fetch the employee_id from response we need respond variable
        employee_id = response.jsonPath().getString("Employee.employee_id");
    }

    @Test
    public void bGetCreateEmployee() {
        // prepare the request
        RequestSpecification request = given().
                header("Content-Type", "application/json").
                header("Authorization", token).queryParam("employee_id", employee_id);
        //hitting the endpoint
        Response response = request.when().get("/getOneEmployee.php");
        //validate the response
        response.then().assertThat().statusCode(200);
        response.prettyPrint();
        String temporaryEmpId =
                response.jsonPath().getString("employee.employee_id");
        //here we are comparing both emp id's from get and post call
        Assert.assertEquals(temporaryEmpId, employee_id);
        //validate the body from get call
        response.then().assertThat().
                body("employee.emp_lastname", equalTo("Marin"));
        response.then().assertThat().
                body("employee.emp_firstname", equalTo("Andreea"));

}
    @Test
    public void cUpdateEmployee(){
        //prepare the request
        RequestSpecification request = given().
                header("Content-Type","application/json").
                header("Authorization",token).
                body("{\n" +
                        "  \"employee_id\": \""+employee_id+"\",\n" +
                        "  \"emp_firstname\": \"thomas\",\n" +
                        "  \"emp_lastname\": \"kartal\",\n" +
                        "  \"emp_middle_name\": \"hobbes\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"2014-03-09\",\n" +
                        "  \"emp_status\": \"doubtful\",\n" +
                        "  \"emp_job_title\": \"philosopher\"\n" +
                        "}");

        //hitting the endpoint
        Response response = request.when().put("/updateEmployee.php");
        //validation of response
        response.then().assertThat().statusCode(200);
        response.then().assertThat().
                body("Message",equalTo("Employee record Updated"));
        response.prettyPrint();
    }

@Test
    public void dGetUpdatedEmployee(){
        //prepare the request
    RequestSpecification request=given().
            header("Content-Type","applications/json").
            header("Authorization",token).
            queryParam("employee_id",employee_id);
    Response response=request.when().get("/getOneEmployee.php");
    //validation
    response.prettyPrint();
    response.then().assertThat().statusCode(200);
}
@Test
    public void eGetAllEmployees(){
        RequestSpecification request=given().
                header("Content-Type","application/json").
                header("Authorization",token);

        Response response=request.when().get("/getAllEmployees.php");
        response.prettyPrint();
        response.then().assertThat().statusCode(200);
}
@Test
    public void fGetJobTitle(){
        RequestSpecification request=given().
                header("Content-Type","application/json").
                header("Authorization",token);
                Response response=request.when().get("/jobTitle.php");
                response.prettyPrint();
                response.then().assertThat().statusCode(200);

}
@Test
    public void gUpdatePartialEmployeeDetails(){
        RequestSpecification request=given().
                header("Content-Type","application/json").
                header("Authorization",token).
                body("{\n" +
                        "  \"employee_id\": \""+employee_id+"\",\n" +
                        "  \"emp_middle_name\": \"Leyla\",\n" +
                        "   \"emp_job_title\": \"Account\",\n" +
                        " \"emp_status\": \"Confirmed\"\n" +
                        " \n" +
                        "}");
Response response=request.when().patch("/updatePartialEmplyeesDetails.php");
response.then().assertThat().statusCode(201);
response.then().assertThat().body("employee.emp_job_title",equalTo("Account"));
response.then().assertThat().body("employee.emp_middle_name",equalTo("hobbes"));
response.then().assertThat().body("Message",equalTo("Employee record updated successfully"));
response.prettyPrint();
}
}
