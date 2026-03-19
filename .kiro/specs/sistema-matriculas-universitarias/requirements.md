# Documento de Requisitos

## Introducción

El Sistema de Matrículas Universitarias es una aplicación web que permite gestionar el proceso de matrícula académica para dos tipos de estudiantes: ingresantes (nuevos alumnos) y regulares (alumnos con historial académico). El sistema cuenta con un módulo de administración para gestionar pagos, documentos, anuncios, cursos y archivos estáticos (horarios, mallas curriculares y reglamento). La autenticación se delega a Keycloak como proveedor de identidad centralizado.

## Glosario

- **Sistema**: La aplicación web de matrículas universitarias (backend Spring Boot + frontend Angular).
- **Keycloak**: Servidor de autenticación y autorización que emite tokens JWT.
- **Estudiante_Ingresante**: Alumno nuevo que se matricula por primera vez en la universidad.
- **Estudiante_Regular**: Alumno con historial académico previo en la universidad.
- **Admin**: Administrador del sistema con acceso al panel de gestión.
- **Voucher**: Código de comprobante de pago emitido por tesorería.
- **Periodo_Academico**: Ciclo académico identificado por año y semestre (I o II).
- **Matricula**: Registro formal de inscripción de un estudiante en cursos de un periodo académico.
- **Detalle_Matricula**: Registro individual de un curso dentro de una matrícula.
- **Prerrequisito**: Curso que debe estar aprobado antes de poder matricularse en otro curso.
- **Creditos_Maximos**: Límite de créditos que un estudiante puede matricular en un periodo.
- **Carrera**: Programa académico al que pertenece un estudiante, definido en el JSON de carreras.
- **Archivo_Estatico**: Archivo PDF o JSON almacenado en el servidor bajo `src/main/resources/static/`.
- **Malla_Curricular**: Documento PDF con el plan de estudios de una carrera.
- **Horario**: Documento PDF con los horarios de clases de una carrera.
- **Reglamento**: Documento PDF general con las normas académicas de la universidad.
- **JSON_Carreras**: Archivo JSON en la carpeta `carreras/` que es la fuente de verdad de las carreras disponibles.
- **Token_JWT**: Token de acceso emitido por Keycloak tras autenticación exitosa.

---

## Requisitos

### Requisito 1: Autenticación de Administrador

**Historia de usuario:** Como administrador, quiero iniciar sesión con mi usuario y contraseña, para acceder al panel de gestión del sistema.

#### Criterios de Aceptación

1. WHEN un administrador envía usuario y contraseña válidos, THE Sistema SHALL autenticar al administrador mediante Keycloak y retornar un Token_JWT junto con los datos del administrador.
2. WHEN un administrador envía credenciales inválidas, THE Sistema SHALL retornar un mensaje de error descriptivo sin revelar información sensible.
3. WHEN el administrador autenticado está inactivo en la base de datos, THE Sistema SHALL rechazar el acceso y retornar un mensaje de error indicando que la cuenta está inactiva.
4. WHEN un administrador se autentica exitosamente, THE Sistema SHALL redirigir al panel de administración (`/admin/dashboard`).
5. IF el campo usuario o contraseña está vacío, THEN THE Sistema SHALL impedir el envío del formulario y mostrar un mensaje de validación.

---

### Requisito 2: Autenticación de Estudiante Regular (Dashboard)

**Historia de usuario:** Como estudiante regular, quiero iniciar sesión con mi DNI y código de estudiante, para acceder a mi dashboard académico.

#### Criterios de Aceptación

1. WHEN un estudiante regular envía DNI y código de estudiante válidos, THE Sistema SHALL autenticar al estudiante mediante Keycloak y retornar un Token_JWT junto con los datos del estudiante.
2. WHEN el estudiante autenticado no es de tipo REGULAR, THE Sistema SHALL rechazar el acceso y retornar un mensaje de error.
3. WHEN el estudiante autenticado tiene estado distinto a ACTIVO, THE Sistema SHALL rechazar el acceso y retornar un mensaje de error indicando el estado de la cuenta.
4. WHEN un estudiante regular se autentica exitosamente, THE Sistema SHALL redirigir al dashboard del estudiante regular (`/regular`).
5. IF el campo DNI o código de estudiante está vacío, THEN THE Sistema SHALL impedir el envío del formulario y mostrar un mensaje de validación.

---

### Requisito 3: Autenticación de Matrícula Regular

**Historia de usuario:** Como estudiante regular, quiero iniciar sesión con mi DNI y voucher de pago, para acceder al proceso de selección de cursos.

