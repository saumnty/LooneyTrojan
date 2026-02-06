<%@ page contentType="application/octet-stream" language="java" %>
<%@ page import="java.io.*" %>
<%
    String sistema = request.getParameter("sistema");
    String version = request.getParameter("version");

    String archivo = "looney-troyans";

    // Determinar el archivo correcto según el sistema operativo y la arquitectura
    if (sistema.equals("Windows")) {
        archivo += version.equals("64") ? "-win64.exe" : "-win32.exe";
    } else if (sistema.equals("Linux")) {
        archivo += "-linux64.tar.gz"; // solo 64 bits para Linux
    } else if (sistema.equals("MacOS")) {
        archivo += "-mac64.dmg"; // solo 64 bits para MacOS
    }

    // Ruta del archivo en el servidor
    String ruta = application.getRealPath("/descargas/" + archivo);
    File f = new File(ruta);

    if (!f.exists()) {
        out.println("Archivo no encontrado.");
        return;
    }

    // Enviar el archivo al cliente
    response.setHeader("Content-Disposition", "attachment;filename=\"" + archivo + "\"");
    response.setContentLength((int) f.length());

    // Leer el archivo y escribirlo en el output stream
    FileInputStream in = new FileInputStream(f);
    OutputStream outStream = response.getOutputStream();

    byte[] buffer = new byte[4096];
    int bytesRead;

    while ((bytesRead = in.read(buffer)) != -1) {
        outStream.write(buffer, 0, bytesRead);
    }

    in.close();
    outStream.flush();
%>