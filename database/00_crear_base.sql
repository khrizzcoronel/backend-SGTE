-- Ejecutar en pgAdmin conectado a la base administrativa "postgres".
-- CREATE DATABASE debe ejecutarse fuera de un bloque de transaccion.
CREATE DATABASE "sgte-db01"
    WITH ENCODING = 'UTF8'
         TEMPLATE = template0;