#### Criterios de Aceptación

1. WHEN un estudiante regular envía DNI y voucher válidos, THE Sistema SHALL verificar que el voucher exista, esté validado y pertenezca al estudiante, luego autenticar mediante Keycloak y retornar un Token_JWT.
2. WHEN el voucher no existe en la base de datos, THE Sistema SHALL retornar un mensaje de error indicando que el voucher no existe.
3. WHEN el voucher existe pero no está validado por tesorería, THE Sistema SHALL retornar un mensaje de error indicando que el voucher no ha sido validado.
4. WHEN el voucher pertenece a otro estudiante, THE Sistema SHALL retornar un mensaje de error indicando que el voucher no corresponde al estudiante.
5. WHEN el voucher no es de tipo MATRICULA, THE Sistema SHALL retornar un mensaje de error indicando el tipo de voucher incorrecto.
6. WHEN la autenticación de matrícula regular es exitosa, THE Sistema SHALL redirigir al módulo de selección de cursos (`/matricula-regular`).
7. IF el campo DNI o voucher está vacío, THEN THE Sistema SHALL impedir el envío del formulario y mostrar un mensaje de validación.

---

### Requisito 4: Autenticación de Ingresante

**Historia de usuario:** Como estudiante ingresante, quiero iniciar sesión con mi DNI y voucher de pago, para acceder al proceso de matrícula de ingresante.

#### Criterios de Aceptación

1. WHEN un ingresante envía DNI y voucher válidos, THE Sistema SHALL autenticar al ingresante mediante Keycloak y retornar un Token_JWT junto con los datos del estudiante.
2. WHEN el usuario autenticado no es de tipo INGRESANTE, THE Sistema SHALL rechazar el acceso y retornar un mensaje de error.
3. WHEN la autenticación de ingresante es exitosa, THE Sistema SHALL redirigir al módulo de matrícula de ingresante (`/matricula-ingresante`).
4. IF el campo DNI o voucher está vacío, THEN THE Sistema SHALL impedir el envío del formulario y mostrar un mensaje de validación.

---

### Requisito 5: Página de Inicio y Navegación

**Historia de usuario:** Como visitante, quiero ver la página de inicio con opciones claras de acceso, para poder navegar hacia el módulo que necesito.

#### Criterios de Aceptación

1. THE Sistema SHALL mostrar en la página de inicio un botón "Quiero matricularme" que presente dos opciones de redirección: matrícula regular y matrícula ingresante.
2. WHEN el visitante selecciona "Matrícula Regular", THE Sistema SHALL redirigir a la ruta `/login-matricula-regular`.
3. WHEN el visitante selecciona "Matrícula Ingresante", THE Sistema SHALL redirigir a la ruta `/login-ingresante`.
4. THE Sistema SHALL mostrar en la página de inicio los anuncios activos publicados por el administrador.
5. WHEN la página de inicio carga, THE Sistema SHALL obtener y mostrar los anuncios activos ordenados por fecha de creación descendente.

---

### Requisito 6: Matrícula de Ingresante

**Historia de usuario:** Como estudiante ingresante autenticado, quiero completar mi proceso de matrícula, para quedar inscrito en los cursos del primer semestre.

#### Criterios de Aceptación

1. WHEN un ingresante autenticado completa el formulario de matrícula con datos personales y documentos requeridos, THE Sistema SHALL registrar al estudiante, guardar sus documentos y crear la matrícula en el periodo académico abierto.
2. WHEN se registra la matrícula de un ingresante, THE Sistema SHALL inscribir automáticamente al estudiante en todos los cursos del primer semestre (semestre = 1).
3. WHEN se registra un ingresante, THE Sistema SHALL generar un código de estudiante único con el formato AASNNNN (AA=año, S=semestre, NNNN=correlativo de 4 dígitos).
4. WHEN no existe un Periodo_Academico con estado ABIERTO, THE Sistema SHALL retornar un error indicando que no hay periodo académico disponible.
5. IF el voucher proporcionado en el registro no está validado, THEN THE Sistema SHALL rechazar la matrícula y retornar un mensaje de error.
6. WHEN la matrícula del ingresante se completa exitosamente, THE Sistema SHALL retornar los datos de la matrícula creada incluyendo el código de estudiante generado.

---

### Requisito 7: Matrícula de Estudiante Regular

**Historia de usuario:** Como estudiante regular autenticado, quiero seleccionar los cursos para matricularme, para inscribirme en el periodo académico vigente respetando las reglas académicas.

#### Criterios de Aceptación

