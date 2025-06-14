package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.util.HashUtil;
import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.db.orm.util.QueryHelper;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;


import java.util.*;
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
            List<String> filtro = Arrays.asList("mail", "id"); // meto los atributos que quiero filtrar
            List<String> condicionales= Arrays.asList("mail", "id");//condicionales
            List<String> deseo = Arrays.asList(ObjectHelper.getFields(ue));
            List<Object> valores = Arrays.asList(mail_nombre, mail_nombre);
            u = (Usuario)session.getCondicional(u.getClass(),filtro, deseo, condicionales, valores);

            if (u == null || !HashUtil.matches(pswd, u.getPswd())) {
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
    public DevolverCompra Comprar(Usuario_objeto usuarioobjeto)
            throws UsuarioNoAutenticadoException, NoSuficientesTarrosException {

        Session session = null;
        DevolverCompra resultado = new DevolverCompra();

        try {
            session = FactorySession.openSession();
            session.beginTransaction();

            List<String> filtrosUser   = Arrays.asList("id");
            List<Object> valoresUser   = Arrays.asList(usuarioobjeto.getUsuario_id());
            List<String > deseo   = Arrays.asList("tarrosMiel");
            Usuario u = (Usuario) session.get(Usuario.class, filtrosUser, valoresUser, deseo);
            if (u == null) {
                throw new UsuarioNoAutenticadoException("Usuario no autenticado");
            }

            List<String> filtrosObj   = Arrays.asList("nombre");
            List<Object> valoresObj   = Arrays.asList(usuarioobjeto.getObjeto_nombre());
            List<String> deseados   = Arrays.asList("precio", "nombre");
            Objeto obj = (Objeto) session.get(Objeto.class, filtrosObj, valoresObj, deseados);
            if (obj == null) {
                throw new RuntimeException("Objeto no encontrado: " + usuarioobjeto.getObjeto_nombre());
            }
            int precio = obj.getPrecio();  // suponemos getter getPrecio()

            if (u.getTarrosMiel() < precio) {
                throw new NoSuficientesTarrosException("No tienes suficientes tarros de miel");
            }

            int nuevosTarros = u.getTarrosMiel() - precio;
            u.setTarrosMiel(nuevosTarros);
            session.update(
                    Usuario.class,
                    Arrays.asList("tarrosMiel"),
                    Arrays.asList("id"),
                    Arrays.asList(nuevosTarros, usuarioobjeto.getUsuario_id())  // valores: primero nuevosTarros, luego id
            );
            System.out.println("Tarros de miel actualizados: " + nuevosTarros);

            Usuario_objeto rel = new Usuario_objeto();
            rel.setUsuario_id(usuarioobjeto.getUsuario_id());
            rel.setObjeto_nombre(obj.getNombre());
            session.save(rel);  // método genérico de INSERT
            session.commit();
            resultado.setTarrosMiel(nuevosTarros);
            return resultado;

        } catch (UsuarioNoAutenticadoException | NoSuficientesTarrosException ex) {
            if (session != null) session.rollback();
            throw ex;
        } catch (Exception ex) {
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
        Usuario_objeto u = new Usuario_objeto();
        List<Objeto> arma = new ArrayList<>();
        u.setUsuario_id(usuario);
        try {
            List<String> filtros = Arrays.asList("usuario_id","id");
            Object valores = usuario;

            armas = (List<Objeto>) session.getWithJoin(Usuario_objeto.class, Objeto.class,null, filtros,"nombre" ,valores);
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
        Usuario_objeto u = new Usuario_objeto();
        List<Objeto> skin = new ArrayList<>();
        u.setUsuario_id(usuario);
        try {
            List<String> filtros = Arrays.asList("usuario_id", "id");
            Object valores = usuario;

            skins = (List<Objeto>) session.getWithJoin(Usuario_objeto.class, Objeto.class, null, filtros, "nombre", valores);
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
            Usuario_objeto a = new Usuario_objeto(id, null);
            session.delete(a);// segundo delete lo borra de la tabla Usuario_objeto
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
         while(FloresSobrantes >= 30){
            Tarros++;
            FloresSobrantes = FloresSobrantes - 30;
         }
            Tarros = Tarros + (u.getFloreGold()*50); // 1 Dorada = 50 Tarros

            List<String> cambios2 = Arrays.asList("tarrosMiel", "flor", "floreGold");
            List<String> filtros2 = Arrays.asList("id");
            List<Object> valores2 = Arrays.asList( u.getTarrosMiel() + Tarros, FloresSobrantes, 0, u.getId());
            session.update(Usuario.class ,cambios2, filtros2, valores2); //aqui se hace un update del usuario, para que se le reste los tarros de miel y las flores sobrantes
	    session.commit();
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

    @Override
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException{
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("id");
            List<Object> valores = Arrays.asList(usuario);
            Usuario u = (Usuario) session.get(Usuario.class,null, null, filtros);
            if(u.getId() == null) {
                throw new CredencialesIncorrectasException("Usuario no puede ser nulo o vacío");
            }

            u.setPswd(contra);
            session.update(Usuario.class, Arrays.asList("pswd"), Arrays.asList("id"), Arrays.asList(contra, usuario));
            session.commit();
        } catch (CredencialesIncorrectasException e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException{
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("id");
            List<Object> valores = Arrays.asList(usuario);
            Usuario u = (Usuario) session.get(Usuario.class, filtros, valores, null);
            if (u == null) {
                throw new CredencialesIncorrectasException("Usuario no encontrado: " + usuario);
            }
            return u.getPregunta();
        } catch (CredencialesIncorrectasException e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException {
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("id", "respuesta");
            List<Object> valores = Arrays.asList(id, respuesta);
            Usuario u = (Usuario) session.getCondicional(Usuario.class, filtros, null, null, valores);
            String HashedAns = HashUtil.hash(respuesta);
            if (u == null || !HashUtil.matches(respuesta, u.getRespuesta())) {
                throw new CredencialesIncorrectasException("Respuesta incorrecta o usuario no encontrado");
            }
            return u;
        } catch (CredencialesIncorrectasException e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Badge> getUserBadges(String userId) throws Exception {
        Session session = FactorySession.openSession();
        try {
            List<String> filtros = Arrays.asList("user_id");
            List<Object> valores = Arrays.asList(userId);
            return (List<Badge>) session.getLista(Badge.class, filtros, valores, null);
        } finally {
            session.close();
        }
    }

    @Override
    public ListFreqQuest getPreguntasFrecuentes() {
        Session session = FactorySession.openSession();
        ListFreqQuest listaFreqQuest = new ListFreqQuest();

        try {
            List<String> filtros = new ArrayList<>();
            List<Object> valores = new ArrayList<>();
            List<String> deseados = new ArrayList<>();

            String sql = QueryHelper.createQuerySELECT(filtros, FreqQuest.class, deseados);
            System.out.println("Consulta generada: " + sql);

            for (int i = 0; i < valores.size(); i++) {
                System.out.println("Parámetro " + (i + 1) + ": " + valores.get(i));
            }

            List<Object> resultados = (List<Object>) session.getLista(FreqQuest.class, filtros, valores, deseados);

            List<FreqQuest> faqs = new ArrayList<>();
            if (resultados != null) {
                for (Object obj : resultados) {
                    if (obj instanceof FreqQuest) {
                        faqs.add((FreqQuest) obj);
                    }
                }
            }

            listaFreqQuest.setFaqs(faqs);
        } catch (Exception e) {
            System.err.println("Error al obtener FAQs: " + e.getMessage());
            e.printStackTrace();
            listaFreqQuest.setFaqs(new ArrayList<>());
        } finally {
            session.close();
        }

        return listaFreqQuest;
    }

    @Override
    public void addQuestion(Question question) throws Exception {
        Session session = FactorySession.openSession();
        try {
            session.save(question);
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void addIssue(Issue issue) throws Exception {
        Session session = FactorySession.openSession();
        try {
            // Generar ID único si no existe
            if (issue.getId() == null || issue.getId().isEmpty()) {
                issue.setId(UUID.randomUUID().toString());
            }
            issue.setDate(new Date().toString()); // Actualizar fecha
            session.save(issue);
            session.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Issue> getAllIssues() throws Exception {
        Session session = FactorySession.openSession();
        try {
            List<Issue> issues = (List<Issue>) session.getLista(Issue.class, null, null, null);

            // Manejar caso de lista nula
            if(issues == null) {
                return new ArrayList<>(); // Devuelve lista vacía en lugar de null
            }
            return issues;
        } finally {
            session.close();
        }
    }

}

