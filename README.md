# Kaiju
Proyecto Kaiju de Jean Lefiman y Corina Urrutia para la asignatura de Desarrollo de Aplicaciones Móviles DSY1105

# Integrantes
- Jean Lefiman -> je.lefiman@duocuc.cl
- Corina Urrutia -> cor.urrutia@duocuc.cl

# Descripción
Kaiju desarrolla actividades vinculadas al comercio, los servicios y la gestión de productos. En su operación administra
productos, materiales e insumos que deben ser recibidos, almacenados, utilizados o vendidos. Para apoyar su
funcionamiento requiere disponer de información clara y actualizada sobre las cantidades disponibles, los movimientos
de inventario y las necesidades de reposición.

# Funcionalidades
El proyecto cuenta con las siguientes funcionalidades: 
- Inicio de Sesión: Solo los usuarios incluidos en los datos de prueba pueden tener acceso a las paginas de la aplicación.
- Catalogo: Muestra una lista de todos los productos con su nombre, descripción, stock, precio y una foto referencial por categoría.
- Detalle de Producto: Muestra toda la información relacionada a un producto especifico, incluyendo los movimientos realizados.
- Agregar Producto: Pagina con un formulario para poder agregar un producto al catalogo. Cuenta con validaciones
- Agregar Movimiento: Formulario con la información requerida para registrar un nuevo movimiento de un producto existente en la aplicación. Cuenta con validaciones.
- Alertas: Pantalla con todas las alertas de stock bajo, que esta asociado a el recurso de notificación para que le deje saber al usuario que cierto producto esta bajo el stock mínimo ingresado.
- Vibración + Animación: En ambas pantallas de agregar, al momento de rellenar el formulario correctamente o incorrectamente, la aplicación provocara una vibración y en caso de que el registro sea exitoso, se mostrara una animación intuitiva para dejarle saber al usuario que su ejemplar fue agregado.

# Como ejecutar e proyecto
1. **Clonar el repositorio:**
   Abre tu terminal y ejecuta el siguiente comando para clonar el proyecto:
   ```bash
   git clone https://github.com/CodedbyMeTheGreatest/Kaiju.git
   ```

2. **Abrir el proyecto:**
   * Abre **Android Studio**.
   * Selecciona **File > Open** (o *Open an Existing Project*).
   * Busca la carpeta donde clonaste el repositorio y selecciónala.

3. **Sincronizar el proyecto con Gradle:**
   * Espera a que Android Studio termine de indexar el proyecto.
   * Si es necesario, haz clic en el elefante de Gradle (**Sync Project with Gradle Files**) en la barra superior para descargar todas las dependencias (Compose, Room, KSP, etc.).

4. **Compilar y Ejecutar:**
   * Selecciona tu dispositivo físico o emulador en la lista desplegable de la barra superior.
   * Haz clic en el botón verde de **Run** (icono de ▶️) o presiona `Shift + F10` (Windows/Linux) / `Control + R` (macOS).

# Links 

Drive: https://drive.google.com/drive/u/2/folders/15_3Bf4qJ-SKR9NN41rxbhOCLvFJ9e2WN
- Incluye algunos bocetos, la plantilla del caso, algunos documentos con los requisitos, y un ERS que nunca pudo ser empezado debido a percances con la reunion con el cliente. 

Trello: Tablero de organización de tareas del equipo.
- https://trello.com/b/xTEVOLtZ/kaijugrupox


# Api Seleccionada
- PokeAPI
- Descripcion: Mediante este sitio web, podrás consultar información sobre Pokémon, sus movimientos, habilidades, tipos, grupos de huevos y mucho más.
- URL: https://pokeapi.co/
- Endpoint (ejemplo): https://pokeapi.co/api/v2/pokemon/ditto

## Como se accede

Al ingresar los datos de un producto a registrar, si se encuentra el nombre de un Pokemon en los apartados 'Nombre', 'Descripción' o 'Detalle', al registrarse el producto se mostrará un mensaje de exito con la imagen de un Pokemon

<img width="324" height="710" alt="imagen" src="https://github.com/user-attachments/assets/61c755ba-3ea3-4fe6-986b-3bd138c4171e" />

Al aparecer esa pantalla, uno puede dirigirse al detalle del producto, y solo los productos con nombres de Pokemon, tendran una ficha especial abajo de la informacion del producto, con informacion perteneciente al Pokemon nombrado. 

<img width="316" height="706" alt="imagen" src="https://github.com/user-attachments/assets/9edbb54f-23a5-409b-8e86-263e09fb42df" />
