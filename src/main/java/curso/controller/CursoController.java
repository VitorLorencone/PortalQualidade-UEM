package curso.controller;

import java.text.ParseException;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import curso.dao.CursoDAO;
import curso.model.Curso;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Intbox;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class CursoController extends Window {

    private Window win;
    private Label cdCurso;
    private Textbox deTitulo;
    private Listbox tpCurso;
    private Intbox nuCarga;
    private Datebox dtCursoInicio;
    private Datebox dtCursoFim;
    private Intbox nuVagas;
    private Datebox dtInscricaoInicio;
    private Datebox dtInscricaoFim;
    private Textbox nmUrl;

    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnCancelar;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private CursoDAO cursoDao = new CursoDAO();

    //A variável curso inicialmente é um Curso vazio,
    //caso tenha algum id passado por parâmetro, o curso será buscado no banco e vinculado à esta variável
    private Curso curso = new Curso();

    private String urlRetorno = "";

    public void onCreate() {

        this.win = ((Window) getFellow("winCurso"));

        this.cdCurso = (Label) getFellow("cdCurso");
        this.deTitulo = (Textbox) getFellow("deTitulo");
        this.tpCurso = (Listbox) getFellow("tpCurso");
        this.nuCarga = (Intbox) getFellow("nuCarga");
        this.dtCursoInicio = (Datebox) getFellow("dtCursoInicio");
        this.dtCursoFim = (Datebox) getFellow("dtCursoFim");
        this.nuVagas = (Intbox) getFellow("nuVagas");
        this.dtInscricaoInicio = (Datebox) getFellow("dtInscricaoInicio");
        this.dtInscricaoFim = (Datebox) getFellow("dtInscricaoFim");
        this.nmUrl = (Textbox) getFellow("nmUrl");
        this.btnSalvar = (Button) getFellow("salvar");
        this.btnExcluir = (Button) getFellow("excluir");
        this.btnCancelar = (Button) getFellow("cancelar");

        //pega os parâmetros que veio da outra página
        Integer cursoId = (Integer) zkUtils.getParametro("curso");
        String acao = (String) zkUtils.getParametro("ação");
        urlRetorno = (String) zkUtils.getParametro("url_retorno");

        //mostra os botões de acordo com a ação
        if (acao.equals("novo")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.cdCurso.setVisible(false);
        } else if (acao.equals("editar")) {
            this.btnSalvar.setVisible(true);
            this.btnCancelar.setVisible(true);
            this.btnExcluir.setVisible(true);
        }

        limparCampos();

        //caso tenha algum id de curso que veio por parâmetro, 
        //então busca esse curso no banco e popula os campos com os valores
        if (cursoId != null) {
            curso = (Curso) cursoDao.buscar(cursoId);
            popularCampos();
        }

    }

    /**
     * Limpa os campos do .zul.
     */
    public void limparCampos() {
        this.cdCurso.setValue("-1");
        this.deTitulo.setRawValue(null);
        zkUtils.selecionarLista(tpCurso, 0);
        this.nuCarga.setRawValue(null);
        this.dtCursoInicio.setRawValue(null);
        this.dtCursoFim.setRawValue(null);
        this.nuVagas.setRawValue(null);
        this.dtInscricaoInicio.setRawValue(null);
        this.dtInscricaoFim.setRawValue(null);
        this.nmUrl.setValue("http://");
        this.deTitulo.setFocus(true);
    }

    /**
     * Popula os campos .zul com os calores do banco.
     */
    public void popularCampos() {
        limparCampos();
        zkUtils.popularCampo(this.cdCurso, (Object) this.curso.getCdCurso());
        zkUtils.popularCampo(this.deTitulo, (Object) this.curso.getDeTitulo());
        zkUtils.selecionarLista(this.tpCurso, this.curso.getTpCurso().toString());
        zkUtils.popularCampo(this.nuCarga, (Object) this.curso.getNuCarga());
        zkUtils.popularCampo(this.dtCursoInicio, (Object) this.curso.getDtCursoInicio());
        zkUtils.popularCampo(this.dtCursoFim, (Object) this.curso.getDtCursoFim());
        zkUtils.popularCampo(this.nuVagas, (Object) this.curso.getNuVagas());
        zkUtils.popularCampo(this.dtInscricaoInicio, (Object) this.curso.getDtInscricaoInicio());
        zkUtils.popularCampo(this.dtInscricaoFim, (Object) this.curso.getDtInscricaoFim());
        zkUtils.popularCampo(this.nmUrl, (Object) this.curso.getNmUrl());
    }

    /**
     * Valida se os campos preenchidos estão corretos.
     */
    public boolean validarCampos() throws ParseException {
        boolean gerouErro = false;

        if (this.deTitulo.getValue().trim().equals("")) {
            Clients.showNotification("Título é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.deTitulo, "end_center", 0);
            gerouErro = true;
            this.deTitulo.setFocus(true);
        }

        if (this.tpCurso.getSelectedIndex() == 0) {
            Clients.showNotification("Selecione o Tipo de Curso!", Clients.NOTIFICATION_TYPE_WARNING, this.tpCurso, "end_center", 0);
            gerouErro = true;
            this.tpCurso.setFocus(true);
        }

        if (this.nmUrl.getValue().trim().equals("")) {
            Clients.showNotification("URL é obrigatória!", Clients.NOTIFICATION_TYPE_WARNING, this.nmUrl, "end_center", 0);
            gerouErro = true;
            this.nmUrl.setFocus(true);
        } else if (!utils.validaURL(this.nmUrl.getValue())) {
            Clients.showNotification("Digite uma URL válida!", Clients.NOTIFICATION_TYPE_WARNING, this.nmUrl, "end_center", 0);
            gerouErro = true;
            this.nmUrl.setFocus(true);
        }

        return !gerouErro;
    }

    /**
     * Grava os dados no banco.
     */
    public void gravar() throws ParseException {
        if (validarCampos()) {
            //se validou os dados prenchidos, então atualizamos nosso objeto Curso com os dados preenchidos
            curso.setDeTitulo(this.deTitulo.getValue());
            curso.setTpCurso(this.tpCurso.getSelectedItem().getValue() != null ? Short.valueOf(this.tpCurso.getSelectedItem().getValue().toString()) : null);
            curso.setNuCarga(this.nuCarga.getValue() != null ? this.nuCarga.getValue().shortValue() : null);
            curso.setDtCursoInicio(this.dtCursoInicio.getValue());
            curso.setDtCursoFim(this.dtCursoFim.getValue());
            curso.setNuVagas(this.nuVagas.getValue() != null ? this.nuVagas.getValue().shortValue() : null);
            curso.setDtInscricaoInicio(this.dtInscricaoInicio.getValue());
            curso.setDtInscricaoFim(this.dtInscricaoFim.getValue());
            curso.setNmUrl(this.nmUrl.getValue());

            if (Integer.parseInt(this.cdCurso.getValue()) == -1) {
                //se não tem id significa que é objeto novo, então incluímos no banco
                Integer id = cursoDao.incluirAutoincrementando(curso);
                if (id != null && id > 0) {
                    Toast.show("Curso cadastrado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.cdCurso.setValue(curso.getCdCurso().toString());
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível incluir");
                }
            } else {
                //se já tem id significa que é objeto que já existe no banco, então fazemos update no banco
                curso.setCdCurso(Integer.valueOf(this.cdCurso.getValue()));
                boolean atualizou = cursoDao.atualizar(curso);
                if (atualizou) {
                    Toast.show("Curso atualizado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                    this.voltar();
                } else {
                    zkUtils.MensagemErro("Houve um erro e não foi possível atualizar");
                }
            }

        }
    }

    /**
     * Exclui o objeto Curso do bando de dados.
     */
    public void excluir() {
        if (zkUtils.MensagemConfirmacao("Deseja excluir o curso atual?")) {
            if (!this.cdCurso.getValue().equals("-1")) {
                cursoDao.excluir(curso);
                Toast.show("Curso excluído com sucesso!", "Sucesso", Toast.Type.SUCCESS);
                limparCampos();
                this.voltar();
            } else {
                zkUtils.MensagemErro("Curso inválido");
            }
        }
    }

    /**
     * Volta para a url de retorno.
     */
    public void voltar() {
        Include include = (Include) win.getParent();
        String urlOrigem = include.getSrc();
        include.setSrc(this.urlRetorno);
    }
}
