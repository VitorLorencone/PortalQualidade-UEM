package curso.controller;

import application.service.Sessao;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import curso.dao.CursoDAO;
import curso.model.Curso;
import java.text.Normalizer;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.SimpleListModel;
import pessoa.model.Pessoa;
import utilitarios.Utils;
import utilitarios.ZkUtils;

/**
 *
 * @author alison
 */
public class CursoListController extends Window {

    private Window win;

    private Listbox vlCampo;
    private Textbox vlPesquisa;
    private Grid resultados;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final CursoDAO cursoDao = new CursoDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winCursoList");
        
        //VALIDAR SESSÃO
        //faça essa chamada caso queira que apenas usuários logados acesse esta pagina
        //caso o usuário não esteja logado, ele será redirecionado para o login
        //Sessao.getInstance().validarSessao();

        //pegamos os campos definidos no .zul e vinculamos à uma variável 
        //para pegarmos ou pularmos com valores
        this.vlPesquisa = (Textbox) getFellow("vlPesquisa");
        this.vlCampo = (Listbox) getFellow("vlCampo");
        this.resultados = (Grid) getFellow("resultados");

        filtrar();
    }

    /**
     * Busca no banco os itens do grid. Caso tenha algum filtro, este filtro é
     * adicionado à consulta ao banco.
     */
    public void filtrar() {

        String filtro = " 1=1 ";
        String ordem = " order by deTitulo ";

        //Aqui buscamos se o valor digitado está no campo de código ou no campo de título,
        //assim a pessoa tem a liberade de pesquisar por código ou título usando o mesmo campo
        String textoDaPesquisa = this.vlPesquisa.getValue();
        if (textoDaPesquisa != null && !textoDaPesquisa.isEmpty()) {
            if (NumberUtils.isCreatable(textoDaPesquisa)) {
                //Se for número filtramos por código
                filtro += " AND cdCurso = " + textoDaPesquisa;
            } else {
                //Se for texto, fazermos a "normalização" do texto digitado e tembém do valor do banco, ou seja, 
                //removemos os acentos e caracteres especiais tanto da string digitada quanto do valor do banco,
                //depois colocamos tudo em caixa alta, e aí sim comparamos. Com isso conseguimos encontrar o nome
                //independentemente se foi digitado com acentuação ou não.
                String textoTratado = Normalizer.normalize(textoDaPesquisa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase();
                filtro += " AND UPPER(npd.str100_sem_acento(deTitulo)) like '%" + textoTratado + "%'";
            }
        }

        List<Curso> cursos = cursoDao.listar(filtro, ordem);

        if (cursos != null) {
            SimpleListModel listModel = new SimpleListModel(cursos);
            this.resultados.setModel(listModel);

            //imprimindo no console.log() o mapeamento ManyToMany (APENAS PARA TESTAR O MAPEAMENTO)
            /*
            for (Curso c : cursos) {
                System.out.println("Pessoa inscrita no curso " + c.getCdCurso() + " - " + c.getDeTitulo() + " (total de " + c.getPessoas().size() + " pessoa(s)):");
                for (Pessoa p : c.getPessoas()) {
                    System.out.println("Pessoa " + p.getCdPessoa() + " - " + p.getNmPessoa() + " (total de " + p.getCursos().size() + " curso(s) inscrito(s))");
                }
            }
            */
            
            /*
             * Este é o jeito de popular um grid pelo java, sem utilizar o <template name="model"> lá no .zul
             * Recomendo utilizar o template no .zul e personalizar a aparência por lá, pois assim você tem alguns benefícios, como por exemplo,
             * funcionar a ordenação do grid por data quando clica-se no nome da coluna.
             * Este código ficará aqui apenas como exemplo.
             *
                RowRenderer rr = new RowRenderer() {
                    @Override
                    public void render(Row row, Object data, int index) throws Exception {

                        Curso curso = (Curso) data;

                        //dados do curso
                        new Label(curso.getCdCurso().toString()).setParent(row);
                        new Label(curso.getDeTitulo()).setParent(row);
                        new Label(curso.getNuVagas() != null ? curso.getNuVagas().toString() : "-").setParent(row);
                        new Label(curso.getNuCarga() != null ? curso.getNuCarga().toString() : "-").setParent(row);
                        new Label(curso.getDtCursoInicio() != null ? ut.formatarData(curso.getDtCursoInicio(), "dd/MM/yyyy") : "-").setParent(row);
                        new Label(curso.getDtCursoFim() != null ? ut.formatarData(curso.getDtCursoInicio(), "dd/MM/yyyy") : "-").setParent(row);
                        new Label(curso.getDtInscricaoInicio() != null ? ut.formatarData(curso.getDtInscricaoInicio(), "dd/MM/yyyy") : "-").setParent(row);
                        new Label(curso.getDtInscricaoFim() != null ? ut.formatarData(curso.getDtInscricaoFim(), "dd/MM/yyyy") : "-").setParent(row);
                        new Label(curso.getNmUrl()).setParent(row);

                        //botões de edição ficam no hbox
                        Hbox box = new Hbox();
                        box.setParent(row);
                        Button btnAlterar = new Button("Alterar");
                        btnAlterar.setIconSclass("z-icon-pencil");
                        btnAlterar.setClass("border-0 btn-transition btn btn-outline-warning");
                        btnAlterar.setParent(box);
                        ComponentsCtrl.applyForward(btnAlterar, "onClick=onClickedAlterarCurso(" + curso.getCdCurso().toString() + ")");

                        //ao clicar na linha abre detalhes
                        ComponentsCtrl.applyForward(row, "onClick=onClickedVerCurso(" + curso.getCdCurso().toString() + ")");
                        row.setStyle("cursor: pointer;");
                    }
                };
                this.resultados.setRowRenderer(rr);
             */
        }

    }

    /**
     * Executado quando é clicado no botão "novo curso". Abre a tela de cadastro
     * passando como parâmetro a ação "novo". Essa ação fará que apenas o botão
     * de Salvar apareça.
     */
    public void novoCurso() throws Exception {
        zkUtils.setParametro("ação", "novo");
        this.redirecionar();
    }

    /**
     * Executado com é clicado em "ver curso". Abre a tela de cadastro passando
     * como parâmetro a ação "ler" e o código do curso. Essa ação fará com que
     * os botões de Salvar e Excluir NÃO apareça.
     */
    public void onClickedVerCurso(Event event) throws Exception {
        //Long cdCurso = Long.valueOf(event.getData().toString());
        Integer cdCurso = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("curso", cdCurso);
        zkUtils.setParametro("ação", "ler");
        this.redirecionar();
    }

    /**
     * Executado quando é clicado em "editar curso". Abre a tela de cadastro
     * passando como parâmetro a ação "editar" e o código do curso. Essa ação
     * fará com que os botões de Salvar e Excluir apareça.
     */
    public void onClickedAlterarCurso(Event event) throws Exception {
        //Long cdCurso = Long.valueOf(event.getData().toString());
        Integer cdCurso = Integer.valueOf(event.getTarget().getClientAttribute("id"));
        zkUtils.setParametro("curso", cdCurso);
        zkUtils.setParametro("ação", "editar");
        this.redirecionar();
    }

    /**
     * Redireciona para a página de cadastro do Curso. É passado como parâmetro
     * a url de retorno, ou seja, a url que aparecerá no botão "Voltar" e a url
     * que será redirecionado automaticamente após salvar ou excluir os dados.
     */
    private void redirecionar() {
        Include include = (Include) win.getParent(); //pega o "pai" da desta tela, o qual é um "include"
        String urlOrigem = include.getSrc(); //o source do include é a página atual
        include.setSrc(null);
        zkUtils.setParametro("url_retorno", urlOrigem); //a página atual será a url de retorno
        include.setSrc("cadastros/curso/curso.zul"); //vai para a página de cadastro de curso
    }
}
