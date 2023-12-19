import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleIntegerProperty id;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty password;
    private SimpleIntegerProperty phoneNumber;
    public Client(int id, String firstName, String lastName, String email, String password, int phonNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phoneNumber = new SimpleIntegerProperty(phonNumber);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public SimpleStringProperty firstNameProperty(){
        return firstName;
    }
    public SimpleStringProperty lastNameProperty(){
        return lastName;
    }
    public SimpleStringProperty emailProperty(){
        return email;
    }
    public SimpleStringProperty passwordProperty(){
        return password;
    }
    public SimpleIntegerProperty phoneNumberProperty(){
        return phoneNumber;
    }




}
