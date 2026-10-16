package com.example.pruebacorte2.dao;

import com.example.pruebacorte2.Connection.DatabaseConnection;
import com.example.pruebacorte2.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscar(String texto, String categoria,
                                 boolean soloConExistencias) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT id_producto, codigo, nombre, categoria, precio, stock
                FROM producto
                WHERE 1 = 1
                """);
        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.isBlank()) {
            sql.append(" AND (codigo ILIKE ? OR nombre ILIKE ?)");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }

        if (categoria != null && !categoria.equals("Todas")) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria);
        }

        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }

        sql.append(" ORDER BY codigo ASC");
        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = DatabaseConnection.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productos.add(crearProducto(resultSet));
                }
            }
            return productos;
        } catch (SQLException ex) {
            registrarError("BUSCAR", ex);
            throw ex;
        }
    }

    public boolean existeCodigo(String codigo, Integer idProductoExcluido)
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM producto WHERE codigo = ?";
        if (idProductoExcluido != null) {
            sql += " AND id_producto <> ?";
        }

        try (Connection conexion = DatabaseConnection.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, codigo);
            if (idProductoExcluido != null) {
                statement.setInt(2, idProductoExcluido);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            registrarError("VERIFICAR CÓDIGO", ex);
            throw ex;
        }
    }

    public int guardar(Producto producto) throws SQLException {
        String sql = """
                INSERT INTO producto (codigo, nombre, categoria, precio, stock)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conexion = DatabaseConnection.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            asignarDatos(statement, producto);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            registrarError("GUARDAR", ex);
            throw ex;
        }
    }

    public int actualizar(Producto producto) throws SQLException {
        String sql = """
                UPDATE producto
                SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ?
                WHERE id_producto = ?
                """;
        try (Connection conexion = DatabaseConnection.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            asignarDatos(statement, producto);
            statement.setInt(6, producto.getIdProducto());
            return statement.executeUpdate();
        } catch (SQLException ex) {
            registrarError("ACTUALIZAR", ex);
            throw ex;
        }
    }

    public int eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conexion = DatabaseConnection.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, idProducto);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            registrarError("ELIMINAR", ex);
            throw ex;
        }
    }

    private void asignarDatos(PreparedStatement statement, Producto producto)
            throws SQLException {
        statement.setString(1, producto.getCodigo());
        statement.setString(2, producto.getNombre());
        statement.setString(3, producto.getCategoria());
        statement.setBigDecimal(4, producto.getPrecio());
        statement.setInt(5, producto.getStock());
    }

    private Producto crearProducto(ResultSet resultSet) throws SQLException {
        return new Producto(
                resultSet.getInt("id_producto"),
                resultSet.getString("codigo"),
                resultSet.getString("nombre"),
                resultSet.getString("categoria"),
                resultSet.getBigDecimal("precio"),
                resultSet.getInt("stock")
        );
    }

    private void registrarError(String operacion, SQLException ex) {
        System.err.println("Operación: " + operacion
                + " | SQLState: " + ex.getSQLState()
                + " | Mensaje: " + ex.getMessage());
    }
}
