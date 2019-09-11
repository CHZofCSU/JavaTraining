package controller;

import com.jfoenix.controls.JFXTextField;
import entity.Manager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import persistence.StaffDAO;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class UpdateStaffStageController implements Initializable {

    @FXML
    private Label accountNumberLabel;
    @FXML
    private JFXTextField nameText;
    @FXML
    private JFXTextField telephoneText;
    @FXML
    private ImageView updateStaffImg;
    @FXML
    private ComboBox<String> positionBox;
    @FXML
    private ComboBox<String> genderBox;


    public UpdateStaffStageController(){

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionBox.getItems().addAll("Cleaner","Security","Repairman");
        genderBox.getItems().addAll("male","female");
        accountNumberLabel.setText(String.valueOf(Context.accountNumber));
        updateStaffImg.setImage(new Image("images/updateHolderImg.jpg"));

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


    public void commitUpdate(ActionEvent actionEvent) {

        String accountNumber = accountNumberLabel.getText();
        String newName = nameText.getText();
        String newPosition = positionBox.getValue();
        String gender = genderBox.getValue();
        String telephone = telephoneText.getText();

        if(accountNumber.isEmpty() || newName.isEmpty() || newPosition.isEmpty() || gender.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please complete all information. ");
            alert.show();
        }
        else{
            //db操作
            StaffDAO staffDAO = new StaffDAO();
            staffDAO.updateStaffByAccountNumber(Integer.valueOf(accountNumber),newName,newPosition,gender,telephone);
            //生成随机值传给主窗口，保证model的text值变化，使主窗口动态更新数据
            ManagerStageController.setText(getRandomString(4));
            //关闭本窗口
            Context.stageManager.closeStage("UpdateStaffStage");
        }
    }

    public void cancelUpdate(ActionEvent actionEvent) {
        Context.stageManager.closeStage("UpdateStaffStage");
    }
}
