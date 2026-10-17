DROP TABLE IF EXISTS producto;

CREATE TABLE producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(80) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL,

    CONSTRAINT chk_producto_codigo
        CHECK (codigo = UPPER(TRIM(codigo)) AND LENGTH(codigo) BETWEEN 1 AND 10),
    CONSTRAINT chk_producto_nombre
        CHECK (LENGTH(TRIM(nombre)) BETWEEN 3 AND 80),
    CONSTRAINT chk_producto_categoria
        CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),
    CONSTRAINT chk_producto_precio
        CHECK (precio > 0 AND precio <= 99999999.99),
    CONSTRAINT chk_producto_stock
        CHECK (stock >= 0)
);
