/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2024. All rights reserved.
 */
package utilitarios;

import br.uem.npd.zhighcharts.ZHighCharts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HighCharts Util. A biblioteca br.uem.npd.zhighcharts.ZHighCharts é um
 * encapsulamento da biblioteca HighCharts em JavaScript
 * (https://www.highcharts.com), para facilitar usar com Java e ZK, sendo seu
 * código-fonte disponível no GitLab da UEM.
 *
 * @author alison
 */
public class HighChartsUtil {

    //Para ver as configurações disponíveis: https://api.highcharts.com/highcharts/. 
    String titleSize = "1.1rem"; //tamanho do h5 no tema archtectUI
    String textSize = "0.88rem"; //tamanho do texto do body no tema archtectUI
    String textColor = "#495057"; //cor do texto do body no tema archtectUI
    String textWeight = "normal";
    String fontFamily = "system-ui, -apple-system, \"Segoe UI\", Roboto, \"Helvetica Neue\", \"Noto Sans\", \"Liberation Sans\", Arial, sans-serif"; //fonte padrão do tema archtectUI/Bootstrap
    String chartColors = "'#509EE3', '#88BF4D', '#A989C5', '#EF8C8C', '#F9D45C', '#F2A86F', '#98D9D9', '#7172AD'"; //metabase colors

    public void setParametrosPadroesGraficoPizza(ZHighCharts chart) {

        chart.setType("pie");

        chart.setTooltipFormatter(""
                + "function formatTooltip(obj){"
                + "     return '<b>' + obj.key + ': '+ Highcharts.numberFormat(obj.percentage, 2, ',', '.') +'%</b>'"
                + "}");

        chart.setPlotOptions("{"
                + "     pie:{"
                + "         allowPointSelect: true, "
                + "         cursor: 'pointer',"
                + "         colors: [" + chartColors + "],"
                + "         dataLabels: { "
                + "             enabled: true, "
                + "             color: '" + textColor + "', "
                + "             connectorColor: '" + textColor + "',"
                + "             formatter: function() { return '<b>'+ this.point.name +'</b><br>'+ Highcharts.numberFormat(this.y, 0, ',', '.') ; },"
                + "             style: {"
                + "                 fontSize: '" + textSize + "',"
                + "                 textOutline: 'none',"
                + "                 opacity: 1,"
                + "                 fontWeight: '" + textWeight + "',"
                + "                 fontFamily: '" + fontFamily + "'"
                + "             }"
                + "         }"
                + "     }"
                + "}");

        chart.setTitleOptions("{"
                + "     style: {"
                + "         color: '" + textColor + "',"
                + "         fontWeight: '" + textWeight + "',"
                + "         fontFamily: '" + fontFamily + "',"
                + "         fontSize: '" + titleSize + "'"
                + "     }"
                + "}");

        chart.setOptions("{"
                + "     renderTo: 'containter',"
                + "     borderWidth: 0,"
                + "     backgroundColor: null,"
                + "}");
    }

    public void setParametrosPadroesGraficoBarra(ZHighCharts chart, List<Object[]> rows) {

        //calcula volume total para calcular porcentual no tooltip
        int a = 0;
        long volumeTotal = 0;
        StringBuilder rotulo = new StringBuilder();
        for (Object[] row : rows) {
            rotulo.append("'");
            rotulo.append((a == rows.size() - 1) ? "'" : "', ");
            volumeTotal = volumeTotal + (long) row[1];
            a++;
        }

        chart.setType("column");

        chart.setTooltipFormatter(""
                + "function formatTooltip(obj) { "
                + "     return obj.x + ' (<b>' + Highcharts.numberFormat((100 * obj.y) / " + volumeTotal + ", 2) +'%</b>)'; "
                + "}"
        );

        chart.setPlotOptions("{"
                + "     column: { "
                + "         dataLabels: { "
                + "             enabled: true, "
                + "             formatter: function() { "
                + "                         return this.y ; "
                + "             },"
                + "             style: {"
                + "                 fontSize: '" + textSize + "',"
                + "                 textOutline: 'none',"
                + "                 opacity: 1,"
                + "                 fontWeight: '" + textWeight + "',"
                + "                 fontFamily: '" + fontFamily + "'"
                + "             }"
                + "         }"
                + "     } "
                + "}"
        );

        chart.setxAxisOptions("{"
                + "     categories: [" + rotulo + "], "
                + "     gridLineWidth: 0, "
                + "     labels: {"
                + "         enabled: true, "
                + "         style: {"
                + "             fontSize: '" + textSize + "', "
                + "             fontFamily: '" + fontFamily + "', "
                + "             color: '" + textColor + "'"
                + "         }"
                + "     }"
                + "}"
        );

        chart.setyAxisOptions("{"
                + "     enabled: true, "
                + "     min: 0, "
                + "     gridLineWidth: 0, "
                + "     lineWidth: 1, "
                + "     overflow: 'justify'"
                + "}"
        );

        chart.setLegend("{"
                + "     enabled: false"
                + "}"
        );
        
        chart.setTitleOptions("{"
                + "     style: {"
                + "         color: '" + textColor + "',"
                + "         fontWeight: '" + textWeight + "',"
                + "         fontFamily: '" + fontFamily + "',"
                + "         fontSize: '" + titleSize + "'"
                + "     }"
                + "}");

        chart.setOptions("{"
                + "     lang: {"
                + "         decimalPoint: ',', "
                + "         thousandsSeparator: '.'"
                + "     } "
                + "}"
        );

    }
}
