package hibernate.anotaciones.util;

import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.annotations.Param;

public class ObjectModelDAO {

    private static Session sesion;
    private static Transaction tx;

    public static Integer saveObject(Object objModel) {
        Integer id = -1;

        try {
            iniciaOperacion();
            id = (Integer) sesion.save(objModel);
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        }finally {
            terminaOperacion();
        }

        return id;
    }

    public ObjectModelDAO() {
    }

    /**
     *
     * @param sw
     * <blockquote><pre>
     *     true: para iniciar operacion
     * </pre></blockquote><p>
     */
    public ObjectModelDAO(boolean sw) {
        if (sw) {
            iniciaOperacion();
        }
    }

    public static void updateObject(Object objModel){
        try {
            iniciaOperacion();
            sesion.update(objModel);
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        }finally {
            terminaOperacion();
        }
    }

    public static void deleteObject(Object objModel) {
        try {
            iniciaOperacion();
            sesion.delete(objModel);
            tx.commit();
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        } finally {
            terminaOperacion();
        }
    }

    public static <T> T getObject(int idObject, Class<T> type) {
        //hacer que se insancie el objeto de la clase de argumento ** 

        T objModel = null;
        try {
            iniciaOperacion();
            objModel = (T) sesion.get(type, idObject);
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        } finally {
            terminaOperacion();
        }

        return objModel;
    }

    /**
     * El codigo {@code ObjectModelDAO} class represents character strings. All
     * string literals in Java programs, such as {@code "abc"}, are implemented
     * as instances of this class.
     * <p>
     * Strings are constant; their values cannot be changed after they are
     * created. String buffers support mutable strings. Because String objects
     * are immutable they can be shared. For example:
     * <blockquote><pre>
     *     String str = "abc";
     * </pre></blockquote><p>
     *
     * @param SQL
     * @return Lista con los campos de consulta
     * @throws HibernateException
     * @see DirectorioPK
     */
    public static List<List> getResultQuery(String SQL) {
        List<List> listaObjectos = null;

        try {
            iniciaOperacion();
            listaObjectos = sesion.createQuery(SQL).list(); //ejemplo : "from Contacto"
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        } finally {
            terminaOperacion();
        }

        return listaObjectos;
    }

    private static void iniciaOperacion() {
        try {
            //System.out.println("\t\t\t\t save5 " + sesion);
            sesion = HibernateUtil.getSessionFactory().openSession();
            //System.out.println("\t\t\t\t save6 " + sesion);
            //sesion = HibernateUtil.getSessionFactory().getCurrentSession();
            //System.out.println("\t\t\t\t save7 " + sesion);
            tx = sesion.beginTransaction();
            //System.out.println("\t\t\t\t save8 " + sesion);
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        }
    }

    private static void manejaExcepcion(HibernateException he) throws HibernateException {
        if (tx.isParticipating()) {
            tx.rollback();
            JOptionPane.showMessageDialog(null, "rollback");
        }

        JOptionPane.showMessageDialog(null,he , "Error", JOptionPane.ERROR_MESSAGE);
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos \n\t", he);
    }

    private static void terminaOperacion() {
        try {
            tx.commit();
            sesion.close();
        } catch (HibernateException ex) {
            manejaExcepcion(ex);
        }
    }

    /*
     public static void almacenaEntidad(Object entidad) throws HibernateException
     {
       

     try
     {
     dummy.iniciaOperacion();
     dummy.getSession().saveOrUpdate(entidad);
     dummy.getSession().flush();
     }
     catch (HibernateException he)
     {
     dummy.manejaExcepcion(he);
     }
     finally
     {
     dummy.terminaOperacion();
     }
     }

     public static <T> T getEntidad(Serializable id, Class<T> claseEntidad) throws HibernateException
     {
     AbstractDAO dummy = new AbstractDAO(){};

     T objetoRecuperado = null;

     try
     {
     dummy.iniciaOperacion();
     objetoRecuperado = (T) dummy.getSession().get(claseEntidad, id);
     }
     catch (HibernateException he)
     {
     dummy.manejaExcepcion(he);
     }
     finally
     {
     dummy.terminaOperacion();
     }

     return objetoRecuperado;
     }

     public static <T> List<T> getListaEntidades(Class<T> claseEntidad) throws HibernateException
     {
     AbstractDAO dummy = new AbstractDAO(){};

     List<T> listaResultado = null;

     try
     {
     dummy.iniciaOperacion();
     listaResultado = dummy.getSession().createQuery("FROM " + claseEntidad.getSimpleName()).list();
     }
     catch (HibernateException he)
     {
     dummy.manejaExcepcion(he);
     }
     finally
     {
     dummy.terminaOperacion();
     }

     return listaResultado;
     }
     */
}
