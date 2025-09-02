package utilitarios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Dias Úteis, Final de Semana e Feriados. Essa classe fornece, por meio dos
 * feriados que tem dias fixo e dos feriados que tem dias móveis dependendo do
 * ano, informações para sabermos se um dia é útil, se é feriado ou se é final
 * de semana, assim como a contagem de dias úteis entre um determinado período.
 * Adaptação do algoritmo https://github.com/amandalima/workday-calculator para
 * Java 7 ou inferior, assim como aperfeiçoamentos do código e informações
 * obtidas.
 *
 * @author alison
 */
public class Feriados {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Feriados fixos, ou seja, com mesmo dia e mês independente do ano. Caso
     * tenha feriados novos ou feriados locais (municipais ou estaduais), é
     * necessário adicionar esse dia aqui.
     */
    private enum FeriadosFixos {

        ANO_NOVO(1, 1),
        TIRADENTES(21, 4),
        DIA_DO_TRABALHO(1, 5),
        INDEPENDENCIA_DO_BRASIL(7, 9),
        PADROEIRA_DO_BRASIL(12, 10),
        FINADOS(2, 11),
        PROCLAMACAO_DA_REPUBLICA(15, 11),
        NATAL(25, 12);

        private int dia;
        private int mes;

        FeriadosFixos(int dia, int mes) {
            this.dia = dia;
            this.mes = mes;
        }

        public int getDia() {
            return this.dia;
        }

        public int getMes() {
            return this.mes;
        }
    }

    /**
     * Retorna uma instância de Calendar. Usar o método Calendar.getInstance()
     * não é uma abordagem recomendada, pois retornará uma instância subjetiva
     * ao local padrão. Ele pode retornar um BuddhistCalendar para tailandês ou
     * JapaneseImperialCalendar para o Japão.
     */
    private Calendar getNewCalandarInstance() {
        Calendar calendario = new GregorianCalendar();
        resetTime(calendario);
        return calendario;
    }

