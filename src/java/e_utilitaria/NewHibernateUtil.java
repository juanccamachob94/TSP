/**
 * Conexión nativa a la base de datos relacional mediante Hibernate
 */
package e_utilitaria;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

public class NewHibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Logger mongoLogger = Logger.getLogger("org.hibernate");
            mongoLogger.setLevel(Level.SEVERE);
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("NewHibernateUtil : {Ha fallado la instancia de la SessionFactory " + e + "}");
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}