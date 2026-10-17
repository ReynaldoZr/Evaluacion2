# Pruebas del catálogo de productos

La base se restauró con los 12 productos de `sql/datos.sql`.
Las capturas están ordenadas dentro de la carpeta `evidencias`.

## Búsquedas y filtros

| Prueba | Entrada | Resultado obtenido | Estado |
|---|---|---|---|
| B1 | Texto `CUADERNO` | P001 y P002, total 2 | Correcto |
| B2 | Texto `T00` | T001, T002, T003 y T004, total 4 | Correcto |
| B3 | Categoría Papelería | P001, P002, P003 y P004, total 4 | Correcto |
| B4 | Solo con existencias | 9 productos, sin P004, T004 ni A003 | Correcto |
| B5 | `USB`, Tecnología y con existencias | T001 y T003, total 2 | Correcto |
| B6 | Texto `ZZZ999` | 0 productos | Correcto |
| B7 | Limpiar filtros | Controles limpios y 12 productos | Correcto |

Las consultas anteriores fueron comprobadas directamente en PostgreSQL.
La aplicación realiza cada búsqueda mediante el DAO.

## Operaciones CRUD

Se realizó una prueba equivalente con `L015`, Laptop Gamer, Tecnología,
precio 950.00 y stock 27. El producto se guardó y luego se eliminó.
La tabla regresó a los 12 registros iniciales.

Prueba exacta indicada en la guía, pendiente de ejecución final:

1. Guardar `X001`, Producto de prueba, Accesorios, 10.00, stock 1.
2. Actualizar el precio a 15.50 y el stock a 3.
3. Cancelar una eliminación y comprobar que permanece.
4. Confirmar la eliminación y comprobar que vuelven a quedar 12 registros.

## Validaciones

| Prueba | Datos que se deben probar | Revisión actual |
|---|---|---|
| V1 | Código vacío, espacios y 11 caracteres | Implementado |
| V2 | Nombre con espacios, `AB` y 81 caracteres | Implementado |
| V3 | Categoría sin seleccionar | Implementado |
| V4 | Precio `abc` | Implementado |
| V5 | Precio `0` y `-1` | Implementado |
| V6 | Precio `12.345` y `100000000.00` | Implementado |
| V7 | Stock `abc` y `2.5` | Implementado |
| V8 | Stock `-1`, `2147483648` y `0` | Implementado |
| V9 | Código duplicado con espacios o minúsculas | Implementado |
| V10 | Actualizar o eliminar sin selección | Implementado |

La ejecución manual de todos los subcasos queda pendiente de la revisión final.

## Manejo de errores

| Prueba | Comprobación | Revisión actual |
|---|---|---|
| E1 | Conexión usando un puerto incorrecto | Manejado en el controlador |
| E2 | Duplicado o CHECK fallido desde el DAO | SQLException manejada |
| E3 | UPDATE o DELETE con cero filas afectadas | Implementado |
| E4 | Registrar operación y SQLState sin mostrar datos sensibles | Implementado |

E1 a E4 fueron revisadas en el código. Las pruebas provocando errores reales
quedan pendientes de ejecución manual.
