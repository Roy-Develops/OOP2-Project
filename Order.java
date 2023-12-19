import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    
            private  SimpleIntegerProperty id;
            private  SimpleIntegerProperty quantity;
            private  SimpleIntegerProperty product_id;
            private  SimpleIntegerProperty customer_id;
            private SimpleStringProperty product_name;
            private SimpleDoubleProperty product_price;
            private SimpleStringProperty customer_first_name;
            private SimpleStringProperty customer_last_name;
            private SimpleStringProperty customer_email;
            private SimpleStringProperty customer_phone;

            public Order(int id, int quantity, int product_id, int customer_id) {
                this.id = new SimpleIntegerProperty(id);
                this.quantity = new SimpleIntegerProperty(quantity);
                this.product_id = new SimpleIntegerProperty(product_id);
                this.customer_id = new SimpleIntegerProperty(customer_id);
            }
            public Order(int id, int quantity, String product_name, double product_price, String customer_first_name, String customer_last_name, String customer_email, String customer_phone) {
                this.id = new SimpleIntegerProperty(id);
                this.quantity = new SimpleIntegerProperty(quantity);
                this.product_name = new SimpleStringProperty(product_name);
                this.product_price = new SimpleDoubleProperty(product_price);
                this.customer_first_name = new SimpleStringProperty(customer_first_name);
                this.customer_last_name = new SimpleStringProperty(customer_last_name);
                this.customer_email = new SimpleStringProperty(customer_email);
                this.customer_phone = new SimpleStringProperty(customer_phone);
            }
            //Getters and Setters for all the properties of an order object
            public SimpleIntegerProperty idProperty() {
                return id;
            }
            public SimpleIntegerProperty quantityProperty() {
                return quantity;
            }
            public SimpleIntegerProperty product_idProperty() {
                return product_id;
            }
            public SimpleIntegerProperty customer_idProperty() {
                return customer_id;
            }
            public SimpleStringProperty product_nameProperty() {
                return product_name;
            }
            public SimpleDoubleProperty product_priceProperty() {
                return product_price;
            }
            public SimpleStringProperty customer_first_nameProperty() {
                return customer_first_name;
            }
            public SimpleStringProperty customer_last_nameProperty() {
                return customer_last_name;
            }
            public SimpleStringProperty customer_emailProperty() {
                return customer_email;
            }
            public SimpleStringProperty customer_phoneProperty() {
                return customer_phone;
            }
}
