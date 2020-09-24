package app.db;

import app.common.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static final String DB_NAME = "contacts.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\My drive\\Coding\\Java\\ContactsApp - database\\app.db\\src\\app\\db\\" + DB_NAME;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FNAME = "firstname";
    public static final String COLUMN_LNAME = "lastname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_NOTES = "notes";
    public static final int INDEX_CONTACTS_ID = 1;
    public static final int INDEX_CONTACTS_FN = 2;
    public static final int INDEX_CONTACTS_LN = 3;
    public static final int INDEX_CONTACTS_PH = 4;
    public static final int INDEX_CONTACTS_NT = 5;


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " (" + COLUMN_ID + " integer primary key, " + COLUMN_FNAME + " text not null, " + COLUMN_LNAME + " text, " +
            COLUMN_PHONE + " text unique, " + COLUMN_NOTES + " text" + ")";
    public static final String CONTACTS_QUERY = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + COLUMN_FNAME;

    public static final String CONTACT_INSERT = "INSERT OR IGNORE INTO " + TABLE_CONTACTS +
            '(' + COLUMN_FNAME + ", " + COLUMN_LNAME + ", " + COLUMN_PHONE + ", " + COLUMN_NOTES + ") VALUES(?, ?, ?, ?)";

    public static final String CONTACT_UPDATE = "UPDATE " + TABLE_CONTACTS + " SET " + COLUMN_FNAME + " = ?, " + COLUMN_LNAME + " = ?, " + COLUMN_PHONE + " = ?, " + COLUMN_NOTES + " = ?" + " WHERE " + COLUMN_ID + " = ?";

    public static final String CONTACT_DELETE = "DELETE FROM " + TABLE_CONTACTS + " WHERE _id = ?";

    private Connection connection;
    //creating singleton
    private static DataSource dataSource = null;

    private DataSource() {

    }

    public static synchronized DataSource getInstance() {
        if (dataSource == null) {
            dataSource = new DataSource();
        }
        return dataSource;
    }

    private PreparedStatement createTable;
    private PreparedStatement contactsQuery;
    private PreparedStatement contactInsert;
    private PreparedStatement contactUpdate;
    private PreparedStatement contactDelete;

    public boolean openConnection() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            createTable = connection.prepareStatement(CREATE_TABLE);
            contactsQuery = connection.prepareStatement(CONTACTS_QUERY);
            contactInsert = connection.prepareStatement(CONTACT_INSERT);
            contactUpdate = connection.prepareStatement(CONTACT_UPDATE);
            contactDelete = connection.prepareStatement(CONTACT_DELETE);
//            artistsInsert = connection.prepareStatement(ARTIST_INSERT, Statement.RETURN_GENERATED_KEYS); //getting key of prepared statement object

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (createTable != null) {
                createTable.close();
            }
            if (contactsQuery != null) {
                contactsQuery.close();
            }
            if (contactInsert != null) {
                contactInsert.close();
            }
            if (contactUpdate != null) {
                contactUpdate.close();
            }
            if (contactDelete != null) {
                contactDelete.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Contact> contactsQueryMethod() {
        List<Contact> contactList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = contactsQuery.executeQuery();
//            statement.executeUpdate("INSERT OR IGNORE INTO contacts (firstname, lastname, phone, notes) VALUES (\"Samis\", \"Samirkov3ic\", \"12322\", \"lll3.@gmail.com\")");
            try{
                System.out.println("Query sleeping");
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.getMessage();
            }

            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setID(resultSet.getInt(INDEX_CONTACTS_ID));
                contact.setFirstname(resultSet.getString(INDEX_CONTACTS_FN));
                contact.setLastname(resultSet.getString(INDEX_CONTACTS_LN));
                contact.setPhone(resultSet.getString(INDEX_CONTACTS_PH));
                contact.setNote(resultSet.getString(INDEX_CONTACTS_NT));
                contactList.add(contact);
            }
            return contactList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean contactInsertMethod(String fname, String lname, String phone, String notes) {
        try {
            try{
                System.out.println("Insert sleeping");
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.getMessage();
            }
            //starting transaction
            connection.setAutoCommit(false);

            contactInsert.setString(1, fname);
            contactInsert.setString(2, lname);
            contactInsert.setString(3, phone);
            contactInsert.setString(4, notes);

            int affectedRows = contactInsert.executeUpdate();
            if (affectedRows == 1) {
                connection.commit();
                System.out.println("Committing");
                return true;
            } else {
                throw new SQLException("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("Preforming rollback");
                connection.rollback();
                return false;
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                System.out.println("Resetting commit");
                connection.setAutoCommit(true);

                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public Boolean contactUpdate(int id, String fname, String lname, String phone, String notes){
        try{
            try{
                System.out.println("Update sleeping");
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.getMessage();
            }
            contactUpdate.setString(1, fname);
            contactUpdate.setString(2, lname);
            contactUpdate.setString(3, phone);
            contactUpdate.setString(4, notes);
            contactUpdate.setInt(5, id);

            int affectedRecords = contactUpdate.executeUpdate();
            return affectedRecords == 1;
        } catch (SQLException e){
            e.getMessage();
            return false;
        }
    }

    public Boolean deleteContact(int id){
        try {
            try{
                System.out.println("Delete sleeping");
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.getMessage();
            }
            contactDelete.setInt(1, id);
            int affectedRecords = contactDelete.executeUpdate();
            return affectedRecords == 1;

        } catch (SQLException e){
            e.getMessage();
            return false;
        }
    }

}