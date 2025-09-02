package quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CronExpression. Para saber como programar o agendamento no CronExpression
 * veja em
 * <a href="https://www.quartz-scheduler.org/api/2.3.0/org/quartz/CronExpression.html">
 * https://www.quartz-scheduler.org/api/2.3.0/org/quartz/CronExpression.html
 * </a>
 * <br><br>
 * Exemplos:
 * <pre>
 * "* * * * * ?": a cada segundo, de cada minuto, de cada hora, de cada dia, etc., ou seja, a cada segundo é executado uma vez.
 * "0 * * * * ?": no segundo 0 de cada minuto, de cada hora, de cada dia, etc., ou seja, a cada minuto é executado uma vez.
 * "0 0 * * * ?": no segundo 0 e no minuto 0 de cada hora, de cada dia, etc., ou seja, a cada hora é executado uma vez.
 * "0 0/15 * * * ?": no segundo 0 e no minuto 0 incrementado de 15 em 15 de cada hora, de cada dia, etc., ou seja, a cada hora é executado 4 vezes sendo de quinze em quinze minutos.
 * </pre>
 *
 * @author alison
 */
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CronExpression {

    String value() default "";
}
