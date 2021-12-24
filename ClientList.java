package ru.avalon.javapp.devj120.avalontelecom.lists;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.avalon.javapp.devj120.avalontelecom.models.ClientInfo;
import ru.avalon.javapp.devj120.avalontelecom.models.Company;
import ru.avalon.javapp.devj120.avalontelecom.models.Person;
import ru.avalon.javapp.devj120.avalontelecom.models.PhoneNumber;

/**
 * Manages list of clients.
 */
public class ClientList {
    /**
     * The only instance of this class.
     *
     * @see #getInstance
     */
    private static ClientList instance;

    static {
        try {
            instance = new ClientList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String FILE_NAME = "clients.dat";

    /**
     * Keeps the list of of clients. Clients are stored in the same order,
     * which they have been registered in.
     */
    private List<ClientInfo> clients;

    /**
     * List of client phone numbers. The list is used by  method to check,
     * that specified phone number is not used.
     * This set is updated simultaneously with {@link #clients} list, when
     * clients are  or {@linkplain #remove removed}.
     */
    private Set <PhoneNumber> numbers;

    /**
     * Prevents instance creation out of the class.
     */
    private ClientList() throws IOException, ClassNotFoundException {
        File f = new File(FILE_NAME);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                clients = (List<ClientInfo>) ois.readObject();
                numbers = new HashSet<>();
                for (ClientInfo b : clients) {
                    numbers.add(b.getPhoneNumber());
                }
            } catch (IOException exception){
                System.out.println("The file 'clients.dat' is unreadable");
                System.exit(0);
            }
        } else {
            clients = new ArrayList<>();
            numbers = new HashSet<>();
        }
    }

    /**
     * Adds client with specified attributes. Creates instance of {@code ClientInfo}
     * (see {@link ClientInfo#ClientInfo(PhoneNumber, String, String)}) while adding new client.
     *
     * @param number client phone number
     * @param name client name
     * @param address client address
     *
     * @exception IllegalArgumentException If some client with specified phone number
     * 		has already been registered.
     */
    public void addPerson(PhoneNumber number, String name, String address, String birth) {
        if(numbers.contains(number))
            throw new IllegalArgumentException("Such a number has already been registered earlier.");
        clients.add(new Person(number, name, address, birth));
        numbers.add(number);
    }

    public void addCompany(PhoneNumber number, String name, String address, String directorName, String contactName) {
        if(numbers.contains(number))
            throw new IllegalArgumentException("Such a number has already been registered earlier.");
        clients.add(new Company(number, name, address, directorName, contactName));
        numbers.add(number);
    }



    /**
     * Removes client with the specified index.
     *
     * @param index index of a client to be removed
     *
     * @exception IndexOutOfBoundsException
     * 		If the index is out of range (index < 0 || index >= {@link #getClientsCount}).
     */
    public void remove(int index) {
        ClientInfo clientInfo = clients.get(index);
        numbers.remove(clientInfo.getPhoneNumber());
        clients.remove(index);
    }

    /**
     * Returns amount of clients, kept by the list.
     *
     * @return Number of clients, kept by the list.
     */
    public int getClientsCount() {
        return clients.size();
    }

    /**
     * Returns information about a client with specified index.
     *
     * @param index client index, which data is retrieved
     *
     * @return {@code ClientInfo}
     *
     * @exception IndexOutOfBoundsException
     * 		If the index is out of range (index < 0 || index >= {@link #getClientsCount}).
     */
    public ClientInfo getClientInfo(int index) {
        return clients.get(index);
    }

    /**
     * Returns the only instance of this class.
     */
    public static ClientList getInstance() {
        return instance;
    }

    public void save() throws  IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(clients);
        }
    }
}