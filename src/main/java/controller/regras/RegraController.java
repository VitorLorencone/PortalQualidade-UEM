package controller.regras;

import dao.RegraDAO;
import model.Regra;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

public class RegraController extends Window {
    
    private Window win;
    private Label cdRegra;
    private Textbox nome;
    private Textbox deGatilho;
    private Datebox dtInicio;
    private Datebox dtFim;
    private Textbox deFrequencia;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private RegraDAO regraDao = new RegraDAO();

    private Regra regra = new Regra();

    private String urlRetorno = "";

    public void onCreate() {
        
        this.win = (Window) getFellow("winRegra");

        this.cdRegra = (Label) getFellow("cdRegra");
        this.nome = (Textbox) getFellow("nome");
        this.deGatilho = (Textbox) getFellow("deGatilho");
        this.dtInicio = (Datebox) getFellow("dtInicio");
        this.dtFim = (Datebox) getFellow("dtFim");
        this.deFrequencia = (Textbox) getFellow("deFrequencia");
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        Integer regraId = (Integer) zkUtils.getParametro("regra");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");

        if (acao != null) {
            if (acao.equals("novo")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.cdRegra.setVisible(false);
            } else if (acao.equals("editar")) {
                this.btnSalvar.setVisible(true);
                this.btnCancelar.setVisible(true);
                this.btnExcluir.setVisible(true);
            }
        }

        limparCampos();

        if (regraId != null) {
            regra = (Regra) regraDao.buscar(regraId);
            if (regra != null) {
                popularCampos();
            }
        }
    }

    public void limparCampos() {
        this.cdRegra.setValue("-1");
        this.nome.setRawValue(null);
        this.deGatilho.setRawValue(null);
        this.dtInicio.setRawValue(null);
        this.dtFim.setRawValue(null);
        this.deFrequencia.setRawValue(null);
        this.nome.setFocus(true);
    }

    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdRegra, (Object) this.regra.getCdRegra());
        zkUtils.popularCampo(this.nome, (Object) this.regra.getNome());
        zkUtils.popularCampo(this.deGatilho, (Object) this.regra.getDeGatilho());
        zkUtils.popularCampo(this.dtInicio, (Object) this.regra.getDtInicio());
        zkUtils.popularCampo(this.dtFim, (Object) this.regra.getDtFim());
        zkUtils.popularCampo(this.deFrequencia, (Object) this.regra.getDeFrequencia());
    }

    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.nome.getValue().trim().isEmpty()) {
            Clients.showNotification("Nome é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.nome.setFocus(true);
        }

        if (this.deGatilho.getValue().trim().isEmpty()) {
            Clients.showNotification("Gatilho é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.deGatilho.setFocus(true);
        }

        if (this.dtInicio.getValue() == null) {
            Clients.showNotification("Data de início é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.dtInicio.setFocus(true);
        }

        if (this.dtFim.getValue() == null) {
            Clients.showNotification("Data de fim é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.dtFim.setFocus(true);
        }

        if (this.deFrequencia.getValue().trim().isEmpty()) {
            Clients.showNotification("Frequência é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.nome, "end_center", 0);
            gerouErro = true;
            this.deFrequencia.setFocus(true);
        }

        return !gerouErro;
    }

    public void gravar() {
        if (validarCampos()) {
            regra.setNome(this.nome.getValue());
            regra.setDeGatilho(this.deGatilho.getValue());
            regra.setDtInicio(this.dtInicio.getValue());
            regra.setDtFim(this.dtFim.getValue());
            regra.setDeFrequencia(this.deFrequencia.getValue());
         
            if (Integer.parseInt(this.cdRegra.getValue()) == -1) {
                
                Integer id = regraDao.incluirAutoincrementando(regra);
                if (id != null && id > 0) {
                    Toast.show("Regra cadastrada com sucesso!",
                    "Sucesso", Toast.Type.SUCCESS);
                    this.cdRegra.setValue(regra.getCdRegra().toString());
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir a regra");
                    System.out.println("ID retornado: " + id);
                    System.out.println("Regra: " + regra);
                }
            } else {
                regra.setCdRegra(Integer.valueOf(this.cdRegra.getValue()));
                boolean atualizou = regraDao.atualizar(regra);
                if (atualizou) {
                    Toast.show("Regra atualizada com sucesso!",
                    "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }
        }
    }

    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir a regra atual?")) {
            if (!this.cdRegra.getValue().equals("-1")) {
                regraDao.excluir(regra);
                Toast.show("Regra excluída com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                limparCampos();
                this.voltar();
            } else {
                zkUtils.MensagemErro("Regra inválida");
            }
        }
    }

    public void voltar() {
        Include include = (Include) win.getParent();
        include.setSrc(this.urlRetorno);
    }

}
