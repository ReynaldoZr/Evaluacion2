package com.example.pruebacorte2.controller;

import com.example.pruebacorte2.dao.ProductoDAO;
import com.example.pruebacorte2.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductoController {
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkConExistencias;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private Label lblContador;
    @FXML private Label lblIdSeleccionado;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private final ObservableList<Producto> listaProductos =
            FXCollections.observableArrayList();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private static final Set<String> CATEGORIAS = Set.of(
            "Papelería", "Tecnología", "Accesorios");

    @FXML
    public void initialize() {
        configurarTabla();
        configurarCategorias();
        configurarSeleccion();
        buscarEnBaseDeDatos(false);
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tablaProductos.setItems(listaProductos);
    }

    private void configurarCategorias() {
        cmbCategoria.getItems().setAll(
                "Papelería", "Tecnología", "Accesorios");
        cmbFiltroCategoria.getItems().setAll(
                "Todas", "Papelería", "Tecnología", "Accesorios");
        cmbFiltroCategoria.setValue("Todas");
    }

    private void configurarSeleccion() {
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarProductoEnFormulario(seleccionado);
                    }
                });
    }

    private void cargarProductoEnFormulario(Producto producto) {
        lblIdSeleccionado.setText(String.valueOf(producto.getIdProducto()));
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(producto.getStock()));
    }

    @FXML
    private void buscarProductos() {
        buscarEnBaseDeDatos(true);
    }

    @FXML
    private void recargarProductos() {
        buscarEnBaseDeDatos(false);
    }

    private void buscarEnBaseDeDatos(boolean mostrarSinCoincidencias) {
        try {
            List<Producto> productos = productoDAO.buscar(
                    txtBuscar.getText(),
                    cmbFiltroCategoria.getValue(),
                    chkConExistencias.isSelected());

            listaProductos.setAll(productos);
            lblContador.setText("Registros visibles: " + listaProductos.size());

            if (mostrarSinCoincidencias && listaProductos.isEmpty()) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Sin coincidencias", "Sin coincidencias",
                        "No existen productos que coincidan con los filtros.");
            }
        } catch (SQLException ex) {
            mostrarErrorBaseDatos("No se pudieron consultar los productos.", ex);
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkConExistencias.setSelected(false);
        buscarEnBaseDeDatos(false);
    }

    @FXML
    private void guardarProducto() {
        try {
            Producto producto = obtenerProductoValidado();

            if (productoDAO.existeCodigo(producto.getCodigo(), null)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "No se puede guardar",
                        "El código ya pertenece a otro producto.");
                return;
            }

            int filas = productoDAO.guardar(producto);
            if (filas == 1) {
                limpiarFormulario();
                buscarEnBaseDeDatos(false);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto guardado",
                        "Operación completada",
                        "El producto se guardó correctamente.");
            }
        } catch (IllegalArgumentException ex) {
            mostrarValidacion(ex.getMessage());
        } catch (SQLException ex) {
            mostrarErrorBaseDatos("No se pudo guardar el producto.", ex);
        }
    }

    @FXML
    private void actualizarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarSeleccionRequerida("actualizar");
            return;
        }

        try {
            Producto producto = obtenerProductoValidado();
            producto.setIdProducto(seleccionado.getIdProducto());

            if (productoDAO.existeCodigo(
                    producto.getCodigo(), producto.getIdProducto())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "No se puede actualizar",
                        "El código ya pertenece a otro producto.");
                return;
            }

            int filas = productoDAO.actualizar(producto);
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto inexistente",
                        "No se actualizó ninguna fila",
                        "El producto ya no existe. Pulse Recargar.");
                return;
            }

            limpiarFormulario();
            buscarEnBaseDeDatos(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto actualizado",
                    "Operación completada",
                    "El producto se actualizó correctamente.");
        } catch (IllegalArgumentException ex) {
            mostrarValidacion(ex.getMessage());
        } catch (SQLException ex) {
            mostrarErrorBaseDatos("No se pudo actualizar el producto.", ex);
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarSeleccionRequerida("eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar producto");
        confirmacion.setContentText("¿Desea eliminar "
                + seleccionado.getCodigo() + " - "
                + seleccionado.getNombre() + "?");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            int filas = productoDAO.eliminar(seleccionado.getIdProducto());
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto inexistente",
                        "No se eliminó ninguna fila",
                        "El producto ya no existe. Pulse Recargar.");
                return;
            }

            limpiarFormulario();
            buscarEnBaseDeDatos(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto eliminado",
                    "Operación completada",
                    "El producto se eliminó correctamente.");
        } catch (SQLException ex) {
            mostrarErrorBaseDatos("No se pudo eliminar el producto.", ex);
        }
    }

    private Producto obtenerProductoValidado() {
        String codigo = txtCodigo.getText() == null
                ? "" : txtCodigo.getText().trim().toUpperCase(Locale.ROOT);
        String nombre = txtNombre.getText() == null
                ? "" : txtNombre.getText().trim();
        String categoria = cmbCategoria.getValue();
        String precioTexto = txtPrecio.getText() == null
                ? "" : txtPrecio.getText().trim();
        String stockTexto = txtStock.getText() == null
                ? "" : txtStock.getText().trim();

        if (codigo.isEmpty() || codigo.length() > 10) {
            throw new IllegalArgumentException(
                    "El código debe tener entre 1 y 10 caracteres.");
        }
        if (nombre.length() < 3 || nombre.length() > 80) {
            throw new IllegalArgumentException(
                    "El nombre debe tener entre 3 y 80 caracteres.");
        }
        if (!CATEGORIAS.contains(categoria)) {
            throw new IllegalArgumentException(
                    "Seleccione una categoría válida.");
        }
        if (!precioTexto.matches("\\d+(\\.\\d{1,2})?")) {
            throw new IllegalArgumentException(
                    "El precio debe ser numérico y tener máximo 2 decimales.");
        }

        BigDecimal precio = new BigDecimal(precioTexto);
        BigDecimal precioMaximo = new BigDecimal("99999999.99");
        if (precio.compareTo(BigDecimal.ZERO) <= 0
                || precio.compareTo(precioMaximo) > 0) {
            throw new IllegalArgumentException(
                    "El precio debe estar entre 0.01 y 99999999.99.");
        }

        if (!stockTexto.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "El stock debe ser un número entero no negativo.");
        }

        long stockLargo;
        try {
            stockLargo = Long.parseLong(stockTexto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "El stock supera el rango permitido.");
        }
        if (stockLargo > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "El stock debe estar entre 0 y 2147483647.");
        }

        return new Producto(0, codigo, nombre, categoria,
                precio, (int) stockLargo);
    }

    @FXML
    private void limpiarFormulario() {
        tablaProductos.getSelectionModel().clearSelection();
        lblIdSeleccionado.setText("Ninguno");
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtStock.clear();
        txtCodigo.requestFocus();
    }

    private void mostrarSeleccionRequerida(String operacion) {
        mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                "No hay un producto seleccionado",
                "Seleccione una fila antes de " + operacion + ".");
    }

    private void mostrarValidacion(String mensaje) {
        mostrarAlerta(Alert.AlertType.WARNING, "Datos incorrectos",
                "Revise el formulario", mensaje);
    }

    private void mostrarErrorBaseDatos(String mensaje, SQLException ex) {
        String sqlState = ex.getSQLState();
        String detalle;

        if (sqlState != null && sqlState.startsWith("08")) {
            detalle = "No se pudo conectar con PostgreSQL. Revise el servidor y "
                    + "las variables DB_URL, DB_USER y DB_PASSWORD.";
        } else if ("23505".equals(sqlState)) {
            detalle = "El código ingresado ya existe.";
        } else if ("23514".equals(sqlState) || "23502".equals(sqlState)) {
            detalle = "PostgreSQL rechazó los datos porque incumplen una restricción.";
        } else {
            detalle = mensaje + " Revise la consola para conocer el SQLState.";
        }

        mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos",
                "No se completó la operación", detalle);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo,
                               String encabezado, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
