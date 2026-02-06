<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Looney Troyans</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <!-- Botón de inicio que redirige a la página principal -->
    <a href="index.jsp">
        <button class="btn-inicio">Inicio</button>
    </a>

    <div class="form-container">
        <h2>Iniciar Sesión</h2>
        <form action="login_usuario.jsp" method="POST">
            <label for="email">Correo Electrónico</label>
            <input type="email" id="email" name="email" required>

            <label for="contraseña">Contraseña</label>
            <input type="password" id="contraseña" name="contraseña" required>

            <button type="submit" class="btn">Iniciar sesión</button>
        </form>
        <p>¿No tienes cuenta? <a href="registro.jsp">Regístrate</a></p>
    </div>
</body>
</html>