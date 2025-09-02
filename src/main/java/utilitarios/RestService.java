package utilitarios;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Service genérico de chamadas REST para a API que não tenha autenticação,
 * utilizando a biblioteca Unirest-Java.
 *
 * @see
 * <a href="https://kong.github.io/unirest-java/">https://kong.github.io/unirest-java/</a>
 *
 * @author alison
 */
public class RestService {

    private final Map<String, String> basicHeaders = new HashMap<>();

    public RestService() {

        basicHeaders.put("Accept", "*/*");
        basicHeaders.put("Accept-Encoding", "gzip, deflate, br");
        basicHeaders.put("Content-Type", "application/json");
        basicHeaders.put("User-Agent", "NPD-UEM");

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
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .headers(basicHeaders)
                    .body(body)
                    .asJson();

            String json = response.getBody().toString();
            return json;

        } catch (UnirestException ex) {
            System.out.println("Houve um erro ao realizar o POST na API: " + ExceptionUtils.getStackTrace(ex));
        }

        return null;
    }

    /**
     * Realiza um GET autenticado.
     *
     * @param url Caminho do entry-point
     * @return Response com o resultado da API em JSON
     */
    public String get(String url) {

        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .headers(basicHeaders)
                    .asJson();

            String json = response.getBody().toString();
            return json;

        } catch (UnirestException ex) {
            System.out.println("Houve um erro ao realizar o GET na API: " + ExceptionUtils.getStackTrace(ex));
        }

        return null;
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
