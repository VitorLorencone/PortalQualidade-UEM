package utilitarios;

import application.service.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.HttpStatus;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Service genérico de chamadas REST para a API que tenha autenticação com Token
 * JWT, utilizando a biblioteca Unirest-Java.
 *
 * @see
 * <a href="https://kong.github.io/unirest-java/">https://kong.github.io/unirest-java/</a>
 *
 * @author alison
 */
public class RestAuthenticatedService {

    private static final String USER = "usario_da_api";
    private static final String PASS = "senha_do_usuario_da_api";
    private static final String URL_BASE = Application.getInstance().getEnviroment().equals(Application.PRODUCTION) ? "url_da_api_de_producao" : "url_da_api_de_teste";

    private static String JWT_TOKEN;
    private static final Integer JWT_TOKEN_MAX_RETRIES = 2;
    private Integer JWT_TOKEN_RETRIES = 0;

    private final Map<String, String> basicHeaders = new HashMap<>();
    private final Map<String, String> authenticatedHeaders = new HashMap<>();

    public RestAuthenticatedService() {

        basicHeaders.put("Accept", "*/*");
        basicHeaders.put("Accept-Encoding", "gzip, deflate, br");
        basicHeaders.put("Content-Type", "application/json");
        basicHeaders.put("User-Agent", "NPD-UEM");

        authenticatedHeaders.put("Authorization", "Bearer " + getCurrentJwtToken());
    }

    /**
     * Autentica na API e retorna um Token JWT.
     *
     * @return String com o Token JWT, em caso de erro o Token JWT será uma
     * string vazia (inválido)
     */
    private String getNewJwtToken() {

        JsonObject json = new JsonObject();
        json.addProperty("username", USER);
        json.addProperty("password", PASS);

        try {
            HttpResponse<String> response = Unirest.post(URL_BASE + "/aut/login")
                    .headers(basicHeaders)
                    .body(json.toString())
                    .asString();

            if (response.getStatus() != HttpStatus.OK) {
                System.out.println("Não foi possível buscar Token JWT na API [" + response.getStatus() + "]: " + response.getBody());
            }

            return response.getBody();

        } catch (UnirestException ex) {
            System.out.println("Houve um erro ao buscar Token JWT na API: " + ExceptionUtils.getStackTrace(ex));
        }

        return "invalid";
    }

    /**
     * Retorna o Token JWT atual da aplicação.
     *
     * @return String com o Token JWT
     */
    private String getCurrentJwtToken() {

        if (JWT_TOKEN == null) {
            setCurrentJwtToken(getNewJwtToken());
        }
        return JWT_TOKEN;
    }

    /**
     * Define um novo Token JWT para a aplicação.
     *
     * @param newJwtToken String com o Token JWT
     */
    private void setCurrentJwtToken(String newJwtToken) {

        JWT_TOKEN = newJwtToken;
        authenticatedHeaders.put("Authorization", "Bearer " + getCurrentJwtToken());
    }

    /**
     * Realiza um POST autenticado.
     *
     * @param url Caminho do entry-point
     * @param body Dados a serem enviados, ou nulo caso não necessário
     * @return Response com o resultado da API em JSON
     */
    public String post(String url, Object body) {

        try {
            HttpResponse<JsonNode> response = Unirest.post(URL_BASE + url)
                    .headers(basicHeaders)
                    .headers(authenticatedHeaders)
                    .body(body)
                    .asJson();

            if (response.getStatus() == HttpStatus.UNAUTHORIZED && JWT_TOKEN_RETRIES < JWT_TOKEN_MAX_RETRIES) { //retry com novo token
                JWT_TOKEN_RETRIES++;
                setCurrentJwtToken(getNewJwtToken());
                return post(url, body);
            } else if (response.getStatus() != HttpStatus.OK) {
                System.out.println("Não foi possível realizar o POST na API [" + response.getStatus() + "]: " + response.getBody().toPrettyString());
                return null;
            }

            String json = response.getBody().toString();
            return json;

        } catch (UnirestException ex) {
            System.out.println("Houve um erro ao realizar o POST na API: " + ExceptionUtils.getStackTrace(ex));
            return null;
        }

    }

    /**
     * Realiza um GET autenticado.
     *
     * @param url Caminho do entry-point
     * @return Response com o resultado da API em JSON
     */
    public String get(String url) {

        try {
            HttpResponse<JsonNode> response = Unirest.get(URL_BASE + url)
                    .headers(basicHeaders)
                    .headers(authenticatedHeaders)
                    .asJson();

            if (response.getStatus() == HttpStatus.UNAUTHORIZED && JWT_TOKEN_RETRIES < JWT_TOKEN_MAX_RETRIES) { //retry com novo token
                JWT_TOKEN_RETRIES++;
                setCurrentJwtToken(getNewJwtToken());
                return get(url);
            } else if (response.getStatus() != HttpStatus.OK) {
                System.out.println("Não foi possível realizar o GET na API [" + response.getStatus() + "]: " + response.getBody().toPrettyString());
                return null;
            }

            String json = response.getBody().toString();
            return json;

        } catch (UnirestException ex) {
            System.out.println("Houve um erro ao realizar o GET na API: " + ExceptionUtils.getStackTrace(ex));
            return null;
        }

    }

    /**
     * Realiza um GET autenticado.
     *
     * @param url Caminho do entry-point
     * @param objectClass Classe do objeto que a API irá retornar
     * @return Response com o resultado da API em Objeto
     */
    public <T> Object getObject(String url, Class<T> objectClass) {

        String json = get(url);

        try {
            return new Gson().fromJson(json, objectClass);
        } catch (JsonSyntaxException ex) {
            System.out.println("Houve um erro ao transformar o JSON em um Objeto: " + ExceptionUtils.getStackTrace(ex));
        }

        return null;
    }

    /**
     * Realiza um GET autenticado.
     *
     * @param url Caminho do entry-point
     * @param objectClass Classe do objeto que a API irá retornar em uma lista
     * @return Response com o resultado da API em uma Lista de Objetos
     */
    public <T> List<T> getObjectList(String url, Class<T> objectClass) {

        String json = get(url);

        Type typeOfT = TypeToken.getParameterized(List.class, objectClass).getType();

        try {
            return new Gson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException ex) {
            System.out.println("Houve um erro ao transformar o JSON em uma Lista de Objetos: " + ExceptionUtils.getStackTrace(ex));
        }

        return null;
    }

}
