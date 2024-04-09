package asignaturasentrega;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ModificadorDeTablas {

	private JFrame frame;
	private JTable table;
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtHoras;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModificadorDeTablas window = new ModificadorDeTablas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModificadorDeTablas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public  void limpiar() {
		txtCodigo.setText("");
		txtNombre.setText("");
		txtHoras.setText("");
		
	}
	
	public static void mostrar(DefaultTableModel model) {
		if (model.getRowCount() > 0) {
			model.setRowCount(0);
		}
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM asignatura");
			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("horas");

				model.addRow(row);
			}
			rs.close();
			stmt.close();
			con.close();

		} catch (SQLException ex) { // Caso erróneo
			JOptionPane.showMessageDialog(null, 
					"No se a podido cargar los datos/n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 642, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Código");
		model.addColumn("Nombre");
		model.addColumn("Horas");
		frame.getContentPane().setLayout(null);
		table = new JTable(model);
		table.setBounds(1, 1, 629, 0);
		frame.getContentPane().add(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		scrollPane.setBounds(0, 0, 644, 233);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblCdigo = new JLabel("Código:");
		lblCdigo.setBounds(74, 268, 70, 15);
		frame.getContentPane().add(lblCdigo);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(74, 295, 70, 15);
		frame.getContentPane().add(lblNombre);
		
		JLabel lblHoras = new JLabel("Horas:");
		lblHoras.setBounds(74, 322, 70, 15);
		frame.getContentPane().add(lblHoras);
		
		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(149, 266, 114, 19);
		frame.getContentPane().add(txtCodigo);
		txtCodigo.setColumns(10);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(149, 293, 114, 19);
		frame.getContentPane().add(txtNombre);
		txtNombre.setColumns(10);
		
		txtHoras = new JTextField();
		txtHoras.setBounds(149, 320, 114, 19);
		frame.getContentPane().add(txtHoras);
		txtHoras.setColumns(10);
		
		JButton btnGuardar = new JButton("Guardar");
		
		btnGuardar.setBounds(339, 263, 117, 25);
		frame.getContentPane().add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nombre = txtNombre.getText();
				String hora = txtHoras.getText();
				String codigo = txtCodigo.getText();
				if (nombre == null || nombre.isEmpty() ) {
					JOptionPane.showMessageDialog(null, "Falta el nombre", "ERROR", JOptionPane.ERROR_MESSAGE);
				
				} else if (hora == null || hora.isEmpty() ) {
					JOptionPane.showMessageDialog(null, "Falta las horas", "ERROR", JOptionPane.ERROR_MESSAGE);


					
				}else if (!txtCodigo.getText().isEmpty()) {
					try {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement ins_pstmt = con.prepareStatement("update asignatura set nombre=?,horas=? where id=?");
						ins_pstmt.setString(1, nombre);
						ins_pstmt.setInt(2, Integer.valueOf(hora));
						ins_pstmt.setInt(3, Integer.valueOf(codigo));
						ins_pstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "Asignatura actualizada");
						limpiar();
						mostrar(model);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		btnActualizar.setBounds(339, 290, 117, 25);
		frame.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String codigo = txtCodigo.getText();
				if (!txtCodigo.getText().isEmpty()) {
					
					try {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement ins_pstmt = con.prepareStatement("delete from asignatura where id=?");
						ins_pstmt.setString(1, codigo);
						ins_pstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "Asignatura borrada correctamente");
						limpiar();
						mostrar(model);

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else  {
					JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrar.setBounds(339, 317, 117, 25);
		frame.getContentPane().add(btnBorrar);
		mostrar(model);
		table.addMouseListener((MouseListener) new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				TableModel model = table.getModel();
				txtCodigo.setText(model.getValueAt(index, 0).toString());
				txtNombre.setText(model.getValueAt(index, 1).toString());
				txtHoras.setText(model.getValueAt(index, 2).toString());
			}
		});

		

btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nombre = txtNombre.getText();
				String hora = txtHoras.getText();
				if (nombre == null || nombre.isEmpty() ) {
					JOptionPane.showMessageDialog(null, "Falta el nombre", "ERROR", JOptionPane.ERROR_MESSAGE);
				
				} else if (hora == null || hora.isEmpty() ) {
					JOptionPane.showMessageDialog(null, "Falta las horas", "ERROR", JOptionPane.ERROR_MESSAGE);


					
				}else if (txtCodigo.getText().isEmpty()) {
					try {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement ins_pstmt = con.prepareStatement("insert into asignatura (nombre,horas) values (?,?)");
						ins_pstmt.setString(1, nombre);
						ins_pstmt.setInt(2, Integer.valueOf(hora));
						ins_pstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "Asignatura añadida");
						limpiar();
						mostrar(model);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		
	}
}
