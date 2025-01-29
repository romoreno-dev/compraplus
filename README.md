## CompraPlus, planificador de la compra semanal

### 1. Descripción

**CompraPlus** es una aplicación para móviles Android que tiene como objetivo ayudar a planificar de forma inteligente la compra semanal. Permite generar listas de compra, conocer la localización de los principales supermercados españoles y consultar los precios de sus productos. Pone remedio a olvidos, desinformación y visitas al súper mal organizadas que, sumadas al actual contexto económico inflacionario, constituyen un verdadero quebradero de cabeza para muchas familias.

### 2. Características técnicas

El proyecto ha sido realizado en Android Studio, utilizando Kotlin (versión 1.9.24), Android SDK 34 y minSDK 24.

El proyecto implementa una base de datos interna mediante SQLite (gestionada por el ORM Room), utiliza Firebase Auth como mecanismo de autenticación y Retrofit para realizar llamadas a las APIs de los supermercados Día, Eroski y Mercadona y Google.


### 2.1. Uso de la funcionalidad de localización de supermercados

Para poder utilizar la funcionalidad de localización de supermercados será necesario disponer de una API Key de Google Maps que debe situarse en el fichero `secrets.properties`

```
MAPS_API_KEY=TU_API_KEY
```

### 3. Colaboraciones

El proyecto originalmente ha sido previsto con tres finalidades:
- Satisfacer una necesidad personal y familiar
- Realizar un proyecto final para el Grado Superior de Desarrollo de Aplicaciones Multiplataforma
- Poner en práctica los conocimientos aprendidos sobre Android y Kotlin

Por el momento, no se contempla publicar este proyecto en Google PlayStore, ni mantener el desarrollo de nuevas funcionalidades.

Sin embargo, sí que es una aplicación que va a ser utilizada por familiares y amigos para sus compras cotidianas. 

Si deseas realizar alguna sugerencia o cambio, puedes abrir un PR realizado mediante fork del repositorio a la rama `develop`. ¡Gracias!