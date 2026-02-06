<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Looney Troyans</title>
    <link rel="stylesheet" href="css/styles.css">
    <script>
        // Validar contraseñas
        function validarContraseñas() {
            var pass1 = document.getElementById("contraseña").value;
            var pass2 = document.getElementById("confirmar").value;

            if (pass1 !== pass2) {
                alert("Las contraseñas no coinciden. Por favor, revisa.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <!-- Botón de inicio que redirige a la página principal, alineado a la izquierda -->
    <a href="index.jsp">
        <button class="btn-inicio">Inicio</button>
    </a>

    <div class="form-container">
        <h2>Registro de Usuario</h2>
        <form action="registro_usuario.jsp" method="POST" onsubmit="return validarContraseñas()">
            <label for="nombre">Nombre Completo</label>
            <input type="text" id="nombre" name="nombre" required>

            <label for="email">Correo Electrónico</label>
            <input type="email" id="email" name="email" required>

            <label for="contraseña">Contraseña</label>
            <input type="password" id="contraseña" name="contraseña" required>

            <label for="confirmar">Confirmar Contraseña</label>
            <input type="password" id="confirmar" name="confirmar" required>

            <button type="submit" class="btn">Registrar</button>
        </form>
        <p>¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión</a></p>
    </div>
</body>
</html>