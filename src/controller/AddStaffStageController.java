package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import persistence.StaffDAO;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddStaffStageController implements Initializable {

    @FXML
    private JFXTextField nameText;
    @FXML
    private JFXTextField telephoneText;
    @FXML
    private ComboBox<String> positionBox;
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private ImageView addStaffImg;

    public AddStaffStageController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.getItems().addAll("male","female");
        positionBox.getItems().addAll("Cleaner","Security","Repairman");
        addStaffImg.setImage(new Image("images/addHolder.jpg"));

        //设置手机号长度限制
        telephoneText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String pass = telephoneText.getText();
                //设置长度限制
                if(pass.length()>11){
                    telephoneText.setText(oldValue);
                }
            }
        });
    }

    //获得固定长度的随机字符串
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public void commitInsert(ActionEvent actionEvent) {
        String name = nameText.getText();
        String telephone = telephoneText.getText();
        String position = positionBox.getValue();
        String gender = genderBox.getValue();

        if(name.isEmpty()||position.isEmpty()||gender.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all information.");
            alert.show();
        }
        else{
            //db操作
            StaffDAO staffDAO = new StaffDAO();
            staffDAO.insertIntoStaff(name,position,gender,telephone);
            //将值传给主界面
            ManagerStageController.setText(getRandomString(4));
            Context.stageManager.closeStage("AddStaffStage");
        }

    }

    public void cancelInsert(ActionEvent actionEvent) {
        Context.stageManager.closeStage("AddStaffStage");
    }
}
