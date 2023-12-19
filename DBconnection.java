import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.TableView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBconnection {

    private Connection conn ;
    public void connect(){
		
        try {
 
            String dbURL = "jdbc:mysql://localhost:3306/project_oop";
            String user = "root";
            String pass = "AsdfQwerty123!@#";
            conn = DriverManager.getConnection(dbURL,user,pass);
            if (conn != null) {
                System.out.println("Connection succeed");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Disconnect from the database
    public void disconnect() {
        // Close the database connection
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }
    
    // Validate email and password from the "clients" table
    public boolean validateClient(String email, String password) {
        connect();
        try {
            String query = "SELECT * FROM clients WHERE email = ? AND pass = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
                              
            if (resultSet.next()) {
                System.out.println("Logged in successfully");
                disconnect();
                return true;
            } else {
                System.out.println("Wrong username or password");
                disconnect();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
            return false;
        }
    }
    
    // Add a client to the "clients" table
    public void addClient(String firstName, String lastName, String email, String password, String phoneNumber) {
        connect();
        try {
            String query = "INSERT INTO clients (first_name, last_name, email, pass, phone_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, phoneNumber);
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Client added successfully");
            } else {
                System.out.println("Failed to add client");
            }
            
            disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
        }
    }

        public void fillProductsTableView(TableView<Product> tableView) {
            ObservableList<Product> productList = FXCollections.observableArrayList();
            connect();
            try {   
                String query = "SELECT * FROM products";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("product_name");
                    double price = resultSet.getDouble("price");

                    Product product = new Product(id, name, price);
                    productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableView.setItems(productList);
            disconnect();
        }

        public void addProduct(String name, double price) {
            connect();
        try {
            String query = "INSERT INTO products (product_name, price) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setLong(2, (long) price);

            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("product added successfully");
            } else {
                System.out.println("Failed to add product");
            }
            
            disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
        }
        }

        public void deleteProduct(int id) {
            connect();
        try {
            String query = "DELETE FROM products WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("product deleted successfully");
            } else {
                System.out.println("Failed to delete product");
            }
            
            disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
        }
    }

        public void updateProduct(int i, String name, double price) {
            connect();
        try {
            String query = "UPDATE products SET product_name = ?, price = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, i);

            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("product updated successfully");
            } else {
                System.out.println("Failed to update product");
            }
            
            disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
        }
    }

        public int getClientId(String email, String password) {
            connect();
            int id = 0;
            try {
                String query = "SELECT id FROM clients WHERE email = ? AND pass = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                                  
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    disconnect();
                    return id;
                } else {
                    System.out.println("Wrong username or password");
                    disconnect();
                    return id;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
                return id;
            }
            
        }

        public String getClientFirstName(int clientId) {
            connect();
            String firstName = null;
            try {
                String query = "SELECT first_name FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                                          
                if (resultSet.next()) {
                    firstName = resultSet.getString("first_name");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            
            return firstName;
        }

        public String getClientLastName(int clientId) {
            connect();
            String lastName = null;
            try {
                String query = "SELECT last_name FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                                          
                if (resultSet.next()) {
                    lastName = resultSet.getString("last_name");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            
            return lastName;
            
        }

        public String getClientEmail(int clientId) {
            connect();
            String email = null;
            try {
                String query = "SELECT email FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                                          
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            
            return email;
        }

        public String getClientPassword(int clientId) {
            connect();
            String password = null;
            try {
                String query = "SELECT pass FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                                          
                if (resultSet.next()) {
                    password = resultSet.getString("pass");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            
            return password;
        }

        public String getClientPhoneNumber(int clientId) {
            connect();
            String phoneNumber = null;
            try {
                String query = "SELECT phone_number FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                                          
                if (resultSet.next()) {
                    phoneNumber = resultSet.getString("phone_number");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            
            return phoneNumber;
        }

        public void updateClient(int clientId, String firstName, String lastName, String email, String password,
                String phoneNumber) {
            connect();
            try {
                String query = "UPDATE clients SET first_name = ?, last_name = ?, email = ?, pass = ?, phone_number = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, password);
                statement.setString(5, phoneNumber);
                statement.setInt(6, clientId);
    
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Client updated successfully");
                } else {
                    System.out.println("Failed to update client");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
        }
        public void deleteOrder(int client_id){
            connect();
            try {
                String query = "DELETE FROM orders WHERE client_id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, client_id);
    
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Order deleted successfully");
                } else {
                    System.out.println("Failed to delete order");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
        }
        public void deleteClient(int id){
            deleteOrder(id);
            connect();
            try {
                String query = "DELETE FROM clients WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, id);
    
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Client deleted successfully");
                } else {
                    System.out.println("Failed to delete client");
                }
                
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
        }
        public ObservableList<Product> getProducts() throws SQLException {
            ObservableList<Product> productList = FXCollections.observableArrayList();
            connect();
            try {   
                String query = "SELECT * FROM products";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("product_name");
                    double price = resultSet.getDouble("price");

                    Product product = new Product(id, name, price);
                    productList.add(product);
                }
                return productList;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void buyProduct(int clientId, int productId,int quantity) {
            connect();
            try{
                String query = "SELECT * from orders where client_id =? and product_id=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, clientId);
                statement.setInt(2, productId);
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    int q = resultSet.getInt("quantity");
                    q+=quantity;
                    query = "UPDATE orders SET quantity = ? WHERE client_id = ? and product_id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, q);
                    statement.setInt(2, clientId);
                    statement.setInt(3, productId);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Order updated successfully");
                    } else {
                        System.out.println("Failed to update order");
                    }
                }
                else{
                    query = "INSERT INTO orders (client_id, product_id,quantity) VALUES (?, ?,?)";
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, clientId);
                    statement.setInt(2, productId);
                    statement.setInt(3, quantity);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Order added successfully");
                    } else {
                        System.out.println("Failed to add order");
                    }
                }
                disconnect();
            }catch (SQLException e) {
                e.printStackTrace();
            }
            
        }

        public void fillClientsTableView(TableView<Client> clientsTableView) {
            ObservableList<Client> clientList = FXCollections.observableArrayList();
            connect();
            try {   
                String query = "SELECT * FROM clients";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("pass");
                    int phoneNumber = resultSet.getInt("phone_number");

                    Client client = new Client(id, firstName, lastName, email, password, phoneNumber);
                    clientList.add(client);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            clientsTableView.setItems(clientList);
            disconnect();
        }   
        public ObservableList<Integer> boughtProduct(int clientId) {
            ObservableList<Integer> productIds = FXCollections.observableArrayList();
            connect();
            String sql = "SELECT product_id FROM orders WHERE client_id = ?";
    
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
    
                while (resultSet.next()) {
                    productIds.add(resultSet.getInt("product_id"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            disconnect();
            return productIds;
        }
        public ObservableList<Product> getBoughtProductDetails(int clientId) {
            connect();
            ObservableList<Product> products = FXCollections.observableArrayList();
                String sql = "SELECT product_name, price, quantity FROM products,orders WHERE products.id = orders.product_id and orders.client_id=? ";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, clientId);
                    ResultSet resultSet = statement.executeQuery();
                    while(resultSet.next()) {
                        Product product = new Product(resultSet.getString("product_name"), resultSet.getDouble("price"), resultSet.getInt("quantity"));
                        products.add(product);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            disconnect();
            return products;
        }
        public double getTotalPrice(int clientId) {
        connect();
        double totalPrice = 0;
        String sql = "SELECT SUM(p.price * o.quantity) AS total_price FROM orders o,products p where o.product_id = p.id and o.client_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalPrice = resultSet.getDouble("total_price");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        disconnect();
        return totalPrice;
    }

         public void makeReceipt(int clientId) {
            String firstName = getClientFirstName(clientId);
            String lastName = getClientLastName(clientId);
        try (FileWriter writer = new FileWriter(firstName+"_"+lastName+"_"+"receipt.txt")) {
                writer.write("First Name: " + firstName + "\n");
                writer.write("Last Name: " + lastName + "\n");
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                writer.write("Date and Time: " + formattedDateTime + "\n\n");
                writer.write("Total Price: " + getTotalPrice(clientId) + "\n");
                writer.write("Products: \n");
                writer.write("Name\t");
                writer.write("Price\t");
                writer.write("Quantity\n");

                for (Product product : getBoughtProductDetails(clientId)) {
                    writer.write(product.nameProperty().get() + "\t" +
                                 product.priceProperty().get() + "\t" +
                                 product.quantityProperty().get() + "\t"+
                                 "\n");
                }
                double tax = getTotalPrice(clientId) * 0.11;
                double totalPriceWithTax = getTotalPrice(clientId) * 1.11;
                writer.write("Tax: " + String.format("%.2f", tax));
                writer.write("Total Price with Tax: " + String.format("%.2f", totalPriceWithTax));
            
        }  catch (IOException e) {
            e.printStackTrace();
        } 
    }
        public boolean validateEmail(String email){
            connect();
            try {
                String query = "SELECT * FROM clients WHERE email = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                                  
                if (resultSet.next()) {
                    System.out.println("Email already exists");
                    disconnect();                    
                    return true;
                } else {
                    System.out.println("Email is available");
                    disconnect();
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                disconnect();
            }
            return false;
        }
        public ObservableList<Order>getOrders(){
            ObservableList<Order> orderList = FXCollections.observableArrayList();
            connect();
            try {   
                String query = "SELECT orders.id,orders.quantity,products.product_name,products.price,clients.first_name,clients.last_name,clients.email,clients.phone_number FROM orders,products,clients where orders.product_id = products.id and orders.client_id = clients.id";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int quantity = resultSet.getInt("quantity");
                    String product_name = resultSet.getString("product_name");
                    double product_price = resultSet.getDouble("price");
                    String customer_first_name = resultSet.getString("first_name");
                    String customer_last_name = resultSet.getString("last_name");
                    String customer_email = resultSet.getString("email");
                    String customer_phone = resultSet.getString("phone_number");

                    Order order = new Order(id, quantity, product_name, product_price, customer_first_name, customer_last_name, customer_email, customer_phone);
                    orderList.add(order);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            disconnect();
            return orderList;
        }
        public void adminDeleteOrder(int id) {
            connect();
            try {
                String query = "DELETE FROM orders WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Order deleted successfully");
                } else {
                    System.out.println("Failed to delete order");
                }

                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Error deleting order: " + ex.getMessage());
                disconnect();
            }
        }
        // search a client by his first name or last name
        public ObservableList<Client> searchClient(String name) {
            ObservableList<Client> clientList = FXCollections.observableArrayList();
            connect();
            try {   
                String query = "SELECT * FROM clients WHERE first_name LIKE ? OR last_name LIKE ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, "%" + name + "%");
                statement.setString(2, "%" + name + "%");
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("pass");
                    int phoneNumber = resultSet.getInt("phone_number");

                    Client client = new Client(id, firstName, lastName, email, password, phoneNumber);
                    clientList.add(client);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            disconnect();
            return clientList;
        }
        

  
}
    



