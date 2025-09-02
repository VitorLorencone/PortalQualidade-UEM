package exemplos.thread.controller;

import org.zkoss.zul.Window;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Timer;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class ThreadController extends Window {

    private Window win;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();
    private static final DecimalFormat df = new DecimalFormat("0");

    private String mensagemProcessamento;
    private Integer totalItensProcessados;
    private Integer totalItensParaProcessar;

    private final Timer timer = new Timer();
    private Thread threadGerarProcessamento = new Thread();

    public void onCreate() {
        this.win = (Window) getFellow("winThread");

        this.totalItensProcessados = 0;
        this.totalItensParaProcessar = 0;
        this.mensagemProcessamento = "";

        //Aqui criamos um timer para que de tempos em tempo ele verifique se a thread ainda está e execução, 
        //e caso a thread tenha terminado ele fecha a janela "busy" a qual está mostrando a barra de progresso
        this.timer.setParent(this.win);
        this.timer.setDelay(200);
        this.timer.setRunning(false);
        this.timer.setRepeats(true);
        this.timer.addEventListener(Events.ON_TIMER, (Event event) -> {
            atualizarStatusDaThread(); //de tempo em tempo chama o atualizarStatus
        });

    }

    //quando clica no botão "iniciar processamento" ele verifica se é possível iniciar a thread
    public void iniciar() {
        if (threadGerarProcessamento.isAlive()) {
            zkUtils.MensagemAtencao("Processamento já está em execução, aguarde terminar.");
        } else {
            this.totalItensProcessados = 0;
            this.totalItensParaProcessar = 0;
            this.mensagemProcessamento = "";

            timer.setRunning(true);
            threadGerarProcessamento = gerarProcessamentoEmThread();
            threadGerarProcessamento.start();
        }
    }

    //verificar se a thread terminou,
    //se terminou ele escondo de mensagem de "busy"
    //se ainda está ativa ele atualiza a mensagem de "busy"
    public void atualizarStatusDaThread() {
        if (threadGerarProcessamento.isAlive()) {
            Clients.showBusy(this.mensagemProcessamento);
        } else {
            Clients.clearBusy();
            this.timer.setRunning(false);
            Toast.show("Total de " + this.totalItensProcessados + " itens processados com sucesso!", "Geração finalizada", Toast.Type.SUCCESS);
        }
    }

    //realiza o processamento
    //esta é uma thread
    private Thread gerarProcessamentoEmThread() {
        return new Thread() {
            @Override
            public void run() {

                //tenho uma lista de itens aleatórios
                ListModelList listaLocales = new ListModelList(Locale.getAvailableLocales());
                List<Locale> itensParaProcessar = listaLocales.getInnerList();
                totalItensParaProcessar = itensParaProcessar.size(); //populo a variável global com o total de itens a serem processados

                try {
                    //para cada item eu processo e atualizo a mensagem de "busy" mostrando o progresso atual
                    for (Locale item : itensParaProcessar) {
                        System.out.println("processando " + item);
                        Thread.sleep(10);
                        totalItensProcessados++; //atualizo a variável global com o total de itens processados
                        mensagemProcessamento = getProgressBar(); //atualizo a variável que contém a mensagem a ser mostrada
                    }
                } catch (Exception ex) {
                    System.out.println("Erro " + ex.getMessage());
                }

            }
        };
    }

    //gera a mensagem que será mostrado no "busy", com a barra de progresso 
    //esta mensagem pode ser algo em html, então usamos um progressbar do bootstrap
    private String getProgressBar() {
        String porcentual = df.format((double) totalItensProcessados * 100 / totalItensParaProcessar);
        return "Processando " + totalItensProcessados + " de " + totalItensParaProcessar + "... <div class='progress' style='width: 190px;'><div class='progress-bar' role='progressbar' style='width: " + porcentual + "%;' aria-valuenow='" + porcentual + "' aria-valuemin='0' aria-valuemax='100'>" + porcentual + "%</div></div>";
    }
}
