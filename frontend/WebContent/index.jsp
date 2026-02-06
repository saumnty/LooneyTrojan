<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    // Detectar el sistema operativo del usuario
    String userAgent = request.getHeader("User-Agent").toLowerCase();
    String sistema = "Desconocido";
    String arquitectura = "64";  // Predeterminado a 64 bits

    if (userAgent.contains("windows")) {
        sistema = "Windows";
    } else if (userAgent.contains("mac")) {
        sistema = "MacOS";
    } else if (userAgent.contains("linux")) {
        sistema = "Linux";
    }

    // Determinar el archivo a descargar según el sistema operativo y arquitectura
    String archivo = "looney-troyans";

    if (sistema.equals("Windows")) {
        archivo += arquitectura.equals("64") ? "-win64.exe" : "-win32.exe";
    } else if (sistema.equals("Linux")) {
        archivo += "-linux64.tar.gz"; // Linux solo 64 bits
    } else if (sistema.equals("MacOS")) {
        archivo += "-mac64.dmg"; // Mac solo 64 bits
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Descargar Looney Troyans</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <!-- Botón de inicio que redirige a la página principal -->
    <a href="index.jsp">
        <button class="btn-inicio">Inicio</button>
    </a>

    <div class="container">
        <h2>🛡️ Looney Troyans - Antivirus Escolar</h2>
        <p>Detectamos tu sistema operativo: <strong><%= sistema %> 64 bits</strong></p>

        <form action="descarga.jsp" method="get">
            <label>Selecciona arquitectura:</label><br>
            <select name="version">
                <option value="64" selected>64 bits</option>
                <option value="32">32 bits</option>
            </select><br><br>

            <!-- Pasar el sistema operativo como un parámetro -->
            <input type="hidden" name="sistema" value="<%= sistema %>">
            <button class="btn" type="submit">⬇️ Descargar Looney Troyans</button>
        </form>

        <br><br>
        <p>¿No tienes cuenta?</p>
        <a href="registro.jsp" class="btn">Regístrate</a>
        <br><br>
        <p>¿Ya tienes cuenta?</p>
        <a href="login.jsp" class="btn">Inicia sesión</a>
        <br><br>
        <p>¿Quieres más funciones?</p>
        <a href="planes.jsp" class="btn">Ver planes premium</a>
    </div>
</body>
</html>