<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Soporte - Antivirus Web</title>
    <link rel="stylesheet" href="styles.css"> <!-- opcional -->
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ffffff;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #ffffff;
            color: white;
            padding: 10px;
            text-align: center;
        }
        .container {
            max-width: 900px;
            margin: 20px auto;
            padding: 20px;
            background: white;
            border-radius: 8px;
        }
        h2 {
            color: #38b61f;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label, textarea, input {
            margin: 10px 0;
        }
        textarea {
            height: 150px;
        }
        button {
            background-color: #ffffff;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        button:hover {
            background-color: #005fa3;
        }

        /* Estilos FAQ */
        .faq-section {
            margin-top: 40px;
        }
        .faq-item {
            border-bottom: 1px solid #ccc;
        }
        .faq-question {
            cursor: pointer;
            padding: 10px;
            background-color: #e6f0fa;
            border: none;
            width: 100%;
            text-align: left;
            font-weight: bold;
        }
        .faq-answer {
            display: none;
            padding: 10px;
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>

<header>
    <h1>Centro de Soporte - Antivirus Web</h1>
</header>

    <div class="faq-section">
        <h2>Preguntas Frecuentes</h2>

        <div class="faq-item">
            <button class="faq-question">¿Por qué no puedo iniciar sesión?</button>
            <div class="faq-answer">Verifica tus credenciales y que tu cuenta esté activada. Intenta restablecer tu contraseña si es necesario.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Cómo puedo registrar una cuenta nueva?</button>
            <div class="faq-answer">Ve a la página de registro, completa tus datos y confirma tu cuenta desde el correo electrónico.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Olvidé mi contraseña, qué hago?</button>
            <div class="faq-answer">En la página de inicio de sesión, haz clic en “¿Olvidaste tu contraseña?” y sigue las instrucciones.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Por qué el escaneo del antivirus no funciona?</button>
            <div class="faq-answer">Verifica tu conexión a internet y que hayas iniciado sesión correctamente. Si el problema persiste, contacta soporte.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Cómo puedo eliminar mi cuenta?</button>
            <div class="faq-answer">Puedes solicitar la eliminación de tu cuenta mediante el formulario de soporte.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Qué datos recopila la aplicación?</button>
            <div class="faq-answer">Solo los necesarios para el servicio, como nombre, correo y actividad. Consulta nuestra política de privacidad.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Qué navegadores son compatibles?</button>
            <div class="faq-answer">Se recomienda usar Chrome, Firefox o Edge en sus versiones más recientes.</div>
        </div>
        <div class="faq-item">
            <button class="faq-question">¿Puedo usar la app en mi celular?</button>
            <div class="faq-answer">Sí, aunque recomendamos la versión de escritorio para una mejor experiencia.</div>
        </div>
    </div>
</div>

<script>
    // Script para hacer funcionar el FAQ tipo acordeón
    const questions = document.querySelectorAll(".faq-question");

    questions.forEach(button => {
        button.addEventListener("click", () => {
            const answer = button.nextElementSibling;
            const isVisible = answer.style.display === "block";
            document.querySelectorAll(".faq-answer").forEach(a => a.style.display = "none");
            answer.style.display = isVisible ? "none" : "block";
        });
    });
</script>

</body>
</html>
