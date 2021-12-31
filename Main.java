package ru.avalon.javapp.devj120.avalontelecom;

import ru.avalon.javapp.devj120.avalontelecom.lists.ClientList;
import ru.avalon.javapp.devj120.avalontelecom.ui.MainFrame;

import java.io.IOException;

/**
 * Application entry point, which only purpose is to create and show application main window.
 */
public class Main {
	/**
	 * Application entry point; creates and shows application main window.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		try{
		ClientList.init();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("The file 'clients.dat' is unreadable");
			System.exit(0);
		}
		new MainFrame().setVisible(true);
	}
}
