-- Por si ya existe la tabla 
DROP TABLE IF EXISTS producto;

-- Creamos la tabla producto
CREATE TABLE producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    codigo VARCHAR(10) NOT NULL UNIQUE,

    nombre VARCHAR(80) NOT NULL,

    categoria VARCHAR(20) NOT NULL,

    precio NUMERIC(10,2) NOT NULL,

    stock INTEGER NOT NULL,

    CHECK (
        codigo = UPPER(TRIM(codigo))
        AND LENGTH(codigo) BETWEEN 1 AND 10
    ),

    CHECK (
        LENGTH(TRIM(nombre)) BETWEEN 3 AND 80
    ),

    CHECK (
        categoria IN (
            'Papelería',
            'Tecnología',
            'Accesorios'
        )
    ),

    CHECK (
        precio > 0
        AND precio <= 99999999.99
    ),

    CHECK (
        stock >= 0
    )
);

-- Insertarmos los 12 products
INSERT INTO producto (
    codigo,
    nombre,
    categoria,
    precio,
    stock
)
VALUES
    ('P001', 'Cuaderno universitario', 'Papelería', 65.00, 25),
    ('P002', 'Cuaderno cuadriculado', 'Papelería', 72.50, 12),
    ('P003', 'Lapicero azul', 'Papelería', 12.00, 80),
    ('P004', 'Resma de papel carta', 'Papelería', 185.00, 0),

    ('T001', 'Memoria USB 32 GB', 'Tecnología', 210.00, 8),
    ('T002', 'Mouse inalámbrico', 'Tecnología', 320.00, 6),
    ('T003', 'Teclado USB', 'Tecnología', 450.00, 4),
    ('T004', 'Cable HDMI', 'Tecnología', 180.00, 0),

    ('A001', 'Mochila escolar', 'Accesorios', 850.00, 5),
    ('A002', 'Estuche de lápices', 'Accesorios', 95.00, 18),
    ('A003', 'Botella reutilizable', 'Accesorios', 240.00, 0),
    ('A004', 'Regla metálica', 'Accesorios', 45.00, 20);

-- Mostrar los productos
SELECT *
FROM producto
ORDER BY codigo;

-- Aqui miramos si realmnte existen esos 12 productos
SELECT COUNT(*) AS total_productos
FROM producto;