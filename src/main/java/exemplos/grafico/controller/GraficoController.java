package exemplos.grafico.controller;

import br.uem.npd.zhighcharts.SimpleExtXYModel;
import br.uem.npd.zhighcharts.ZHighCharts;
import curso.dao.CursoDAO;
import java.util.List;
import java.util.Locale;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.Window;
import utilitarios.HighChartsUtil;

/**
 * Exemploí ZHighCharts. A biblioteca em /WEB-INF/lib/zhighcharts.jar foi
 * baseada no modelo diponível em
 * https://www.zkoss.org/wiki/Small_Talks/2012/November/ZHighCharts:_Integrating_ZK_with_Highcharts
 * e atualizado para a versão 9 da biblioteca HighCharts dsponível em
 * https://www.highcharts.com
 *
 * @author alison
 */
public class GraficoController extends Window {

    private Window win;

    HighChartsUtil highChartsUtil = new HighChartsUtil();

    //Valores
    private Label valor1;
    private Label valor2;
    private Label valor3;
    private Label valor4;

    //Graficos
    private ZHighCharts graficoPizza;
    private ZHighCharts graficoColuna;

    public void onCreate() {
        this.win = (Window) getFellow("winGraficos");

        valor1 = (Label) getFellow("valor_1");
        valor2 = (Label) getFellow("valor_2");
        valor3 = (Label) getFellow("valor_3");
        valor4 = (Label) getFellow("valor_4");

        graficoPizza = (ZHighCharts) getFellow("graficoPizza");
        graficoColuna = (ZHighCharts) getFellow("graficoColuna");

        this.inicializaCardsValores();
        this.criarGraficoPizza();
        this.criarGraficoColuna();
    }

    /**
     * popula cards com valores
     */
    private void inicializaCardsValores() {
        valor1.setValue(this.getTotalCurso().toString());
        valor2.setValue(this.getTotalPessoa().toString());
        valor3.setValue(this.getTotalInscricao().toString());
        valor4.setValue(this.getTotalCancelamento().toString());
    }

    /**
     * popula grafico de pizza
     */
    private void criarGraficoPizza() {

        List<Object[]> rows = this.getListaTipoCurso();

        SimplePieModel modeloPizza = new SimplePieModel();

        Long totalAlunos = 0L;
        for (Object[] row : rows) {
            modeloPizza.setValue((short) row[0], (long) row[1]);
            totalAlunos += (long) row[1];
        }

        graficoPizza.setTitle(String.format(new Locale("pt"), "%,d", totalAlunos) + " Alunos");
        highChartsUtil.setParametrosPadroesGraficoPizza(graficoPizza);
        graficoPizza.setModel(modeloPizza);

    }

    /**
     * popula grafico de coluna
     */
    public void criarGraficoColuna() {

        List<Object[]> rows = this.getListaTipoCurso();

        SimpleExtXYModel modeloColuna = new SimpleExtXYModel();

        //adiciona dados ao modelo
        int x = 0;
        for (Object[] row : rows) {
            modeloColuna.addValue("Tipo", x, (long) row[1]);
            x++;
        }

        //inicializa gráfico
        graficoColuna.setTitle("Cursos por Tipo - Gráfico de Colunas");
        highChartsUtil.setParametrosPadroesGraficoBarra(graficoColuna, rows);
        graficoColuna.setModel(modeloColuna);

    }

    private List<Object[]> getListaTipoCurso() {
        CursoDAO cursoDao = new CursoDAO();
        List<Object[]> lista = cursoDao.executarSelectHql("SELECT c.tpCurso, COUNT(c) FROM Curso c GROUP BY c.tpCurso");
        return lista;
    }

    private Integer getTotalCurso() {
        CursoDAO cursoDao = new CursoDAO();
        Integer contagem = cursoDao.contar("1=1");
        return contagem;
    }

    private Integer getTotalPessoa() {
        return 0;
    }

    private Integer getTotalInscricao() {
        return 0;
    }

    private Integer getTotalCancelamento() {
        return 0;
    }

}
