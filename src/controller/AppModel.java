package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppModel {

    private IntegerProperty accountNumber = new SimpleIntegerProperty();
    private StringProperty text = new SimpleStringProperty();

    public AppModel(){
        this.text = new SimpleStringProperty();
        this.accountNumber = new SimpleIntegerProperty();
    }

    public final int getAccountNumber() {
        return accountNumber.get();
    }

    public IntegerProperty accountNumberProperty() {
        return accountNumber;
    }

    public final void setAccountNumber(int accountNumber) {
        this.accountNumber.set(accountNumber);
    }

    public final String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public final void setText(String text) {
        this.text.set(text);
    }
}
