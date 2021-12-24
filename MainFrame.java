package ru.avalon.javapp.devj120.avalontelecom.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import ru.avalon.javapp.devj120.avalontelecom.models.ClientInfo;
import ru.avalon.javapp.devj120.avalontelecom.models.Company;
import ru.avalon.javapp.devj120.avalontelecom.models.Person;
import ru.avalon.javapp.devj120.avalontelecom.models.PhoneNumber;

/**
 * Application main window.
 */
public class MainFrame extends JFrame {
	private ClientListTableModel clientsTableModel = new ClientListTableModel();
	private JTable clientsTable = new JTable();
	private ClientDialog client = new ClientDialog(this);

	public MainFrame() {
		super("AvalonTelecom Ltd. clients list");

		initMenu();
		initLayout();

		setBounds(300, 200, 600, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu operations = new JMenu("Operations");
		operations.setMnemonic('O');
		menuBar.add(operations);

		addMenuItemTo(operations, "Add Person", 'P',
				KeyStroke.getKeyStroke('P', InputEvent.ALT_DOWN_MASK),
				e -> addPerson());

		addMenuItemTo(operations, "Add Company", 'A',
				KeyStroke.getKeyStroke('A', InputEvent.ALT_DOWN_MASK),
				e -> addCompany());

		addMenuItemTo(operations, "Change", 'C',
				KeyStroke.getKeyStroke('C', InputEvent.ALT_DOWN_MASK),
				e -> changeClient());

		addMenuItemTo(operations, "Delete", 'D',
				KeyStroke.getKeyStroke('D', InputEvent.ALT_DOWN_MASK),
				e -> delClient());

		setJMenuBar(menuBar);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					clientsTableModel.save();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Auxiliary method, which creates menu item with specifies text, mnemonic and accelerator,
	 * installs specified action listener to the item, and adds the item to the specified menu.
	 *
	 * @param parent menu, which the created item is added to
	 */
	private void addMenuItemTo(JMenu parent, String text, char mnemonic,
							   KeyStroke accelerator, ActionListener al) {
		JMenuItem mi = new JMenuItem(text, mnemonic);
		mi.setAccelerator(accelerator);
		mi.addActionListener(al);
		parent.add(mi);
	}

	private void initLayout() {
		clientsTable.setModel(clientsTableModel);
		clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		add(clientsTable.getTableHeader(), BorderLayout.NORTH);
		add(new JScrollPane(clientsTable), BorderLayout.CENTER);
	}

	private void addPerson() {
		client.prepareForAddPerson();
		while (client.showModal()) {
			try {
				PhoneNumber pn = new PhoneNumber(client.getAreaCode(), client.getPhoneNum());
				clientsTableModel.addClientPerson(
						pn,
						client.getClientName(),
						client.getClientAddr(),
						client.getBirthDay());
				return;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Person registration error",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void addCompany() {
		client.prepareForAddCompany();
		while (client.showModal()) {
			try {
				PhoneNumber pn = new PhoneNumber(client.getAreaCode(), client.getPhoneNum());
				clientsTableModel.addClientCompany(
						pn,
						client.getClientName(),
						client.getClientAddr(),
						client.getDirectorName(),
						client.getContactName());
				return;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Company registration error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	private void changeClient() {
		int seldRow = clientsTable.getSelectedRow();
		if (seldRow == -1)
			return;

		ClientInfo ci = clientsTableModel.getClient(seldRow);

		if (ci instanceof Person) {
			client.prepareForChangePerson(ci);
			if (client.showModal()) {
				ci.setName(client.getClientName());
				ci.setAddress(client.getClientAddr());
				((Person) ci).setBirthDay(client.getBirthDay());
				clientsTableModel.clientChanged(seldRow);
			}
		}

		if (ci instanceof Company) {
			client.prepareForChangeCompany(ci);
			if (client.showModal()) {
				ci.setName(client.getClientName());
				ci.setAddress(client.getClientAddr());
				((Company) ci).setContactName(client.getContactName());
				((Company) ci).setDirectorName(client.getDirectorName());
				clientsTableModel.clientChanged(seldRow);
			}
		}
	}

	private void delClient() {
		int seldRow = clientsTable.getSelectedRow();
		if (seldRow == -1)
			return;

		ClientInfo ci = clientsTableModel.getClient(seldRow);
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete client\n"
						+ "with phone number " + ci.getPhoneNumber() + "?",
				"Delete confirmation",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			clientsTableModel.dropClient(seldRow);
		}
	}
}
