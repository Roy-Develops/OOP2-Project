import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public  class Product {

        private  SimpleIntegerProperty id;
        private  SimpleStringProperty name;
        private  SimpleDoubleProperty price;
        private  SimpleIntegerProperty quantity;

        public Product(int id, String name, double price) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
        }
        public Product(String name, double price,int quantity) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
            this.quantity = new SimpleIntegerProperty(quantity);
        }


        
          public SimpleIntegerProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }
        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }
    }