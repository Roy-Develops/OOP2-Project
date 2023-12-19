import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class App extends Application {

    
        DBconnection DB=new DBconnection();
        int clientId;
        @Override
        public void start(Stage primaryStage) {
            BorderPane root = new BorderPane();
            //* home gridPane
            BorderPane adminHomeBorderPane = new BorderPane();
            
            MenuBar adminMenuBar = new MenuBar();
            Menu clientsMenu = new Menu("clients");
            MenuItem viewClientsMenuItem = new MenuItem("View clients");
            clientsMenu.getItems().addAll(viewClientsMenuItem);
            viewClientsMenuItem.setOnAction(e->{
                GridPane clientsGridPane = new GridPane();
                clientsGridPane.setPadding(new Insets(10));
                clientsGridPane.setHgap(10);
                clientsGridPane.setVgap(10);
                //* client table view
                TableView<Client> clientsTableView = new TableView<>();
                TableColumn<Client, Integer> idColumn = new TableColumn<>("ID");
                idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
                TableColumn<Client, String> firstNameColumn = new TableColumn<>("First Name");
                firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
                TableColumn<Client, String> lastNameColumn = new TableColumn<>("Last Name");
                lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
                TableColumn<Client, String> emailColumn = new TableColumn<>("Email");
                emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
                TableColumn<Client, String> passwordColumn = new TableColumn<>("Password");
                passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
                TableColumn<Client, String> phoneNumberColumn = new TableColumn<>("Phone Number");
                phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty().asString());
                clientsTableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, passwordColumn, phoneNumberColumn);
                DB.fillClientsTableView(clientsTableView);
                Label searchLabel = new Label("Search:");
                TextField searchField = new TextField("search client ...");
                searchField.setOnMouseClicked(e2->{
                    searchField.setText("");
                });
                Button search = new Button("search");
                search.setOnAction(e2->{
                    String searchValue = searchField.getText();
                    clientsTableView.setItems(DB.searchClient(searchValue));
                });
                clientsGridPane.add(searchLabel, 0, 0);
                clientsGridPane.add(searchField, 1, 0);
                clientsGridPane.add(search, 2, 0);

                clientsGridPane.add(clientsTableView, 3, 0);
                root.setCenter(clientsGridPane);
            });
            //* product menu
            Menu productMenu = new Menu("products");
            MenuItem addMenuItem = new MenuItem("Add product");
            
            MenuItem deleteMenuItem = new MenuItem("Delete product");
            productMenu.getItems().addAll(addMenuItem, deleteMenuItem);
            
            //* product table view
            TableView<Product> productsTableView = new TableView<>();

            //* Create columns for TableView
            TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
    
            TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    
            TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    
            productsTableView.getColumns().addAll(idColumn, nameColumn, priceColumn);

            productsTableView.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
                    GridPane updateProductGridPane = new GridPane();
                    updateProductGridPane.setPadding(new Insets(10));
                    updateProductGridPane.setHgap(10);
                    updateProductGridPane.setVgap(10);
                    Label nameLabel = new Label("Name:");
                    TextField nameField = new TextField();
                    nameField.setText(selectedProduct.nameProperty().get()); // Retrieve the value from the SimpleStringProperty
                    Label priceLabel = new Label("Price:");
                    TextField priceField = new TextField();
                    priceField.setText(selectedProduct.priceProperty().get() + ""); // Retrieve the value from the SimpleDoubleProperty
                    Button updateButton = new Button("Update");
                    updateButton.setOnAction(e2->{
                        String name = nameField.getText();
                        double price = Double.parseDouble(priceField.getText());
                        DB.updateProduct(selectedProduct.idProperty().get(), name, price);
                        DB.fillProductsTableView(productsTableView);
                    });
                    updateProductGridPane.add(nameLabel, 0, 0);
                    updateProductGridPane.add(nameField, 1, 0);
                    updateProductGridPane.add(priceLabel, 0, 1);
                    updateProductGridPane.add(priceField, 1, 1);
                    updateProductGridPane.add(updateButton, 1, 2);
                    GridPane.setColumnSpan(productsTableView, 5);
                    updateProductGridPane.add(productsTableView, 0, 4);
                    root.setCenter(updateProductGridPane);

                }
            });
            //* fill the table view
            DB.fillProductsTableView(productsTableView);
            adminHomeBorderPane.setCenter(productsTableView);
            //*  add product 
            addMenuItem.setOnAction(e->{
                GridPane addProductGridPane = new GridPane();
                addProductGridPane.setPadding(new Insets(10));
                addProductGridPane.setHgap(10);
                addProductGridPane.setVgap(10);
                Label nameLabel = new Label("Name:");
                TextField nameField = new TextField();
                Label priceLabel = new Label("Price:");
                TextField priceField = new TextField();
                Button addButton = new Button("Add");
                addButton.setOnAction(e2->{
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    DB.addProduct(name, price);
                    DB.fillProductsTableView(productsTableView);
                });
                addProductGridPane.add(nameLabel, 0, 0);
                addProductGridPane.add(nameField, 1, 0);
                addProductGridPane.add(priceLabel, 0, 1);
                addProductGridPane.add(priceField, 1, 1);
                addProductGridPane.add(addButton, 1, 2);
                GridPane.setColumnSpan(productsTableView, 5);
                addProductGridPane.add(productsTableView, 0, 4);
                root.setCenter(addProductGridPane);
            });
            //! delete product
            deleteMenuItem.setOnAction(e->{
                GridPane deleteProductGridPane = new GridPane();
                deleteProductGridPane.setPadding(new Insets(10));
                deleteProductGridPane.setHgap(10);
                deleteProductGridPane.setVgap(10);
                Label idLabel = new Label("ID:");
                TextField idField = new TextField();
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(e2->{
                    int id = Integer.parseInt(idField.getText());
                    DB.deleteProduct(id);
                    DB.fillProductsTableView(productsTableView);
                });
                deleteProductGridPane.add(idLabel, 0, 0);
                deleteProductGridPane.add(idField, 1, 0);
                deleteProductGridPane.add(deleteButton, 1, 2);
                GridPane.setColumnSpan(productsTableView, 5);
                deleteProductGridPane.add(productsTableView, 0, 4);
                root.setCenter(deleteProductGridPane);
            });
            //* Orders Menu
            Menu ordersMenu = new Menu("Orders");
            MenuItem viewOrdersMenuItem = new MenuItem("View Orders");
            ordersMenu.getItems().addAll(viewOrdersMenuItem);
            viewOrdersMenuItem.setOnAction(e->{
                GridPane ordersGridPane = new GridPane();
                ordersGridPane.setPadding(new Insets(10));
                ordersGridPane.setHgap(10);
                ordersGridPane.setVgap(10);
                //* orders table view
                TableView<Order> ordersTableView = new TableView<>();
                //* Create columns for TableView
                TableColumn<Order, Integer> idColumn2 = new TableColumn<>("ID");
                idColumn2.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
                TableColumn<Order, Integer> quantityColumn2 = new TableColumn<>("Quantity");
                quantityColumn2.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
                TableColumn<Order, String> product_nameColumn2 = new TableColumn<>("Product Name");
                product_nameColumn2.setCellValueFactory(cellData -> cellData.getValue().product_nameProperty());
                TableColumn<Order, Double> product_priceColumn2 = new TableColumn<>("Product Price");
                product_priceColumn2.setCellValueFactory(cellData -> cellData.getValue().product_priceProperty().asObject());
                TableColumn<Order, String> customer_first_nameColumn2 = new TableColumn<>("Customer First Name");
                customer_first_nameColumn2.setCellValueFactory(cellData -> cellData.getValue().customer_first_nameProperty());
                TableColumn<Order, String> customer_last_nameColumn2 = new TableColumn<>("Customer Last Name");
                customer_last_nameColumn2.setCellValueFactory(cellData -> cellData.getValue().customer_last_nameProperty());
                TableColumn<Order, String> customer_emailColumn2 = new TableColumn<>("Customer Email");
                customer_emailColumn2.setCellValueFactory(cellData -> cellData.getValue().customer_emailProperty());
                TableColumn<Order, String> customer_phoneColumn2 = new TableColumn<>("Customer Phone");
                customer_phoneColumn2.setCellValueFactory(cellData -> cellData.getValue().customer_phoneProperty());
                ordersTableView.getColumns().addAll(idColumn2, quantityColumn2, product_nameColumn2, product_priceColumn2, customer_first_nameColumn2, customer_last_nameColumn2, customer_emailColumn2, customer_phoneColumn2);
                ObservableList<Order> orders;
                orders = DB.getOrders();
                ordersTableView.setItems(orders);
                ordersGridPane.add(ordersTableView, 0, 0);
                root.setCenter(ordersGridPane);
            });
            adminMenuBar.getMenus().addAll(productMenu,clientsMenu,ordersMenu);

            //* SignIn gridPane
            GridPane SignIngridPane = new GridPane();
            SignIngridPane.setPadding(new Insets(10));
            SignIngridPane.setHgap(10);
            SignIngridPane.setVgap(10);

            Label usernameLabel = new Label("email:");
            TextField usernameField = new TextField();

            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            Button loginButton = new Button("Login");
            Button switchButton = new Button("Don't have an account? Sign up");
           
            //*  client menu bar
            MenuBar clientMenuBar = new MenuBar();
            Menu clientMenu = new Menu("products");
            MenuItem buyMenuItem = new MenuItem("Buy product");
            MenuItem viewBoughtProductsItem = new MenuItem("view bought products");
            Menu clientProfileMenu = new Menu("profile");
            MenuItem updateProfileMenuItem = new MenuItem("View profile");
            MenuItem logoutMenuItem = new MenuItem("Logout");
            MenuItem deleteProfileMenuItem = new MenuItem("delete profile");
            Menu animationMenu = new Menu("Animations");
            MenuItem animationMenuItem = new MenuItem("view");
            animationMenuItem.setOnAction(e->{
                FlowPane animationGridPane = new FlowPane();
                animationGridPane.setAlignment(Pos.CENTER);
                String paragraph =  """
                    Hello doctor,
                this was a great semester,
                I had many fun times with you,
                I learned a lot from you,
                I only want to say thank you.
                Merry Christmas "A7la docteur"!!
                """ ;
                String[] words = paragraph.split(" ");

                Text text = new Text();
                text.getStyleClass().add("christmas-text");
                Timeline timeline = new Timeline();
                Duration startDelay = Duration.millis(500); // Delay time before starting

             for (int i = 0; i < words.length; i++) {
                String word = words[i] + " ";
                KeyFrame keyFrame = new KeyFrame(startDelay.multiply(i + 1), e2 -> text.setText(text.getText() + word));
                timeline.getKeyFrames().add(keyFrame);
                
            }

            timeline.play();
            try {
                playSound("We Wish You a Merry Christmas with Lyrics  Christmas Carol & Song.wav");
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
                ImageView imageView = new ImageView("christmas.png");

                imageView.setOpacity(0);

            FadeTransition fade = new FadeTransition(Duration.seconds(3), imageView);
            fade.setFromValue(0);   // Start fully transparent
            fade.setToValue(1);
                timeline.setOnFinished(e2->{
                    
                fade.play();
                });

            animationGridPane.getChildren().addAll(text,imageView);

                root.setCenter(animationGridPane);
            });
            deleteProfileMenuItem.setOnAction(e->{
                DB.deleteClient(clientId);
                root.setCenter(SignIngridPane);
            });
            logoutMenuItem.setOnAction(e->{
                root.setCenter(SignIngridPane);
            });
            loginButton.setOnAction(e->{
                root.setCenter(SignIngridPane);
            });
            viewBoughtProductsItem.setOnAction(e->{
                GridPane boughtProductsGridPane = new GridPane();
                boughtProductsGridPane.setPadding(new Insets(10));
                boughtProductsGridPane.setHgap(10);
                boughtProductsGridPane.setVgap(10);
                //* bought products table view
                TableView<Product> boughtProductsTableView = new TableView<>();
                //* Create columns for TableView
                TableColumn<Product, String> nameColumn2 = new TableColumn<>("Name");
                nameColumn2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
                TableColumn<Product, Double> priceColumn2 = new TableColumn<>("Price");
                priceColumn2.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
                TableColumn<Product, Integer> quantityColumn2 = new TableColumn<>("Quantity");
                quantityColumn2.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
                boughtProductsTableView.getColumns().addAll(nameColumn2, priceColumn2,quantityColumn2);
                ObservableList<Product> boughtProducts;
                boughtProducts = DB.getBoughtProductDetails(clientId);
                boughtProductsTableView.setItems(boughtProducts);
                Label subtotLabel = new Label();
                subtotLabel.setText("Subtotal: $"+DB.getTotalPrice(clientId));
                boughtProductsGridPane.add(boughtProductsTableView, 0, 0);
                boughtProductsGridPane.add(subtotLabel, 2, 2);
                Button makeReceiptButton = new Button("make receipt");
                makeReceiptButton.setOnAction(e2->{
                    DB.makeReceipt(clientId);
                });
                boughtProductsGridPane.add(makeReceiptButton, 2, 3);
                root.setCenter(boughtProductsGridPane);
            });
            animationMenu.getItems().addAll(animationMenuItem);
            clientMenu.getItems().addAll(buyMenuItem,viewBoughtProductsItem);
            clientProfileMenu.getItems().addAll(updateProfileMenuItem, logoutMenuItem,deleteProfileMenuItem);
            clientMenuBar.getMenus().addAll(clientMenu, clientProfileMenu,animationMenu);
            loginButton.setOnAction(e -> {
                String email = usernameField.getText();
                String password = passwordField.getText();
                if (email.equals("admin") && password .equals("admin")) {
                    root.setTop(adminMenuBar);
                    root.setCenter(adminHomeBorderPane);
                }else{
                if(DB.validateClient(email, password)){
                    clientId=DB.getClientId(email, password);
                    root.setTop(clientMenuBar);
                    root.setCenter(null);

                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Wrong email or password");
                    alert.show();
                }

                }
            });
            
            buyMenuItem.setOnAction(e->{
                GridPane buyProductGridPane = new GridPane();
                root.setCenter(buyProductGridPane);
                buyProductGridPane.setPadding(new Insets(10));
                buyProductGridPane.setHgap(10);
                buyProductGridPane.setVgap(10);
                 
                
                try {
                   ObservableList<Product> products= DB.getProducts();
                   int row = 0;

                for (Product product : products) {
                    Label nameLabel = new Label(product.nameProperty().get());
                    Label priceLabel = new Label(product.priceProperty().get() + "");
                    Button buyButton = new Button("Buy");
                    Spinner<Integer> quantitySpinner = new Spinner<>();
                    quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1));
                    buyButton.setOnAction(e2->{
                        DB.buyProduct(clientId, product.idProperty().get(),quantitySpinner.getValue());
                    });
                    buyProductGridPane.add(nameLabel, 0, row);
                    buyProductGridPane.add(priceLabel, 1, row);
                    buyProductGridPane.add(quantitySpinner, 2, row);
                    buyProductGridPane.add(buyButton, 3, row);
                    row++;
                }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                
                
            });
            updateProfileMenuItem.setOnAction(e->{
                GridPane updateClientGridPane = new GridPane();
                updateClientGridPane.setPadding(new Insets(10));
                updateClientGridPane.setHgap(10);
                updateClientGridPane.setVgap(10);
                Label firstNameLabel = new Label("First Name:");
                TextField firstNameField = new TextField();
                firstNameField.setText(DB.getClientFirstName(clientId));
                Label lastNameLabel = new Label("Last Name:");
                TextField lastNameField = new TextField();
                lastNameField.setText(DB.getClientLastName(clientId));
                Label emailLabel = new Label("Email:");
                TextField emailField = new TextField();
                emailField.setText(DB.getClientEmail(clientId));
                Label passwordLabel2 = new Label("Password:");
                PasswordField passwordField2 = new PasswordField();
                passwordField2.setText(DB.getClientPassword(clientId));
                Label phoneNumberLabel = new Label("Phone Number:");
                TextField phoneNumberField = new TextField();
                phoneNumberField.setText(DB.getClientPhoneNumber(clientId));
                Button updateButton = new Button("Update");
                updateButton.setOnAction(e2->{
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String email = emailField.getText();
                    String password = passwordField2.getText();
                    String phoneNumber = phoneNumberField.getText();
                    DB.updateClient(clientId, firstName, lastName, email, password, phoneNumber);
                });
                updateClientGridPane.add(firstNameLabel, 0, 0);
                updateClientGridPane.add(firstNameField, 1, 0);
                updateClientGridPane.add(lastNameLabel, 0, 1);
                updateClientGridPane.add(lastNameField, 1, 1);
                updateClientGridPane.add(emailLabel, 0, 2);
                updateClientGridPane.add(emailField, 1, 2);
                updateClientGridPane.add(passwordLabel2, 0, 3);
                updateClientGridPane.add(passwordField2, 1, 3);
                updateClientGridPane.add(phoneNumberLabel, 0, 4);
                updateClientGridPane.add(phoneNumberField, 1, 4);
                updateClientGridPane.add(updateButton, 1, 5);
                root.setCenter(updateClientGridPane);
            });

            
            root.setCenter(SignIngridPane);            
            SignIngridPane.add(usernameLabel, 0, 0);
            SignIngridPane.add(usernameField, 1, 0);
            SignIngridPane.add(passwordLabel, 0, 1);
            SignIngridPane.add(passwordField, 1, 1);
            SignIngridPane.add(loginButton, 1, 2);
            SignIngridPane.add(switchButton, 3, 3);
            GridPane SignUpgridPane = new GridPane();
            SignUpgridPane.setPadding(new Insets(10));
            SignUpgridPane.setHgap(10);
            SignUpgridPane.setVgap(10);

            Label firstNameLabel = new Label("First Name:");
            TextField firstNameField = new TextField();

            Label lastNameLabel = new Label("Last Name:");
            TextField lastNameField = new TextField();

            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField();

            Label passwordLabel2 = new Label("Password:");
            PasswordField passwordField2 = new PasswordField();

            Label phoneNumberLabel = new Label("Phone Number:");
            TextField phoneNumberField = new TextField();

            Button signUpButton = new Button("Sign Up");
            Button signUpSwitchButton = new Button("Already have an account? Login in");
            signUpSwitchButton.setOnAction(e->{
                root.setCenter(SignIngridPane);
            });
             switchButton.setOnAction(e->{
                root.setCenter(SignUpgridPane);
            });
            
            signUpButton.setOnAction(e -> {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String password = passwordField2.getText();
                String phoneNumber = phoneNumberField.getText();
                if(DB.validateEmail(email)){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Email already exists");
                    alert.show();
                }
                else{
                    DB.addClient(firstName, lastName, email, password, phoneNumber);
                    clientId=DB.getClientId(email, password);
                    root.setTop(clientMenuBar);
                    root.setCenter(null);
                }

            });

            SignUpgridPane.add(firstNameLabel, 0, 0);
            SignUpgridPane.add(firstNameField, 1, 0);
            SignUpgridPane.add(lastNameLabel, 0, 1);
            SignUpgridPane.add(lastNameField, 1, 1);
            SignUpgridPane.add(emailLabel, 0, 2);
            SignUpgridPane.add(emailField, 1, 2);
            SignUpgridPane.add(passwordLabel2, 0, 3);
            SignUpgridPane.add(passwordField2, 1, 3);
            SignUpgridPane.add(phoneNumberLabel, 0, 4);
            SignUpgridPane.add(phoneNumberField, 1, 4);
            SignUpgridPane.add(signUpButton, 1, 5);
            SignUpgridPane.add(signUpSwitchButton, 3, 6);            
            
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add("./style.css");

            primaryStage.setTitle("Login Form");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    private void playSound(String fileName) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            File file = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
    }

   
}
