package quartz;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;

/**
 * JobListener Initializer. Esta classe procura todos os Jobs que estão neste
 * mesmo pacote java e inicializa eles adicionando-os ao Scheduler do Quartz.
 * Desse modo para criar Jobs basta criar uma classe que extends de Job e que
 * tenha um annotation @CronExpression definindo seu tempo de execução.
 *
 * @author alison
 */
public class JobListener implements ServletContextListener {

    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Reflections reflections = new Reflections("quartz");
            Set<Class<? extends Job>> classes = reflections.getSubTypesOf(Job.class);

            for (Class<? extends Job> j : classes) {
                JobDetail job = JobBuilder.newJob(j).withIdentity(j.getName(), Scheduler.DEFAULT_GROUP).build();
                CronExpression cronExpression = j.getAnnotation(CronExpression.class);

                if (cronExpression == null) {
                    return;
                }

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(j.getName(), Scheduler.DEFAULT_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression.value()))
                        .build();

                scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.scheduleJob(job, trigger);
                scheduler.start();

                System.out.println("QUARTZ: Criado agendamento do cron job " + j.getName());
            }

        } catch (SchedulerException e) {
            System.out.println("QUARTZ ERROR: Erro ao inicializar cron jobs " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Collection cs = new StdSchedulerFactory().getAllSchedulers();
            for (Iterator it = cs.iterator(); it.hasNext();) {
                Scheduler s = (Scheduler) it.next();
                s.clear();
                s.shutdown();
                System.out.println("QUARTZ: Destruído agenda do cron job " + s.getSchedulerName());
            }
        } catch (SchedulerException e) {
            System.out.println("QUARTZ ERROR: Ocorreu um erro ao remover agendamento do Quartz." + e.getMessage());
        }
    }
}
