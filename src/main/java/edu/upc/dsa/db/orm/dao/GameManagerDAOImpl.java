package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameManagerDAOImpl implements GameManagerDAO {

    @Override
    public void addUsuario(String id, String name, String apellidos,
                           String contra, String mail,
                           String pregunta, String respuesta)
            throws UsuarioYaRegistradoException {

        Session session = FactorySession.openSession();
        try {

            List<String> filtros = Arrays.asList("id", "mail");
            List<Object> valores = Arrays.asList(id, mail);
            List<String> orAttributes = Arrays.asList("id","mail");
            // CREO QUE FALLA ESTA LINEA LA DE DEBAJO
            Usuario existente = (Usuario) session.getCondicional(Usuario.class, filtros,null, orAttributes ,valores);

            if (existente != null) {
                throw new UsuarioYaRegistradoException("Usuario o email ya registrado: " + id + "/" + mail);
            }

            Usuario nuevo = new Usuario(id, name, apellidos, contra, mail, pregunta, respuesta);
            session.save(nuevo);

            session.commit();

        } catch (UsuarioYaRegistradoException e) {
            // en caso de duplicado, deshace (aunque no haya cambios)
            session.rollback();
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public UsuarioEnviar login(String mail_nombre, String pswd) throws CredencialesIncorrectasException{
        Session session = null;
        UsuarioEnviar ue = new UsuarioEnviar();
        Usuario u = new Usuario();

        try {
            session = FactorySession.openSession();
            List<String> filtro = Arrays.asList("mail", "id", "pswd"); // meto los atributos que quiero filtrar
            List<String> condicionales= Arrays.asList("mail", "id");//condicionales
            List<String> deseo = Arrays.asList(ObjectHelper.getFields(ue));
            List<Object> valores = Arrays.asList(mail_nombre, mail_nombre, pswd);
            u = (Usuario)session.getCondicional(u.getClass(),filtro, deseo, condicionales, valores);
            if (u == null) {
                throw new CredencialesIncorrectasException("Credenciales incorrectas");
            }
            ue = new UsuarioEnviar(u.getId(), u.getName(), u.getApellidos(), u.getPswd(), u.getMail(), u.getPregunta(), u.getRespuesta(), u.getTarrosMiel(), u.getFlor(), u.getMejorPuntuacion(), u.getNumPartidas(), u.getFloreGold());

            return ue;

        }
        catch (Exception e) {
            throw new CredencialesIncorrectasException("Credenciales incorrectas");
        }
        finally {
            session.close();
        }
    }
    @Override
    public DevolverCompra Comprar(usuario_objeto usuarioobjeto)
            throws UsuarioNoAutenticadoException, NoSuficientesTarrosException {

        Session session = null;
        DevolverCompra resultado = new DevolverCompra();

        try {
            // 1) Abrimos sesión y arrancamos la transacción
            session = FactorySession.openSession();
            session.beginTransaction();

            // 2) Recuperar usuario por ID
            List<String> filtrosUser   = Arrays.asList("id");
            List<Object> valoresUser   = Arrays.asList(usuarioobjeto.getUsuario_id());
            List<String > deseo   = Arrays.asList("tarrosMiel");
            Usuario u = (Usuario) session.get(Usuario.class, filtrosUser, valoresUser, deseo);
            if (u == null) {
                throw new UsuarioNoAutenticadoException("Usuario no autenticado");
            }

            // 3) Recuperar objeto (para sacarle el precio)
            List<String> filtrosObj   = Arrays.asList("nombre");
            List<Object> valoresObj   = Arrays.asList(usuarioobjeto.getObjeto_id());
            List<String> deseados   = Arrays.asList("precio", "id");
            Objeto obj = (Objeto) session.get(Objeto.class, filtrosObj, valoresObj, deseados);
            if (obj == null) {
                throw new RuntimeException("Objeto no encontrado: " + usuarioobjeto.getObjeto_id());
            }
            int precio = obj.getPrecio();  // suponemos getter getPrecio()

            // 4) Comprobar si tiene suficientes tarros de miel
            if (u.getTarrosMiel() < precio) {
                throw new NoSuficientesTarrosException("No tienes suficientes tarros de miel");
            }

            // 5) Restar tarros y actualizar en la tabla usuario
            int nuevosTarros = u.getTarrosMiel() - precio;
            u.setTarrosMiel(nuevosTarros);
            session.update(
                    Usuario.class,
                    Arrays.asList("tarrosMiel"),
                    Arrays.asList("id"),
                    Arrays.asList(nuevosTarros, u.getId())  // valores: primero nuevosTarros, luego id
            );

            // 6) Insertar registro en usuario_objeto
            // Construimos la entidad de relación (usuario_objeto)
            usuario_objeto rel = new usuario_objeto();
            rel.setUsuario_id(u.getId());
            rel.setObjeto_id(obj.getId());
            session.save(rel);  // método genérico de INSERT

            resultado.setTarrosMiel(nuevosTarros);
            return resultado;

        } catch (UsuarioNoAutenticadoException | NoSuficientesTarrosException ex) {
            // En caso de nuestros errores de negocio, hacemos rollback y re-lanzamos
            if (session != null) session.rollback();
            throw ex;
        } catch (Exception ex) {
            // Cualquier otro error interno: rollback y envolvemos
            if (session != null) session.rollback();
            throw new RuntimeException("Error interno al comprar: " + ex.getMessage(), ex);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Objeto> findArmas() {
        Session session = FactorySession.openSession();
        List<Objeto> armas = new ArrayList<>();
        try {
            armas = (List<Objeto>) session.getLista(Objeto.class,
                    Arrays.asList("tipo"),
                    Arrays.asList("arma"),
                    null);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las armas: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return armas;
    }

    @Override
    public List<Objeto> findSkins() {
        Session session = FactorySession.openSession();
        List<Objeto> skins = new ArrayList<>();
        try {
            skins = (List<Objeto>) session.getLista(Objeto.class,
                    Arrays.asList("tipo"),
                    Arrays.asList("skin"),
                    null);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las skins: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return skins;
    }

    public List<Objeto> armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Session session = FactorySession.openSession();
        List<Objeto> armas = new ArrayList<>();
        usuario_objeto u = new usuario_objeto();
        List<Objeto> arma = new ArrayList<>();
        u.setUsuario_id(usuario);
        try {
            List<String> filtros = Arrays.asList("usuario_id","id");
            Object valores = usuario;

            armas = (List<Objeto>) session.getWithJoin(usuario_objeto.class, Objeto.class,null, filtros,"nombre" ,valores);
            if (armas == null || armas.isEmpty()) {
                throw new NoHayObjetos("No hay objetos");
            }
            arma = armas.stream()
                    .filter(o -> "arma".equalsIgnoreCase(o.getTipo()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las armas: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return arma;
    }
    public List<Objeto>  skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos {
        Session session = FactorySession.openSession();
        List<Objeto> skins = new ArrayList<>();
        usuario_objeto u = new usuario_objeto();
        List<Objeto> skin = new ArrayList<>();
        u.setUsuario_id(usuario);
        try {
            List<String> filtros = Arrays.asList("usuario_id", "id");
            Object valores = usuario;

            skins = (List<Objeto>) session.getWithJoin(usuario_objeto.class, Objeto.class, null, filtros, "nombre", valores);
            if (skins == null || skins.isEmpty()) {
                throw new NoHayObjetos("No hay objetos");
            }
            skin = skins.stream()
                    .filter(o -> "skin".equalsIgnoreCase(o.getTipo()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las armas: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return skin;
    }

    @Override
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException {
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("id");
            List<Object> valores = Arrays.asList(id);
            Usuario u = (Usuario) session.get(Usuario.class, filtros, valores, null);
            if (u == null) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado: " + id);
            }
            session.delete(u); // primer delete lo borra de la tabla usuarioUsuario
            usuario_objeto a = new usuario_objeto(id, null);
            session.delete(a);// segundo delete lo borra de la tabla usuario_objeto
            session.commit();
        } catch (UsuarioNoEncontradoException e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Intercambio intercambio(String usuario) throws CredencialesIncorrectasException, NoHayFlores{
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("id");
            List<Object> valores = Arrays.asList(usuario);
            Usuario u = (Usuario) session.get(Usuario.class, filtros, valores, null ); //SELECT DEL USUARIO
            System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());

            if (u == null) {
                throw new CredencialesIncorrectasException("No esta registrado");
            }
         int Tarros = 0;
         int FloresSobrantes = u.getFlor();

         if((u.getFlor() < 30) && (u.getFloreGold() == 0)) {
            throw new NoHayFlores("No hay nada a convertir en Tarros, juega mas para conseguir mas!!");
         }
         while(FloresSobrantes >= 30){ // Va ahciendo restas de 30 n 30 i ba sumando Tarros pq cada 30 flores normales
            Tarros++;                // equivalen a 1 solo Tarro de Miel
            FloresSobrantes = FloresSobrantes - 30;
         }
            Tarros = Tarros + (u.getFloreGold()*50); // 1 Dorada = 50 Tarros

            List<String> cambios2 = Arrays.asList("tarrosMiel", "flor", "floreGold");
            List<String> filtros2 = Arrays.asList("id");
            List<Object> valores2 = Arrays.asList(u.getId(), u.getTarrosMiel() + Tarros, FloresSobrantes, 0);
            session.update(Usuario.class ,cambios2, filtros2, valores2); //aqui se hace un update del usuario, para que se le reste los tarros de miel y las flores sobrantes

         Intercambio i = new Intercambio(Tarros, FloresSobrantes);
         System.out.println(u.getTarrosMiel() + "+" + u.getFlor()+ "+" + u.getFloreGold());
          return i;

        } catch (CredencialesIncorrectasException | NoHayFlores e) {
            System.out.println("Error lógico: " + e.getMessage());
            throw e; // o manejarlo según tu lógica de aplicación
        }
         finally {
            session.close();
        }
    }

    @Override
    public List<Info> informcion (String UserId) throws RuntimeException {
        Session session = FactorySession.openSession();
        List<Usuario> usuarios = new ArrayList<>();
        List<Info> informcion = new ArrayList<>();
        List<String> deseados = Arrays.asList("id","mejorPuntuacion", "numPartidas");
        try {
            usuarios = (List<Usuario>) session.getLista(Usuario.class, null, null, null);
            if (usuarios == null || usuarios.isEmpty()) {
                throw new RuntimeException("No hay usuarios registrados");
            }
            List<Info> top5 = usuarios.stream()
                    .sorted((u1, u2) -> Integer.compare(u2.getMejorPuntuacion(), u1.getMejorPuntuacion()))
                    .limit(5)
                    .map(u -> new Info(u.getId(), u.getMejorPuntuacion(), u.getNumPartidas()))
                    .collect(Collectors.toList());
            top5.stream().limit(6);

             for (int i = 0;usuarios.size()<i;i++) {
                if(usuarios.get(i).getId().equals(UserId)) {
                    top5.add(new Info(usuarios.get(i).getId(), usuarios.get(i).getMejorPuntuacion(), usuarios.get(i).getNumPartidas()));
                }
             }
             return top5;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}

