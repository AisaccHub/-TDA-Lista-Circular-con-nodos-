package front_end;

import back_end.CircularList;
import back_end.Product;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class ListaGUI extends JFrame {

    // Colores del sistema: cambiar estas constantes cambia toda la apariencia
    private static final Color COLOR_FONDO = new Color(240, 240, 246);
    private static final Color COLOR_FORMULARIO = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(30, 30, 70);
    private static final Color COLOR_BORDE = new Color(110, 110, 160);
    private static final Color C_INSERTAR = new Color(198, 239, 206);
    private static final Color C_ELIMINAR = new Color(255, 199, 206);
    private static final Color C_MOSTRAR = new Color(204, 224, 255);
    private static final Color C_BUSCAR = new Color(255, 235, 156);
    private static final Color C_NAVEGAR = new Color(216, 206, 255);
    private static final Color C_AUXILIAR = new Color(221, 221, 221);

    private static final int IMG_ANCHO = 170;
    private static final int IMG_ALTO = 230;

    // Unica estructura que almacena elementos: lista circular doble de nodos
    private final CircularList catalogo = new CircularList();
    // Indice de la pelicula visible en la GUI (solo un numero, no guarda elementos)
    private int cursorPos = -1;

    private JTextField txtId, txtNombre, txtPrecio, txtDescripcion, txtRutaImagen, txtPosicion, txtBuscarId;
    private JTextArea taFicha;
    private JLabel lblImagen, lblContador;
    private JPanel pnlFormulario;

    public ListaGUI() {
        construirInterfaz();
        setTitle("Catalogo de peliculas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        refrescarVista();
    }

    // ------------------------- armado de la interfaz -------------------------

    private void construirInterfaz() {
        getContentPane().setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("Catalogo de peliculas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
        pnlCentro.setBackground(COLOR_FONDO);
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pnlCentro.add(crearPanelFormulario(), BorderLayout.NORTH);
        pnlCentro.add(crearPanelFicha(), BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBackground(COLOR_FORMULARIO);
        pnlFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDE), " Datos de la pelicula (record Product) "));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = campoTexto();
        txtNombre = campoTexto();
        txtPrecio = campoTexto();
        txtDescripcion = campoTexto();
        txtRutaImagen = campoTexto();
        txtPosicion = campoTexto();
        txtBuscarId = campoTexto();

        agregarFila(gbc, 0, "ID:", txtId);
        agregarFila(gbc, 1, "Nombre:", txtNombre);
        agregarFila(gbc, 2, "Precio:", txtPrecio);
        agregarFila(gbc, 3, "Descripcion:", txtDescripcion);

        JPanel pnlImagen = new JPanel(new BorderLayout(5, 0));
        pnlImagen.add(txtRutaImagen, BorderLayout.CENTER);
        pnlImagen.add(crearBoton("Examinar...", C_AUXILIAR, e -> examinarImagen()), BorderLayout.EAST);
        agregarFila(gbc, 4, "Foto (PNG/JPG):", pnlImagen);

        agregarFila(gbc, 5, "Posicion:", txtPosicion);
        agregarFila(gbc, 6, "Buscar por ID:", txtBuscarId);

        return pnlFormulario;
    }

    private void agregarFila(GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        pnlFormulario.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        pnlFormulario.add(campo, gbc);
    }

    private JTextField campoTexto() {
        JTextField t = new JTextField();
        t.setBackground(Color.WHITE);
        t.setForeground(new Color(20, 20, 20));
        return t;
    }

    private JPanel crearPanelFicha() {
        JPanel pnlFicha = new JPanel(new BorderLayout(10, 6));
        pnlFicha.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDE), " Ficha de la pelicula actual "));

        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setForeground(COLOR_TEXTO);
        lblImagen.setPreferredSize(new Dimension(IMG_ANCHO + 16, IMG_ALTO + 26));
        lblImagen.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        pnlFicha.add(lblImagen, BorderLayout.WEST);

        taFicha = new JTextArea(9, 40);
        taFicha.setEditable(false);
        taFicha.setLineWrap(true);
        taFicha.setWrapStyleWord(true);
        taFicha.setFont(new Font("Monospaced", Font.PLAIN, 13));
        pnlFicha.add(new JScrollPane(taFicha), BorderLayout.CENTER);

        lblContador = new JLabel(" Pelicula: -- de -- ", SwingConstants.CENTER);
        lblContador.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblContador.setForeground(COLOR_TEXTO);
        pnlFicha.add(lblContador, BorderLayout.SOUTH);

        return pnlFicha;
    }

    private JPanel crearPanelBotones() {
        JPanel pnlSur = new JPanel(new GridLayout(6, 1, 0, 4));
        pnlSur.setBorder(BorderFactory.createEmptyBorder(4, 8, 10, 8));

        pnlSur.add(filaBotones(
                crearBoton("Insertar al inicio", C_INSERTAR, e -> insertarAlInicio()),
                crearBoton("Insertar al final", C_INSERTAR, e -> insertarAlFinal()),
                crearBoton("Insertar en posicion", C_INSERTAR, e -> insertarEnPosicion())));

        pnlSur.add(filaBotones(
                crearBoton("Eliminar primero", C_ELIMINAR, e -> eliminarPrimero()),
                crearBoton("Eliminar ultimo", C_ELIMINAR, e -> eliminarUltimo()),
                crearBoton("Eliminar en posicion", C_ELIMINAR, e -> eliminarEnPosicion())));

        pnlSur.add(filaBotones(
                crearBoton("Mostrar primero", C_MOSTRAR, e -> mostrarPrimero()),
                crearBoton("Mostrar ultimo", C_MOSTRAR, e -> mostrarUltimo()),
                crearBoton("Mostrar en posicion", C_MOSTRAR, e -> mostrarEnPosicion())));

        pnlSur.add(filaBotones(
                crearBoton("Buscar por ID", C_BUSCAR, e -> buscarPorValor()),
                crearBoton("Buscar por posicion", C_BUSCAR, e -> buscarPorPosicion())));

        pnlSur.add(filaBotones(
                crearBoton("< Anterior", C_NAVEGAR, e -> anterior()),
                crearBoton("Siguiente >", C_NAVEGAR, e -> siguiente())));

        pnlSur.add(filaBotones(
                crearBoton("Limpiar catalogo", C_AUXILIAR, e -> limpiarCatalogo()),
                crearBoton("Salir", C_AUXILIAR, e -> System.exit(0))));

        return pnlSur;
    }

    private JPanel filaBotones(JButton... botones) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        for (JButton b : botones) {
            pnl.add(b);
        }
        return pnl;
    }

    private JButton crearBoton(String texto, Color color, ActionListener accion) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(new Color(20, 20, 20));
        b.setFocusPainted(false);
        b.addActionListener(accion);
        return b;
    }

    // ------------------------- validaciones -------------------------

    // Valida el formulario antes de instanciar el Product; devuelve null si hay error
    private Product crearProductoDesdeFormulario() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            float precio = Float.parseFloat(txtPrecio.getText().trim());

            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio.",
                        "Dato invalido", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) descripcion = "Sin descripcion";

            return new Product(id, precio, nombre, descripcion, txtRutaImagen.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de formato: el ID debe ser entero y el precio un numero decimal (ej. 9.99).",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Valida la posicion como entero dentro del rango; devuelve -1 si falla
    private int leerPosicionValida(int min, int max) {
        try {
            int pos = Integer.parseInt(txtPosicion.getText().trim());
            if (pos < min || pos > max) {
                JOptionPane.showMessageDialog(this,
                        "Indice fuera de rango: debe estar entre " + min + " y " + max
                                + " (tamanio actual: " + catalogo.getSize() + ").",
                        "Indice fuera de rango", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
            return pos;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La posicion debe ser un numero entero.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Devuelve null si el ID de busqueda no es numerico
    private Integer leerIdBusqueda() {
        try {
            return Integer.valueOf(txtBuscarId.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de busqueda debe ser un numero entero.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private boolean catalogoVacio() {
        if (catalogo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El catalogo esta vacio. Inserta una pelicula primero.",
                    "Catalogo de peliculas", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // ------------------------- insercion -------------------------

    private void insertarAlInicio() {
        Product p = crearProductoDesdeFormulario();
        if (p == null) return;

        boolean vaciaAntes = catalogo.isEmpty();
        catalogo.insertFirst(p);
        // Si estaba vacia el nuevo nodo es el indice 0; si no, los indices corren +1
        if (vaciaAntes) cursorPos = 0;
        else cursorPos++;

        refrescarVista();
    }

    private void insertarAlFinal() {
        Product p = crearProductoDesdeFormulario();
        if (p == null) return;

        boolean vaciaAntes = catalogo.isEmpty();
        catalogo.insertLast(p);
        if (vaciaAntes) cursorPos = 0;

        refrescarVista();
    }

    private void insertarEnPosicion() {
        Product p = crearProductoDesdeFormulario();
        if (p == null) return;

        // Se acepta hasta "size" porque insertar en esa posicion equivale a insertar al final
        int pos = leerPosicionValida(0, catalogo.getSize());
        if (pos == -1) return;

        boolean vaciaAntes = catalogo.isEmpty();
        catalogo.insertAt(p, pos);
        if (vaciaAntes) cursorPos = 0;
        else if (cursorPos >= pos) cursorPos++;

        refrescarVista();
    }

    // ------------------------- remocion -------------------------

    private void eliminarPrimero() {
        if (catalogoVacio()) return;
        if (!confirmar("¿Eliminar la primera pelicula del catalogo?")) return;

        Product eliminado = catalogo.removeFirst();
        if (eliminado == null) { refrescarVista(); return; }

        if (cursorPos > 0) cursorPos--;
        refrescarVista();
        JOptionPane.showMessageDialog(this, "Eliminada: " + eliminado.getName(),
                "Catalogo de peliculas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarUltimo() {
        if (catalogoVacio()) return;
        if (!confirmar("¿Eliminar la ultima pelicula del catalogo?")) return;

        Product eliminado = catalogo.removeLast();
        if (eliminado == null) { refrescarVista(); return; }

        // Si el indice visible era el del nodo eliminado, se regresa al primero
        if (catalogo.isEmpty()) cursorPos = -1;
        else if (cursorPos >= catalogo.getSize()) cursorPos = 0;

        refrescarVista();
        JOptionPane.showMessageDialog(this, "Eliminada: " + eliminado.getName(),
                "Catalogo de peliculas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarEnPosicion() {
        if (catalogoVacio()) return;
        int pos = leerPosicionValida(0, catalogo.getSize() - 1);
        if (pos == -1) return;
        if (!confirmar("¿Eliminar la pelicula en la posicion " + pos + "?")) return;

        Product eliminado = catalogo.removeAt(pos);
        if (eliminado == null) { refrescarVista(); return; }

        int size = catalogo.getSize();
        if (size == 0) cursorPos = -1;
        else if (cursorPos > pos) cursorPos--;
        else if (cursorPos == pos && cursorPos >= size) cursorPos = 0;

        refrescarVista();
        JOptionPane.showMessageDialog(this, "Eliminada: " + eliminado.getName(),
                "Catalogo de peliculas", JOptionPane.INFORMATION_MESSAGE);
    }

    // ------------------------- muestra -------------------------

    private void mostrarPrimero() {
        if (catalogoVacio()) return;
        moverCursorA(0);
        refrescarVista();
    }

    private void mostrarUltimo() {
        if (catalogoVacio()) return;
        moverCursorA(catalogo.getSize() - 1);
        refrescarVista();
    }

    private void mostrarEnPosicion() {
        if (catalogoVacio()) return;
        int pos = leerPosicionValida(0, catalogo.getSize() - 1);
        if (pos == -1) return;
        moverCursorA(pos);
        refrescarVista();
    }

    // ------------------------- busqueda -------------------------

    private void buscarPorValor() {
        if (catalogoVacio()) return;
        Integer id = leerIdBusqueda();
        if (id == null) return;

        // searchByValue recorre el anillo comparando el ID con tope "size" (sin bucles infinitos)
        int posicion = catalogo.searchByValue(id);
        if (posicion == -1) {
            JOptionPane.showMessageDialog(this, "No existe ninguna pelicula con ID " + id + ".",
                    "Resultado de la busqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }

        moverCursorA(posicion);
        refrescarVista();
        JOptionPane.showMessageDialog(this,
                "Encontrada la pelicula con ID " + id + " en la posicion " + posicion + " del anillo.",
                "Resultado de la busqueda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorPosicion() {
        if (catalogoVacio()) return;
        int pos = leerPosicionValida(0, catalogo.getSize() - 1);
        if (pos == -1) return;
        moverCursorA(pos);
        refrescarVista();
    }

    // ------------------------- navegacion circular -------------------------
    // El anillo es doble: siguiente de "ultimo" es "primero" y anterior de
    // "primero" es "ultimo", por eso next()/previous() ya hacen el transito.

    private void siguiente() {
        if (catalogoVacio()) return;
        Product p = catalogo.next();
        cursorPos = (cursorPos + 1) % catalogo.getSize();
        mostrarProducto(p);
        actualizarContador();
    }

    private void anterior() {
        if (catalogoVacio()) return;
        int size = catalogo.getSize();
        Product p = catalogo.previous();
        cursorPos = (cursorPos - 1 + size) % size;
        mostrarProducto(p);
        actualizarContador();
    }

    // Mueve el cursor interno de la lista hasta "destino" por el camino mas corto,
    // para que el cursor de la lista y el indice visible nunca se desincronicen
    private void moverCursorA(int destino) {
        int size = catalogo.getSize();
        if (size == 0) { cursorPos = -1; return; }

        destino = ((destino % size) + size) % size;
        int adelante = (destino - cursorPos + size) % size;
        int atras = (cursorPos - destino + size) % size;

        if (adelante <= atras) {
            for (int i = 0; i < adelante; i++) catalogo.next();
        } else {
            for (int i = 0; i < atras; i++) catalogo.previous();
        }
        cursorPos = destino;
    }

    // ------------------------- auxiliares -------------------------

    private void limpiarCatalogo() {
        if (catalogo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El catalogo ya esta vacio.",
                    "Catalogo de peliculas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!confirmar("¿Vaciar todo el catalogo? Se eliminaran todas las peliculas.")) return;

        catalogo.clear();
        cursorPos = -1;
        refrescarVista();
    }

    // ------------------------- vista -------------------------

    private void refrescarVista() {
        int size = catalogo.getSize();
        if (size == 0) {
            cursorPos = -1;
            mostrarProducto(null);
            actualizarContador();
            return;
        }
        if (cursorPos < 0 || cursorPos >= size) cursorPos = 0;
        mostrarProducto(catalogo.showAt(cursorPos));
        actualizarContador();
    }

    private void mostrarProducto(Product p) {
        if (p == null) {
            taFicha.setText("El catalogo esta vacio.\n\n"
                    + "Usa los botones de INSERTAR para agregar peliculas.");
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
            return;
        }

        taFicha.setText("  Posicion en el anillo: " + cursorPos + "\n"
                + "  -----------------------------------------\n"
                + "  ID:            " + p.getId() + "\n"
                + "  Nombre:        " + p.getName() + "\n"
                + "  Precio:        $" + String.format("%.2f", p.getPrice()) + "\n"
                + "  Descripcion:   " + p.getDescription() + "\n"
                + "  Foto (ruta):   " + (p.getImagePath().isEmpty() ? "(sin foto)" : p.getImagePath()) + "\n");
        taFicha.setCaretPosition(0);
        cargarImagen(p.getImagePath());
    }

    private void actualizarContador() {
        int size = catalogo.getSize();
        if (size == 0) {
            lblContador.setText(" Pelicula: -- de --  (catalogo vacio) ");
        } else {
            lblContador.setText(" Pelicula " + (cursorPos + 1) + " de " + size + "  (posicion " + cursorPos + ") ");
        }
    }
    // ------------------------- fotos PNG/JPG -------------------------
    private void examinarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto de la pelicula (PNG o JPG)");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Imagenes PNG y JPG (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRutaImagen.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void cargarImagen(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
            return;
        }
        try {
            File archivo = new File(ruta);
            if (!archivo.isFile()) {
                lblImagen.setIcon(null);
                lblImagen.setText("Imagen no disponible");
                return;
            }
            ImageIcon original = new ImageIcon(ruta);
            if (original.getIconWidth() == -1) {
                lblImagen.setIcon(null);
                lblImagen.setText("Formato no valido\n(use PNG o JPG)");
                return;
            }
            Image escalada = original.getImage().getScaledInstance(IMG_ANCHO, IMG_ALTO, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(escalada));
            lblImagen.setText("");
        } catch (Exception ex) {
            lblImagen.setIcon(null);
            lblImagen.setText("Error al cargar\nla imagen");
        }
    }

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListaGUI().setVisible(true));
    }
}