1. WHEN un estudiante regular solicita los cursos disponibles, THE Sistema SHALL retornar únicamente los cursos que el estudiante no ha aprobado, no tiene en curso actualmente, y cuyos prerrequisitos ha cumplido.
2. WHEN un estudiante regular envía su selección de cursos, THE Sistema SHALL validar que el total de créditos no exceda el límite de Creditos_Maximos del estudiante.
3. WHEN un estudiante ha desaprobado un curso 4 veces, THE Sistema SHALL rechazar la matrícula en ese curso e indicar que no puede continuar.
4. WHEN un estudiante ha desaprobado un curso exactamente 3 veces, THE Sistema SHALL permitir matricularse únicamente en ese curso y rechazar cualquier selección adicional.
5. WHEN la selección de cursos es válida, THE Sistema SHALL crear la matrícula con el detalle de cada curso en el periodo académico abierto.
6. WHEN se registra el detalle de matrícula, THE Sistema SHALL incrementar el contador `veces_llevado` basándose en el historial previo del estudiante en ese curso.
7. IF el estudiante no está en estado ACTIVO, THEN THE Sistema SHALL rechazar la matrícula y retornar un mensaje de error.

---

### Requisito 8: Dashboard del Estudiante Regular

**Historia de usuario:** Como estudiante regular autenticado, quiero ver mi información académica y acceder a recursos, para gestionar mi situación académica.

#### Criterios de Aceptación

1. WHEN un estudiante regular accede a su dashboard, THE Sistema SHALL mostrar sus datos personales, carrera, estado y créditos máximos.
2. WHEN un estudiante regular solicita su historial académico, THE Sistema SHALL retornar el detalle de todas sus matrículas con cursos, notas y estados.
3. WHEN un estudiante regular solicita ver el horario de su carrera, THE Sistema SHALL servir el archivo PDF correspondiente a su carrera desde la carpeta `horarios/`.
4. WHEN un estudiante regular solicita ver la malla curricular de su carrera, THE Sistema SHALL servir el archivo PDF correspondiente a su carrera desde la carpeta `mallas/`.
5. WHEN un estudiante regular solicita ver el reglamento, THE Sistema SHALL servir el archivo PDF `reglamento_general.pdf` desde la carpeta `reglamento/`.
6. IF no existe un archivo de horario o malla para la carrera del estudiante, THEN THE Sistema SHALL retornar un mensaje de error indicando que el archivo no está disponible.

---

### Requisito 9: Gestión de Archivos Estáticos (Admin)

**Historia de usuario:** Como administrador, quiero subir y actualizar archivos estáticos (horarios, mallas y reglamento), para mantener la información académica actualizada para los estudiantes.

#### Criterios de Aceptación

1. WHEN un administrador sube un archivo de horario para una carrera, THE Sistema SHALL guardar el archivo en la carpeta `horarios/` con el nombre normalizado de la carrera (sin tildes, en minúsculas, espacios reemplazados por guiones bajos) con extensión `.pdf`, reemplazando el archivo anterior si existe.
2. WHEN un administrador sube un archivo de malla curricular para una carrera, THE Sistema SHALL guardar el archivo en la carpeta `mallas/` con el nombre normalizado de la carrera con extensión `.pdf`, reemplazando el archivo anterior si existe.
3. WHEN un administrador sube el reglamento general, THE Sistema SHALL guardar el archivo como `reglamento_general.pdf` en la carpeta `reglamento/`, reemplazando el archivo anterior si existe.
4. IF el archivo subido está vacío, THEN THE Sistema SHALL rechazar la operación y retornar un mensaje de error.
5. WHEN se reemplaza un archivo existente, THE Sistema SHALL eliminar el archivo anterior antes de guardar el nuevo.

---

### Requisito 10: Gestión de Carreras (JSON)

**Historia de usuario:** Como sistema, quiero que la lista de carreras disponibles provenga de un único archivo JSON, para garantizar consistencia en todos los campos de selección de carrera.

#### Criterios de Aceptación

1. THE Sistema SHALL leer la lista de carreras disponibles exclusivamente desde el archivo JSON ubicado en la carpeta `carreras/` de los archivos estáticos.
2. WHEN cualquier componente del sistema requiera mostrar un campo de selección de carrera, THE Sistema SHALL obtener las opciones desde el JSON_Carreras.
3. WHEN el JSON_Carreras no está disponible o tiene formato inválido, THE Sistema SHALL retornar un error descriptivo e impedir la operación que depende de él.

---

### Requisito 11: Gestión de Pagos (Admin)

