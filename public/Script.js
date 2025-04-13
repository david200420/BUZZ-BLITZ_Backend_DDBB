$(document).ready(function() { // espera a que todo este cargado ( el html)
    let usuarioActual = null;

    const mainHeader = $("#main-header");
    const userInfo = $("#user-info");

    const navAbout = $("#nav-about");
    const navLogin = $("#nav-login");
    const navSignup = $("#nav-signup");
    const navShop = $("#nav-shop");

    const sectionAbout = $("#about");
    const sectionLogin = $("#login");
    const sectionSignup = $("#signup");
    const sectionShop = $("#shop");

    const landing = $("#landing");
    const mainContent = $("#mainContent");

    <!--const ObjetoContainer = $("#armas-container");-->

    function setUpWeb(){
        landing.show();      // Se muestra la landing
        mainContent.hide(); // Se oculta el contenido principal
    }

    function updateNav() {
        if (usuarioActual) {
            $("#nav-signup").hide();
            $("#nav-shop").show(); //QUIERO HACER QUE CERRAR SESSION SE VAYA ARRIBA A LA DERECHA
            $("#nav-login").text("Cerrar sesión");
        } else {
            $("#nav-signup").show();
            $("#nav-shop").hide();
            $("#nav-login").text("Log in");
        }
    }

    function showSection(sectionId) { //Como hacemos una SPA cada vez que cliquemos a uno del nav
        $("#about, #login, #signup, #shop, #olvidadoContra,#terminos-condiciones").hide();//Vamos a tener que hacer hide de los <div>
        $("nav a").removeClass("active");
        $(sectionId).show();
    }

    function getTienda() {
        $.ajax({
            url: "http://localhost:8080/dsaApp/usuarios/tienda/armas",
            type: "GET",
            success: function (tienda) {
                console.log("Respuesta de la tienda:", tienda);
                mostrarItems(tienda.armas, "#objetos-container");
            },
            error: function (xhr) {
                alert("Error cargando tienda: " + xhr.responseText);
            }
        });
        $.ajax({
            url: "http://localhost:8080/dsaApp/usuarios/tienda/skins",
            type: "GET",
            success: function (tienda) {
                console.log("Respuesta de la tienda:", tienda);
                mostrarItems(tienda.skins, "#objetos-container");
            },
            error: function (xhr) {
                alert("Error cargando tienda: " + xhr.responseText);
            }
        });
    }
    function mostrarItems(items, contenedor) {
        const container = $(contenedor);
        container.empty();

        Object.values(items).forEach(item => {
            const htmlItem = `
                    <div class="item-tienda">
                        <h4>${item.nombre}</h4>
                        <div class="item-precio">Precio: ${item.precio} 🍯</div>
                        <button class="btn-comprar" data-id="${item.id}">COMPRAR</button>
                    </div>
                `;
            container.append(htmlItem);
        });

        $(contenedor + " .btn-comprar").click(function () {
            if (!usuarioActual) {
                alert("Debes iniciar sesión para comprar");
                return;
            }

            const objetoId = $(this).data('id');
            realizarCompra(objetoId);
        });
    }

    function realizarCompra(objetoId) {
        const compraData = {
            usuarioId: usuarioActual.id,
            objeto: {id: objetoId}
        };

        $.ajax({
            url: "http://localhost:8080/dsaApp/usuarios/comprar",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(compraData),
            success: function (usuarioActualizado) {
                usuarioActual = usuarioActualizado;
                alert("Compra realizada con éxito!");
                getTienda();
            }

        });
    }
    function mostrarWeb (){
        mainContent.fadeIn(500);
        mainHeader.removeClass("header-portada").addClass("header-principal");
        updateNav();
    }

    $("#landing .overlay").one("click",(function() { // con un clik en el overlay se carga la web
        landing.slideUp(1000, mostrarWeb); //el landing tiene una funcion slideUP que lo que hace es que
    }));//aplica la funcion con un tiempo, que desaparece el contenedor y aplica la funcion.

    $("#nav-about").click(function (e) {
        e.preventDefault();
        showSection("#about");
        $(this).addClass("active");
    });

    $("#nav-login").click(function (e) {
        e.preventDefault();
        if (usuarioActual) {
            // Confirmación de cierre de sesión
            if (confirm("¿Estás seguro que quieres cerrar sesión?")) {
                usuarioActual = null;
                updateNav();
                showSection("#about");
                $("#nav-about").addClass("active");
                $("#user-info").fadeOut();
                alert("Sesión cerrada correctamente");
            }
        } else {
            showSection("#login");
            $(this).addClass("active");
        }
    });

    $("#nav-signup").click(function (e) {
        e.preventDefault();
        showSection("#signup");
        $(this).addClass("active");
    });

    $("#nav-shop").click(function (e) {
        e.preventDefault();
        showSection("#shop");
        $(this).addClass("active");
        getTienda();
    });

    $("#link-to-signup").click(function (e) {
        e.preventDefault();
        $("#nav-signup").click();
    });

    $("#link-to-login").click(function (e) {
        e.preventDefault();
        $("#nav-login").click();
    });

    $("#Terminos-enlace").click(function(e){
        e.preventDefault();
        $("#terminos-condiciones").show();
    });

    $("#aceptarTerminos").click(function(e){
        e.preventDefault();
        var checkbox = $('#checkboxx');
        checkbox.prop('checked', !checkbox.prop('checked'));
        $("#terminos-condiciones").hide();
    });

    $("#rechazarTerminos").click(function(e){
        e.preventDefault();
        $("#terminos-condiciones").hide();
    });

    $("#form-login").submit(function (e) { //Si le da al boton submit del <div>
        e.preventDefault();

        const loginData = {
            idoname: $("#login-user").val(),
            pswd: $("#login-password").val()
        };

        $.ajax({
            url: "http://localhost:8080/dsaApp/usuarios/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function (response) {
                usuarioActual = response;
                alert("Login exitoso! Bienvenido, " + response.name);
                updateNav();
                showSection("#about");
                $("#nav-about").addClass("active");
                $("#user-info").text("ID: " + response.id).fadeIn();
            },
            error: function (xhr) {
                alert("Error en el login: " + xhr.responseText);
            }
        });
    });

    $("#form-signup").submit(function (e) {
        e.preventDefault();//Para que no se actualice la web
        const signupData = { //definimos variables necesarias para la funcion ya que no son globales
            id: $("#signup-user").val(), // y las metemos en un objeto que lo transformaremos a JSON
            name: $("#signup-fullname").val(),
            pswd: $("#signup-password").val(),
            mail: $("#signup-email").val(),
            pregunta:$("#security-question").val(),
            respuesta: $("#answer-securityquestion").val()
        };

        const p = $("#resignup-password").val();//miramos si las contraseñas coinciden
        if(signupData.pswd !== p){
            alert("Las contraseñas no coinciden");
            return;
        }
        const checkbox = $("#checkboxx");//miramos is ha aceptado los terminos o no
        if (!checkbox.prop("checked")) {
            $("#terminos-label").addClass("shake-border"); // Añadimos la classe para que el usuario vea
                                                                //en lo que se esta equivocando
            setTimeout(() => {
                $("#terminos-label").removeClass("shake-border");
            }, 1000);

            alert("Debes aceptar los términos y condiciones para registrarte");
            return;
        }

        $.ajax({
            url: "http://localhost:8080/dsaApp/usuarios/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(signupData),
            success: function (response) {
                alert("Registro exitoso! Ahora puedes iniciar sesión.");
                $("#nav-login").click(); // hacemos que se clique el boton del navegador del login

                //Rellenamos los campos del login con los daos que acaba de usar para el signup
                $("#login-user").val(signupData.id);
                $("#login-password").val(signupData.pswd);
                $("#login-boton-submit").focus(); //esto esta muy bien ya que deja el raton en el boton
            },
            error: function (xhr) {
                alert("Error en el registro: " + xhr.responseText);
            }
        });
    });
    $("#Olv-Contra").click(function(e){
        e.preventDefault();
        $("#olvidadoContra").show();
    })
    $("#enviar-usuario").click(function (e){

    })
});
