# Lab 4 WebSocket -- Project Report

## Description of Changes

Se ha completado el test `onChat` en `ElizaServerTest.kt` para verificar la funcionalidad del servidor WebSocket de Eliza. Para ello, se ha implementado la lógica para enviar un mensaje de prueba ("I am feeling sad") después de recibir la bienvenida completando el endpoint de cliente `ComplexClient`, lo que permite evaluar las respuestas del servidor. Estas respuestas se evaluan asegurando que el número de mensajes recibidos esté dentro de un rango esperado (entre 2 y 4), y luego evaluando que la respuesta contenga ciertas palabras clave relacionadas con el mensaje enviado.

## Technical Decisions

Se ha utilizado assertTrue con un rango en lugar de usar una comparación exacta con assertEquals, debido a la naturaleza asincrónica de las comunicaciones WebSocket, ya que sino el test, dependiendo de qué mensajes hayan llegado, si fuera un número fijo, podría no funcionar. Por si esto fallara, se planteó el poner o un rango más amplio, o un timeout, pero debido a que las pruebas han funcionado correctamente practicamente el 100% de las veces, se ha optado por mantener este formato final.

## Learning Outcomes

A través de la implementación de este test, se ha profundizado en el manejo de WebSockets en Kotlin, así como en la creación de pruebas unitarias efectivas para aplicaciones asincrónicas. Además, se ha mejorado la comprensión sobre cómo interactuar con servidores WebSocket y cómo validar las respuestas recibidas en un entorno de prueba.

## AI Disclosure

### AI Tools Used

- DeepSeek (Modo DeepThink) - Para dudas sobre el código y explicaciones técnicas.
- GitHub Copilot - Para autocompletado.

### AI-Assisted Work

- Completar automaticamente los comentarios que explican las decisiónes tomadas en el código.
- Ayudar a comprender el código existente.

### Original Work

- Toda la implementación del test `onChat` en `ElizaServerTest.kt`.