**Historia de usuario:** Como administrador, quiero gestionar los pagos de los estudiantes, para validar o rechazar comprobantes de pago antes de que se usen en el proceso de matrícula.

#### Criterios de Aceptación

1. WHEN un administrador solicita la lista de pagos pendientes de validación, THE Sistema SHALL retornar todos los pagos con `validado = false`.
2. WHEN un administrador valida un pago, THE Sistema SHALL actualizar el campo `validado` a `true` para ese pago.
3. WHEN un administrador intenta validar un pago ya validado, THE Sistema SHALL retornar un error indicando que el pago ya fue validado.
4. WHEN un administrador rechaza un pago no validado, THE Sistema SHALL eliminar el registro del pago de la base de datos.
5. WHEN un administrador intenta rechazar un pago ya validado, THE Sistema SHALL retornar un error indicando que no se puede rechazar un pago validado.
6. WHEN un administrador registra un nuevo pago, THE Sistema SHALL verificar que no exista otro pago validado con el mismo voucher antes de guardarlo.

---

### Requisito 12: Gestión de Anuncios (Admin)

**Historia de usuario:** Como administrador, quiero crear y gestionar anuncios, para comunicar información relevante a los estudiantes en la página de inicio.

#### Criterios de Aceptación

1. WHEN un administrador crea un anuncio con título, contenido y tipo válidos, THE Sistema SHALL guardar el anuncio con estado activo y fecha de creación actual.
2. WHEN un administrador actualiza un anuncio existente, THE Sistema SHALL modificar los campos título, contenido, tipo, fecha de inicio y fecha de fin.
3. WHEN un administrador desactiva un anuncio, THE Sistema SHALL actualizar el campo `activo` a `false` sin eliminar el registro.
4. WHEN se solicitan los anuncios activos, THE Sistema SHALL retornar únicamente los anuncios con `activo = true`, ordenados por fecha de creación descendente.

---

### Requisito 13: Gestión de Estudiantes (Admin)

**Historia de usuario:** Como administrador, quiero gestionar los datos de los estudiantes, para mantener actualizado el registro académico.

#### Criterios de Aceptación

1. WHEN un administrador solicita la lista de estudiantes, THE Sistema SHALL retornar todos los estudiantes registrados con sus datos completos.
2. WHEN un administrador actualiza el estado de un estudiante, THE Sistema SHALL persistir el nuevo estado (ACTIVO, INHABILITADO o RETIRADO).
3. WHEN un administrador actualiza los créditos máximos de un estudiante, THE Sistema SHALL validar que el valor esté entre 0 y 30 antes de persistir el cambio.
4. WHEN un administrador consulta los documentos de un ingresante, THE Sistema SHALL retornar el registro de documentos asociado al estudiante.
5. WHEN un administrador verifica los documentos de un ingresante, THE Sistema SHALL actualizar el campo `verificado` a `true`.

---

### Requisito 14: Gestión de Periodos Académicos (Admin)

**Historia de usuario:** Como administrador, quiero gestionar los periodos académicos, para controlar cuándo está habilitado el proceso de matrícula.

#### Criterios de Aceptación

1. WHEN un administrador abre un periodo académico, THE Sistema SHALL actualizar su estado a ABIERTO.
2. WHEN un administrador cierra un periodo académico, THE Sistema SHALL actualizar su estado a CERRADO.
3. THE Sistema SHALL permitir que exista como máximo un Periodo_Academico con estado ABIERTO en cualquier momento.
4. WHEN se consultan los periodos académicos, THE Sistema SHALL retornar la lista completa ordenada por año y semestre descendente.

---

### Requisito 15: Sincronización con Keycloak (Admin)

**Historia de usuario:** Como administrador, quiero que los usuarios del sistema estén sincronizados con Keycloak, para que la autenticación funcione correctamente para todos los usuarios.

#### Criterios de Aceptación

1. WHEN se registra un nuevo estudiante ingresante, THE Sistema SHALL crear el usuario correspondiente en Keycloak con DNI como username y voucher como contraseña.
2. WHEN se registra un nuevo estudiante regular en Keycloak, THE Sistema SHALL usar el DNI como username y el código de estudiante como contraseña.
3. WHEN un administrador ejecuta la sincronización manual de usuarios, THE Sistema SHALL crear en Keycloak todos los usuarios que existen en la base de datos local pero no en Keycloak.
4. IF la sincronización con Keycloak falla para un usuario, THEN THE Sistema SHALL registrar el error y continuar con los demás usuarios sin interrumpir el proceso completo.
