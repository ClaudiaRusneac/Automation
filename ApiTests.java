import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    @Order(1)
    @Tag("positive")
    @DisplayName("GET /posts/2 - verificare postare id 2")
    void getPostById() {
        given()
                .when()
                .get("/posts/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(2))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()))
                .body("body", not(emptyOrNullString()));
    }

    @Test
    @Order(2)
    @Tag("positive")
    @DisplayName("GET /posts - verifica postare cu id 11")
    void getAllPosts() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(100))
                .body("find { it.id == 11 }.userId", equalTo(2))
                .body("find { it.id == 11 }.title", equalTo("et ea vero quia laudantium autem"))
                .body("find { it.id == 11 }.body", containsString("delectus reiciendis molestiae occaecati"));
    }

    @Test @Order(3) @Tag("positive")
    @DisplayName("GET /posts/2/comments")
    void getCommentsForPostId2(){
        given()
                .when()
                .get("/posts/2/comments")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("[0].postId", equalTo(2))
                .body("[0].email", containsString("@"));

    }

    @Test
    @Order(4)
    @Tag("positive")
    @DisplayName("GET /comments?postId=2")
    void getComments() {
        given()
                .queryParam("postId", 2)
                .when()
                .get("comments")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(2)));
    }

    @Test
    @Order(5)
    @Tag("positive")
    @DisplayName("POST /posts")
    void createPost() {
        String body = """
                    {
                    "title": "Claudia",
                    "body": "Hello world",
                    "userId": 9
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("id", equalTo(101))
                .body("title", equalTo("Claudia"))
                .body("body", equalTo("Hello world"))
                .body("userId", equalTo(9));
    }

    @Test
    @Order(6)
    @Tag("positive")
    @DisplayName("PUT /posts/25 - update ")
    void updatePost25() {
        String body = """
            {
                "id": 25,
                "title": "Nou titlu pentru postarea 25",
                "body": "Acesta este un body actualizat",
                "userId": 5
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/posts/25")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(25))
                .body("title", equalTo("Nou titlu pentru postarea 25"))
                .body("body", equalTo("Acesta este un body actualizat"))
                .body("userId", equalTo(5));
    }
    @Test
    @Order(7)
    @Tag("positive")
    @DisplayName("PATCH /posts/25 - actualizare parțială body")
    void patchPost25Body() {
        String body = """
            {
                "body": "acesta este body-ul nou pentru postarea 25"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/posts/25")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(25))
                .body("body", equalTo("acesta este body-ul nou pentru postarea 25"));
    }

    @Test
    @Order(8)
    @Tag("positive")
    @DisplayName("DELETE /posts/10")
    void createDelete() {
        given()
                .when()
                .delete("/posts/10")
                .then()
                .statusCode(200);

    }

    @Test
    @Order(9)
    @Tag("negative")
    @DisplayName("GET /nu-exista - endpoint inexistent")
    void getNuExistaEndpoint() {
        given()
                .when()
                .get("/nu-exista")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(10)
    @Tag("negative")
    @DisplayName("POST /posts - body gol")
    void BodyGol() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/posts")
                .then()
                .statusCode(201);
    }
    @Test
    @Order(11)
    @Tag("negative")
    @DisplayName("DELETE /posts/9999 - ștergere post inexistent")
    void StergerePostInexistent() {
        given()
                .when()
                .delete("/posts/9999")
                .then()
                .statusCode(200);
    }
    @Test
    @Order(12)
    @Tag("positive")
    @DisplayName("GET /users/5 - verificare nume user")
    void VerificareNume() {
        given()
                .when()
                .get("/users/5")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(5))
                .body("name", not(emptyOrNullString()));
    }
}
