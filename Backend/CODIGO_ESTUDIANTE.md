# Formato del Código de Estudiante

## Estructura: AASNNNN

El código de estudiante tiene **6 dígitos** que siguen el formato `AASNNNN`:

### Componentes:

1. **AA** (2 dígitos): Últimos 2 dígitos del año de ingreso
   - Ejemplo: `23` para el año 2023
   - Ejemplo: `22` para el año 2022

2. **S** (1 dígito): Semestre de ingreso
   - `1` = Primera mitad del año (Enero - Junio)
   - `2` = Segunda mitad del año (Julio - Diciembre)

3. **NNNN** (4 dígitos): Número correlativo del estudiante
   - Se genera automáticamente de forma secuencial
   - Comienza desde 0001 para cada año-semestre

## Ejemplos:

### Ejemplo 1: `231156`
- **23**: Año 2023
- **1**: Primer semestre (2023-1)
- **1156**: Estudiante número 1156 de ese periodo

**Interpretación**: Estudiante que ingresó en el primer semestre del año 2023

### Ejemplo 2: `222115`
- **22**: Año 2022
- **2**: Segundo semestre (2022-2)
- **2115**: Estudiante número 2115 de ese periodo

**Interpretación**: Estudiante que ingresó en el segundo semestre del año 2022

### Ejemplo 3: `260001`
- **26**: Año 2026
- **0**: (Error - debería ser 1 o 2)
- **0001**: Primer estudiante

**Nota**: Este código sería inválido porque el semestre debe ser 1 o 2.

## Generación Automática

El código se genera automáticamente cuando un estudiante ingresante completa su matrícula:

1. Se obtiene el año actual (últimos 2 dígitos)
2. Se determina el semestre según el mes:
   - Meses 1-6 → Semestre 1
   - Meses 7-12 → Semestre 2
3. Se cuenta cuántos estudiantes ya tienen códigos con ese prefijo (AAS)
4. Se asigna el siguiente número correlativo

## Validación

Para validar un código de estudiante:

```java
// Formato: AASNNNN (6 dígitos)
String regex = "^\\d{2}[12]\\d{4}$";
boolean esValido = codigoEstudiante.matches(regex);
```

Reglas de validación:
- Debe tener exactamente 6 dígitos
- El tercer dígito (semestre) debe ser 1 o 2
- Los primeros 2 dígitos deben representar un año válido (00-99)
- Los últimos 4 dígitos pueden ser cualquier número (0001-9999)

## Uso en el Sistema

El código de estudiante se utiliza para:
- Login de estudiantes regulares
- Identificación única en el sistema
- Consultas de historial académico
- Generación de reportes
- Matrícula en cursos
