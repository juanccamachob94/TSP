/**
 * Conecta con la base de datos relacional y ejecuta operaciones básicas con la base de datos.
 */
package d_datos.c_datasource;

import e_utilitaria.ExceptionFatal;
import e_utilitaria.NewHibernateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SourcePostgres implements DataSource{
    
    private static SourcePostgres instancia;

    private SourcePostgres() {
    }
    
    /**
     * Devuelve la instancia en caso de exitir. En caso contrario, crea otra.
     * @return 
     */
    public static DataSource getInstancia(){
        if(instancia == null) instancia = new SourcePostgres();
        return instancia;
    }

    @Override
    public Object consultarObjeto(String query) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Object obj = session.createQuery(query).uniqueResult();
            transaction.commit();
            session.close();
            return obj;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al consultar el objeto en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public List consultarLista(String query) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List lista;
        try {
            lista = session.createQuery(query).list();
            transaction.commit();
            session.close();
            return lista;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al consultar la lista de datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public List consultarLista(String query, int n) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List lista;
        try {
            lista = session.createQuery(query).setMaxResults(n).list();
            transaction.commit();
            session.close();
            return lista;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al consultar la lista de datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void insertar(Object obj) throws ExceptionFatal {
        try {
            List l = new ArrayList();
            l.add(obj);
            insertar(l);
        } catch (ExceptionFatal e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionFatal("Error al insertar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void insertar(List objs) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            int t = objs.size();
            for (int i = 0; i < t; i++)
                session.save(objs.get(i));
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al insertar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void guardar(Object obj) throws ExceptionFatal {
        throw new ExceptionFatal("Error al guardar los datos en el motor postgres.");
    }

    @Override
    public void actualizar(String query) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createQuery(query).executeUpdate();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al actualizar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Object obj) throws ExceptionFatal {
        try {
            List l = new ArrayList();
            l.add(obj);
            actualizar(l);
        } catch (Exception e) {
            throw new ExceptionFatal("Error al actualizar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void actualizar(List objs) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            int t = objs.size();
            for (int i = 0; i < t; i++)
                session.update(objs.get(i));
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al actualizar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String query) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
    try {
            session.createQuery(query).executeUpdate();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            throw new ExceptionFatal("Error al eliminar los datos en el motor postgres.");
        }
    }

    @Override
    public void eliminar(Object obj) throws ExceptionFatal {
        try {
            List l = new ArrayList();
            l.add(obj);
            eliminar(l);
        } catch (Exception e) {
            throw new ExceptionFatal("Error al eliminar los datos en el motor postgres. " + e.getMessage());
        }
    }

    @Override
    public void eliminar(List objs) throws ExceptionFatal {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            int t = objs.size();
            for (int i = 0; i < t; i++)
                session.delete(objs.get(i));
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            session.close();
            throw new ExceptionFatal("Error al eliminar los datos en el motor postgres. " + e.getMessage());
        }
    }

}