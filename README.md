# LoopieApp - Aplicación Móvil de Moda Sustentable

**Loopie** es una aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose** para Android, diseñada como un **marketplace de compraventa de vestuario sustentable**, prendas de segunda mano y productos de emprendedores.  
Su propósito es fomentar la **economía circular** y el **consumo responsable**, conectando a vendedores y compradores de moda ecológica y reutilizable.

---

## Descripción General

Loopie permite explorar, comprar y vender artículos de vestuario únicos, hechos a mano, reutilizados o de segunda mano.  
La app ofrece una interfaz moderna, fluida y accesible, con soporte offline, persistencia local y recursos nativos integrados.

### Áreas principales

** Tienda pública (cliente):**
- Navegación por categorías y búsqueda con filtros.
- Visualización de productos (nombre, estado, precio, vendedor, fotos).
- Carrito de compras y flujo de checkout simulado.
- Confirmación de compra (éxito o fallo).
- Historial de pedidos y contacto con el vendedor.

** Panel vendedor / administración:**
- CRUD de productos (crear, editar, eliminar).
- Gestión de inventario y estados (publicado, borrador, agotado).
- Visualización de pedidos y estadísticas básicas.
- Subida de imágenes desde cámara o galería.

---

## Tecnologías Utilizadas

| Capa | Tecnologías | Descripción |
|------|--------------|-------------|
| **Frontend (Mobile)** | Kotlin, Jetpack Compose, AndroidX | UI declarativa y reactiva, arquitectura MVVM |
| **Gestión de estado** | ViewModel, StateFlow / LiveData | Control de estados y validaciones desacopladas |
| **Persistencia local** | Room (SQLite), DataStore | Guardado de productos, usuarios y configuraciones |
| **Recursos nativos** | CameraX, almacenamiento local | Subida y manejo de imágenes desde el dispositivo |
| **Imágenes** | Coil | Carga eficiente de imágenes remotas o locales |
| **Testing** | JUnit, MockK, Compose UI Tests, Espresso | Pruebas unitarias y de interfaz |
| **Colaboración** | GitHub, Trello | Control de versiones y planificación ágil |
| **Build** | Gradle | Automatización de compilaciones |

---

## Arquitectura del Proyecto

**Patrón:** MVVM (Model–View–ViewModel)


**Flujo general:**
1. El usuario interactúa con la interfaz construida en Compose.  
2. Los ViewModels gestionan la lógica y el estado.  
3. El Repositorio coordina el acceso a datos (Room o fuente local JSON).  
4. Las entidades y modelos garantizan independencia entre capas.

---

## Funcionalidades Implementadas (MVP)

-  Interfaz principal con navegación fluida entre vistas.  
-  Formularios con validación visual por campo.  
-  Lógica de validación desacoplada del UI.  
-  Animaciones funcionales en pantallas e interacciones.  
-  Persistencia local mediante Room y DataStore.  
-  Acceso a recursos nativos: cámara y almacenamiento.  
-  Control de estado con ViewModel y StateFlow.  
-  Repositorio GitHub activo y Trello con planificación.  

---

## Indicadores de Logro (rúbrica DSY1105)

| Indicador | Descripción | Cumplimiento |
|------------|--------------|---------------|
| **IE 2.1.1** | Interfaz coherente, jerárquica y navegable |
| **IE 2.1.2** | Formularios validados con retroalimentación visual |
| **IE 2.2.1** | Lógica desacoplada y gestión de estado correcta |
| **IE 2.2.2** | Animaciones funcionales y coherentes |
| **IE 2.3.1** | Modularidad y persistencia local integradas |
| **IE 2.3.2** | Uso de GitHub y Trello con participación activa |
| **IE 2.4.1** | Acceso seguro a recursos nativos |

---

## Plan de Desarrollo (Sprints)

