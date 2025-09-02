package quartz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job Exemplo 1. Utilize esta classe penas como um guia para criar seu próprio
 * CronJob.
 *
 * @author alison
 */
//@CronExpression("* * * * * ?") //->> DESCOMENTAR ESTA LINHA PARA DEFINIR O AGENDAMENTO E TESTAR
public class JobExemplo1 implements Job {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            //Coloque seu código aqui dentro do "try"
            System.out.println("[" + LocalDateTime.now().format(formatter) + "] Teste CronJob " + this.getClass().getName() + ". Favor apagar essa classe java ao desenvolver seu sistema.");
        } catch (Exception ex) {
            System.out.println("Erro no CronJob " + this.getClass().getName() + ": " + ExceptionUtils.getStackTrace(ex));
        }

    }

}