    /**
     * Reseta as horas, minutos, segundos e milisegundos do Calendar, para que
     * as comparação entre datas sempre ocorra corretamente.
     */
    private void resetTime(Calendar calendario) {
        calendario.set(Calendar.HOUR, 0);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Feriado móvel que é o dia que irá cair a páscoa naquele ano. Esse dia é
     * utilizado para calcular outros feriados móveis, Sexta-feira Santa,
     * Carnaval e Corpus Christi.
     */
    private Date getPascoaNoAno(int ano) {
        Calendar calendar = getNewCalandarInstance();

        //Excessões da regra de cálculo
        if (ano == 2076) {
            calendar.set(ano, 4 - 1, 19);
            return calendar.getTime(); //Date.of(year, 4, 19);
        }

        if (ano == 2049) {
            calendar.set(ano, 4 - 1, 18);
            return calendar.getTime(); //Date.of(year, 4, 18);
        }

        //Algoritmo de Meeus/Jones/Butcher
        int a = ano % 19;
        int b = ano / 100;
        int c = ano % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int n = h + l - 7 * m + 114;
        int mes = n / 31;
        int dia = (n % 31) + 1;
        calendar.set(ano, mes - 1, dia);

        return calendar.getTime(); //LocalDate.of(year, month, day);
    }

    /**
     * Feriado móvel que é o dia que irá cair o dia de Carnaval naquele ano.
     */
    private Date getCarnavalNoAno(int ano) {
        Calendar calendar = getNewCalandarInstance();
        calendar.setTime(getPascoaNoAno(ano));
        calendar.add(Calendar.DATE, -47);
        return calendar.getTime(); //getPascoaForYear(year).minusDays(47);
    }

    /**
     * Feriado móvel que é o dia que irá cair o dia de Corpus Christi naquele
     * ano.
     */
    private Date getCorpusChristiNoAno(int ano) {
        Calendar calendar = getNewCalandarInstance();
        calendar.setTime(getPascoaNoAno(ano));
        calendar.add(Calendar.DATE, 60);
        return calendar.getTime(); //getPascoaForYear(year).plusDays(60);
    }

    /**
     * Feriado móvel que é o dia que irá cair o dia de Paixão de Cristo naquele
     * ano.
     */
    private Date getPaixaoDeCristoNoAno(int ano) {
        Calendar calendar = getNewCalandarInstance();
        calendar.setTime(getPascoaNoAno(ano));
        calendar.add(Calendar.DATE, -2);
        return calendar.getTime(); //getPascoaForYear(year).minusDays(2);
    }

    private boolean isFeriadoFixo(Calendar calendario) {
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        for (FeriadosFixos feriado : FeriadosFixos.values()) {
            if (feriado.getDia() == dia && feriado.getMes() == mes) {
                return true;
            }
        }
        return false;
    }

    private boolean isFeriadoMovel(Calendar calendario) {
        resetTime(calendario);
        int ano = calendario.get(Calendar.YEAR);
        return getPascoaNoAno(ano).compareTo(calendario.getTime()) == 0
                || getCarnavalNoAno(ano).compareTo(calendario.getTime()) == 0
                || getCorpusChristiNoAno(ano).compareTo(calendario.getTime()) == 0
                || getPaixaoDeCristoNoAno(ano).compareTo(calendario.getTime()) == 0;
    }

    private Calendar getCalendarioFromDiaMesAno(int dia, int mes, int ano) {
        Calendar calendario = getNewCalandarInstance();
        calendario.set(ano, mes - 1, dia);
        return calendario;
    }

    private Calendar getCalendarioFromDate(Date data) {
        Calendar calendario = getNewCalandarInstance();
        calendario.setTime(data);
        return calendario;
    }

    /**
     * Retorna se um determinado dia é feriado ou não.
     *
     * @param calendario é um java.util.Calendar
     * @return true se é feriado, false caso contrário
     */
    public boolean isFeriado(Calendar calendario) {
        return isFeriadoMovel(calendario) || isFeriadoFixo(calendario);
    }

    /**
     * Retorna se um determinado dia é feriado ou não.
     *
     * @param data é um java.util.Date
     * @return true se é feriado, false caso contrário
     */
    public boolean isFeriado(Date data) {
        Calendar calendar = getCalendarioFromDate(data);
        return isFeriado(calendar);
    }

    /**
     * Retorna se um determinado dia é feriado ou não.
     *
     * @param dia é o dia do mês
     * @param mes é o valor numérico do mês, começando com Janeiro sendo 1
     * @param ano é o ano
     * @return true se é feriado, false caso contrário
     */
    public boolean isFeriado(int dia, int mes, int ano) {
        Calendar calendar = getCalendarioFromDiaMesAno(dia, mes, ano);
        return isFeriado(calendar);
    }

    /**
     * Retorna se um determinado dia é sábado ou domingo, ou seja, fim de
     * semana.
     *
     * @param calendario é um java.util.Calendar
     * @return true se for sábado ou domingo (fim de semana), false caso
     * contrário
     */
    public boolean isSabadoOuDomingo(Calendar calendario) {
        return calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * Retorna se um determinado dia é sábado ou domingo, ou seja, fim de
     * semana.
     *
     * @param data é um java.util.Date
     * @return true se for sábado ou domingo (fim de semana), false caso
     * contrário
     */
    public boolean isSabadoOuDomingo(Date data) {
        Calendar calendar = getCalendarioFromDate(data);
        return isSabadoOuDomingo(calendar);
    }

    /**
     * Retorna se um determinado dia é sábado ou domingo, ou seja, fim de
     * semana.
     *
     * @param dia é o dia do mês
     * @param mes é o valor numérico do mês, começando com Janeiro sendo 1
     * @param ano é o ano
     * @return true se for sábado ou domingo (fim de semana), false caso
     * contrário
     */
    public boolean isSabadoOuDomingo(int dia, int mes, int ano) {
        Calendar calendar = getCalendarioFromDiaMesAno(dia, mes, ano);
        return isSabadoOuDomingo(calendar);
    }

    /**
     * Retorna se um determinado dia é "dia útil". Um "dia útil" é um dia que
     * não é feriado, nem sábado e nem domingo.
     *
     * @param calendario é um java.util.Calendar
     * @return true se for dia útil, false caso contrário
     */
    public boolean isDiaUtil(Calendar calendario) {
        return !isFeriado(calendario) && !isSabadoOuDomingo(calendario);
    }

    /**
     * Retorna se um determinado dia é "dia útil". Um "dia útil" é um dia que
     * não é feriado, nem sábado e nem domingo.
     *
     * @param data é um java.util.Date
     * @return true se for dia útil, false caso contrário
     */
    public boolean isDiaUtil(Date data) {
        Calendar calendar = getCalendarioFromDate(data);
        return isDiaUtil(calendar);
    }

    /**
     * Retorna se um determinado dia é "dia útil". Um "dia útil" é um dia que
     * não é feriado, nem sábado e nem domingo.
     *
     * @param dia é o dia do mês
     * @param mes é o valor numérico do mês, começando com Janeiro sendo 1
     * @param ano é o ano
     * @return true se for dia útil, false caso contrário
     */
    public boolean isDiaUtil(int dia, int mes, int ano) {
        Calendar calendar = getCalendarioFromDiaMesAno(dia, mes, ano);
        return isDiaUtil(calendar);
    }

    /**
     * Retorna quantidade de dias úteis dentro do um período.
     *
     * @param dataInicio é um java.util.Calendar que representa o dia de início
     * do período
     * @param dataFim é um java.util.Calendar que representa o dia final do
     * período
     * @return valor inteiro que é a quantidade de dias úteis dentro do período
     * escolhido
     */
    public int countDiasUteis(Calendar dataInicio, Calendar dataFim) {
        int diasUteis = 0;
        while (!dataInicio.after(dataFim)) {
            if (isDiaUtil(dataInicio)) {
                diasUteis++;
            }
            dataInicio.add(Calendar.DAY_OF_MONTH, 1);
        }
        return diasUteis;
    }

    /**
     * Retorna quantidade de dias úteis dentro do um período.
     *
     * @param dataInicio é um java.util.Date que representa o dia de início do
     * período
     * @param dataFim é um java.util.Date que representa o dia final do período
     * @return valor inteiro que é a quantidade de dias úteis dentro do período
     * escolhido
     */
    public int countDiasUteis(Date dataInicio, Date dataFim) {
        Calendar inicio = getCalendarioFromDate(dataInicio);
        Calendar fim = getCalendarioFromDate(dataFim);
        return countDiasUteis(inicio, fim);
    }

    /**
     * Retorna quantidade de dias úteis dentro do um período.
     *
     * @param diaInicio é o dia do mês que representa o dia de início do período
     * @param mesInicio é o valor numérico do mês, começando com Janeiro sendo
     * 1, que representa o dia de início do período
     * @param anoInicio é o ano, que representa o dia de início do período
     * @param diaFim é o dia do mês, que representa o dia de final do período
     * @param mesFim é o valor numérico do mês, começando com Janeiro sendo 1,
     * que representa o dia de final do período
     * @param anoFim é o ano, que representa o dia de final do período
     * @return valor inteiro que é a quantidade de dias úteis dentro do período
     * escolhido
     */
    public int countDiasUteis(int diaInicio, int mesInicio, int anoInicio, int diaFim, int mesFim, int anoFim) {
        Calendar inicio = getCalendarioFromDiaMesAno(diaInicio, mesInicio, anoInicio);
        Calendar fim = getCalendarioFromDiaMesAno(diaFim, mesFim, anoFim);
        return countDiasUteis(inicio, fim);
    }

    /**
     * Retorna a lista de feriados naquele ano.
     *
     * @param ano é o ano
     * @return lista de java.util.Date com os dias que é feriado
     */
    public List<Date> getFeriadosNoAno(int ano) {
        List feriadosList = new ArrayList();
        feriadosList.add(getCarnavalNoAno(ano));
        feriadosList.add(getPaixaoDeCristoNoAno(ano));
        feriadosList.add(getPascoaNoAno(ano));
        feriadosList.add(getCorpusChristiNoAno(ano));
        for (FeriadosFixos feriado : FeriadosFixos.values()) {
            Calendar calendario = getNewCalandarInstance();
            calendario.set(ano, feriado.getMes() - 1, feriado.getDia());
            Date data = calendario.getTime();
            feriadosList.add(data);
        }
        return feriadosList;
    }

    /**
     * Imprime no console/output o resultado do isFeriado(data_escolhida).
     */
    public void printTestIsFeriado(Calendar calendario) {
        System.out.println("DATA ESCOLHIDA: " + simpleDateFormat.format(calendario.getTime()) + ", É FERIADO? " + (isFeriado(calendario) ? "SIM" : "NÃO"));
    }

    /**
     * Imprime no console/output o resultado do isFeriado(data_escolhida).
     * Utilizado para fins de teste.
     */
    public void printTestIsFeriado(Date data) {
        Calendar calendar = getCalendarioFromDate(data);
        printTestIsFeriado(calendar);
    }

    /**
     * Imprime no console/output o resultado do isFeriado(data_escolhida).
     * Utilizado para fins de teste.
     */
    public void printTestIsFeriado(int dia, int mes, int ano) {
        Calendar calendar = getCalendarioFromDiaMesAno(dia, mes, ano);
        printTestIsFeriado(calendar);
    }

    /**
     * Imprime no console/output o resultado do isDiaUtil(data_escolhida).
     * Utilizado para fins de teste.
     */
    public void printTestIsDiaUtil(Calendar calendario) {
        System.out.println("DATA ESCOLHIDA: " + simpleDateFormat.format(calendario.getTime()) + ", É DIA ÚTIL? " + (isDiaUtil(calendario) ? "SIM" : "NÃO"));
    }

    /**
     * Imprime no console/output o resultado do isDiaUtil(data_escolhida).
     * Utilizado para fins de teste.
     */
    public void printTestIsDiaUtil(Date data) {
        Calendar calendar = getCalendarioFromDate(data);
        printTestIsDiaUtil(calendar);
    }

    /**
     * Imprime no console/output o resultado do isDiaUtil(data_escolhida).
     * Utilizado para fins de teste.
     */
    public void printTestIsDiaUtil(int dia, int mes, int ano) {
        Calendar calendar = getCalendarioFromDiaMesAno(dia, mes, ano);
        printTestIsDiaUtil(calendar);
    }

    /**
     * Imprime no console/output a listagem de todos os feriados do ano.
     * Utilizado para fins de teste.
     */
    public void printFeriadosNoAno(int ano) {
        System.out.println("CARNAVAL: " + simpleDateFormat.format(getCarnavalNoAno(ano)));
        System.out.println("PAIXAO_DE_CRISTO: " + simpleDateFormat.format(getPaixaoDeCristoNoAno(ano)));
        System.out.println("PASCOA: " + simpleDateFormat.format(getPascoaNoAno(ano)));
        System.out.println("CORPUS_CHRISTI: " + simpleDateFormat.format(getCorpusChristiNoAno(ano)));
        for (FeriadosFixos feriado : FeriadosFixos.values()) {
            System.out.println(feriado + ": " + feriado.getDia() + "/" + feriado.getMes() + "/" + ano);
        }
    }

}
