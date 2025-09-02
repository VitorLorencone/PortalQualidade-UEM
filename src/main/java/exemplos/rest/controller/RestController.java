package exemplos.rest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import utilitarios.RestService;

/**
 * Este exemplo mostra como usar a classe utilitária RestService para chamar API
 * que não necessita de autenticação. Caso vá utilizar uma API com autenticação
 * JWT, faça uma cópia de RestAuthenticatedService, configure o usuário, senha e
 * URL, e utilize o RestAuthenticatedService.
 *
 * @author alison
 */
public class RestController extends Window {

    private Window win;

    private Listbox tipo;
    private Textbox url;
    private Textbox respostaJson;

    private final RestService restService = new RestService();

    public void onCreate() {
        win = (Window) getFellow("winRest");

        tipo = (Listbox) getFellow("tipo");
        url = (Textbox) getFellow("url");
        respostaJson = (Textbox) getFellow("respostaJson");
    }

    public void executar() {
        String tipoSelecionado = tipo.getSelectedItem().getValue();
        String urlSelecionada = url.getValue();

        if (tipoSelecionado.equals("GET")) {

            //PEGA A RESPOSTA EM JSON
            String json = restService.get(urlSelecionada);

            //PEGA E RESPOSTA EM OBJETO
            //Objeto object = restService.getObject(urlSelecionada, Objeto.class);
            
            //VOCE PODE TRANSFORAR MANUALMENTE A RESPOSTA EM JSON PARA OBJETO, CASO DESEJAR
            //Objeto object = new Gson().fromJson(json, Objeto.class);
            
            //CASO A RESPOSTA SEJA UMA LISTA, AQUI VOCÊ PODE PEGAR A LISTA DE OBJETOS
            //List<Objeto> list = restService.getObjectList(urlSelecionada, Objeto.class);
            
            //MOSTRANSO O RESULTADO EM JSON PARA VOCÊ VER
            respostaJson.setValue(prettyPrintJsonString(json));

        } else if (tipoSelecionado.equals("POST")) {

            //FAZ O POST PASSANDO UM JSON
            //String json = restService.post(urlSelecionada, jsonParaEnviar);
             //MOSTRANSO O RESULTADO EM JSON PARA VOCÊ VER
            //respostaJson.setValue(prettyPrintJsonString(json));
            
            //Não implementei exemplo dessa funcionalidade
            respostaJson.setValue("Não implementado. Veja os exemplos no código-fonte.");
        }
    }

    private String prettyPrintJsonString(String uglyJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(uglyJson);
        String prettyJsonString = gson.toJson(jsonElement);
        return prettyJsonString;
    }
}
