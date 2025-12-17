# Final Programacion II 2025 | Sistema de Gestión de Vehículos - CRUD JavaFX
### Sobre mi
Mi nombre es Julian Decastelli. Soy alumno de la tecnicatura universitaria en programación y espero poder recibirme en el primer cuatrimestre de 2026.


## Resumen
Esta es una aplicación que permite gestionar vehiculos a través de una interfaz gráfica. Sus principales funcionalidades son las de Crear, Leer, Modificar y Eliminar registros a la vez que exportar o importar datos soportando tanto archivos CSV, JSON, binarios y TXT.

<img width="1003" height="632" alt="image" src="https://github.com/user-attachments/assets/6d5b74ed-3acd-48c8-8fab-9b60dc26ecbf" />
Los vehiculos cargados se pueden visualizar en una grilla, la misma puede ser filtrada y ordenada según distintas condiciones.
<img width="660" height="410" alt="image" src="https://github.com/user-attachments/assets/a8f32fd6-ac64-4270-9f22-68ab92ad1282" />
la interfaz tambien nos permite aplicar un descuento general del 10% a todos los vehiculos, esto se logra utilizando una interfaz funcional (Consumer) aplicada sobre cada elemento del gestor de manera iterativa.

<img width="920" height="537" alt="image" src="https://github.com/user-attachments/assets/90e29c46-a360-4a7b-8299-60e4fcb8d53e" />
Al presionar el botón de Agregar se despliega un formulario que nos permite añadir un nuevo vehiculo a la lista.

<img width="896" height="514" alt="image" src="https://github.com/user-attachments/assets/947ca5e1-de48-4bf3-b986-18656172c79a" />
Al seleccionar un vehiculo de la tabal y presionar el botón de Actualizar esta vez el formulario nos permite modificar los datos de un vehiculo

<img width="754" height="544" alt="image" src="https://github.com/user-attachments/assets/8952bc00-3ffc-4bb8-9395-4c8f7be547be" />
Los botones de importar y exportar nos permiten cargar o guardar archivos respectivamente, siendo posible seleccionar el formato CSV y JSON.

<img width="838" height="385" alt="image" src="https://github.com/user-attachments/assets/56f724ad-79b3-416c-a49c-094829554bee" />
Mientras que el boton de guardar y cargar nos permite serializar y deserealizar los datos a un archivo binario (.dat) que se carga de manera automatica cada vez que se abre el proyecto.

<img width="502" height="237" alt="image" src="https://github.com/user-attachments/assets/418e7b51-f71c-4352-b2eb-96b55aced42a" />
Al intentar cerrar la aplicación un dialog nos pregunta si queremos guardar antes de salir.

## Diagrama UML de entidades
<img width="1100" height="630" alt="Entidades" src="https://github.com/user-attachments/assets/05eaaa44-a890-4ce4-be80-975c547b0f0e" />

## Archivos de Persistencia
- [vehiculos.json](https://github.com/user-attachments/files/24220198/vehiculos.json)
- [vehiculos.txt](https://github.com/user-attachments/files/24220199/vehiculos.txt)
- [vehiculos.csv](https://github.com/user-attachments/files/24220200/vehiculos.csv)
<img width="1003" height="308" alt="image" src="https://github.com/user-attachments/assets/5815cf9d-c2e0-4d98-92f7-59e5771a6444" />
(vehiculos.dat)
