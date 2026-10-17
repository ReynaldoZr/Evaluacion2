DROP TABLE IF EXISTS producto;

CREATE TABLE producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(80) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL,

    CHECK (codigo = UPPER(TRIM(codigo)) AND LENGTH(codigo) BETWEEN 1 AND 10),
    CHECK (LENGTH(TRIM(nombre)) BETWEEN 3 AND 80),
    CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),
    CHECK (precio > 0 AND precio <= 99999999.99),
    CHECK (stock >= 0)
);
