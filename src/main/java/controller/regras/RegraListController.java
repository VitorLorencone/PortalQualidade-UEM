package controller.regras;

import dao.RegraDAO;
import model.Regra;

import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import org.zkoss.zk.ui.event.Event;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import java.text.Normalizer;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;

public class RegraListController extends Window {
    
    private Window win;
    
    private Textbox vlPesquisa;
    private Grid resultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final RegraDAO regraDao = new RegraDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winRegraList");

        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    public void filtrar() {
        String filtro = " 1=1 ";
        String ordem = "order by t.nome";

        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                filtro += " AND t.cdRegra = " + textoDaPesquisa;
            } else {
                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                
                filtro += "AND UPPER(t.nome) like '%" + textoTratado + "%'";
            }
        }

        List<Regra> regras = regraDao.listar(filtro, ordem);
        if (regras != null) {
            SimpleListModel listModel = new SimpleListModel(regras);
            this.resultados.setModel(listModel);
        } 
    }

    public void novaRegra() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    public void onClickedVerRegra(Event event) throws Exception {
        Integer cdRegra = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("regra", cdRegra);
        zkUtils.setParametro("ação", "editar");
        this.redirecionar();
    }

    private void redirecionar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem);
        include.setSrc("dados/regras/regra.zul");
    }
}