| Semana | Sprint | Objetivo | Entregables clave |
|:--:|:--|:--|:--|
| 1 | Diseño y estructura base | Proyecto inicial, navegación, arquitectura MVVM | Home, Detalle, Carrito |
| 2 | Formularios y validaciones | Formularios de login, registro, CRUD | Validaciones visuales |
| 3 | Persistencia y recursos nativos | Room + CameraX + almacenamiento | Datos persistentes |
| 4 | Animaciones y testing | Transiciones fluidas, pruebas UI | Experiencia refinada |
| 5 | Integración final y entrega | Consolidación, README, APK final | Demo funcional |

---

## Estrategia de Testing

- **Unit tests:** Validan lógica de negocio y ViewModels (JUnit + MockK).  
- **Integration tests:** Validan persistencia (Room DAOs).  
- **UI tests:** Validan navegación y flujos críticos (Espresso / ComposeTest).  
- **Cobertura esperada:** 60–80% de lógica crítica.

---

## Recursos Nativos Usados

- **Cámara / galería:** Subida de imagen del producto.  
- **Almacenamiento:** Guardado de datos en Room y lectura local.  

---

## Propósito y Justificación

La industria de la moda es una de las más contaminantes.  
Loopie busca **reducir el desperdicio textil** y fomentar la **moda sustentable**, dando valor a la reutilización y al trabajo artesanal.  
Desde la perspectiva técnica, el proyecto integra prácticas modernas de desarrollo móvil con arquitectura modular, pruebas y accesibilidad.

---

## Equipo de desarrollo

| Nombre | Rol | Responsabilidades |
|:--|:--|:--|
| [Michelle Melo] | Desarrollador Frontend y Backend| UI, Compose, validaciones | Persistencia, lógica, testing |

---
## API Endpoints Utilizados

La aplicación consume una API REST propia para gestionar usuarios y productos. La URL base configurada es `http://[TU_IP_LOCAL]:8080/`.

### Endpoints de Usuarios (`/api/v1/users`)
| Método | Ruta | Descripción |
|:--- |:--- |:---|
| **POST** | `/` | Registra un nuevo usuario. |
| **POST** | `/login` | Autentica a un usuario y devuelve sus datos. (*Asumido*) |
| **GET** | `/{id}` | Obtiene los datos de un usuario por su ID. |
| **GET** | `/correo/{correo}` | Obtiene un usuario por su correo electrónico. |
| **PUT** | `/{id}` | Actualiza los datos de un usuario (perfil). |
| **PUT** | `/{id}/change-password` | Cambia la contraseña de un usuario. |
| **DELETE** | `/{id}` | Elimina un usuario. |

### Endpoints de Productos (`/api/v1/products`)
| Método | Ruta | Descripción |
|:--- |:--- |:---|
| **POST** | `/` | Crea un nuevo producto. |
| **GET** | `/` | Obtiene la lista de todos los productos. |
| **GET** | `/{idProducto}` | Obtiene un producto por su ID. |
| **PUT** | `/{idProducto}` | Actualiza un producto existente. |
| **DELETE** | `/{idProducto}` | Elimina un producto. |

### Endpoints de Países (API Externa)
| Método | Ruta | Descripción |
|:--- |:--- |:---|
| **GET** | `https://restcountries.com/v3.1/all` | Obtiene la lista de todos los países. |


## Ejecución del proyecto

**Requisitos previos:**
- Android Studio Iguana | 2023.2.1 o superior 
- Gradle 8+  
- SDK mínimo 26  
- - JDK 17 o superior
- Emulador o dispositivo físico con Android 8+ (API 26) o superior.

**Ejecución:**
1. Clonar el repositorio:  
   ```bash
   git clone https://github.com/michmelo/LoopieApp.git
2. **Abrir en Android Studio:**
   - Abre Android Studio.
   - Selecciona `Open` y navega hasta la carpeta `LoopieApp` que acabas de clonar.
   - Espera a que Gradle sincronice el proyecto.
3. **Configurar la IP del Backend:**
   - **Importante:** La aplicación necesita conectarse a tu API REST local. Debes configurar la IP correcta.
   - Abre el archivo `app/src/main/java/com/example/loopieapp/Data/RetrofitClient.kt`.
   - Modifica la constante `BASE_URL` con la dirección IP de la máquina donde se ejecuta tu backend. Asegúrate de que tu teléfono o emulador estén en la misma red WiFi.
     
