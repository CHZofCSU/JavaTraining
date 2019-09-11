package controller;

import com.jfoenix.controls.JFXTextField;
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
import persistence.HouseHolderDAO;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class UpdateHolderStageController implements Initializable {

    public static AppModel model = new AppModel();

    @FXML
    private Label accountNumberLabel;
    @FXML
    private JFXTextField passwordText;
    @FXML
    private JFXTextField nameText;
    @FXML
    private JFXTextField houseNumberText;
    @FXML
    private JFXTextField houseSquareText;
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private ImageView updateHolderImg;

    public UpdateHolderStageController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accountNumberLabel.setText(String.valueOf(Context.accountNumber));
        genderBox.getItems().addAll("male","female");
        updateHolderImg.setImage(new Image("images/updateHolderImg.jpg"));

        //设置密码长度限制
        passwordText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String pass = passwordText.getText();
                //设置长度限制
                if(pass.length()>6){
                    passwordText.setText(oldValue);
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


    //按钮事件
    public void cancelUpdate(ActionEvent actionEvent) {
        Context.stageManager.closeStage("UpdateHolderStage");

    }

    public void commitUpdate(ActionEvent actionEvent) {

        String accountNumber = accountNumberLabel.getText();
        String newPass = passwordText.getText();
        String newName = nameText.getText();
        String newHouseNumber = houseNumberText.getText();
        String newHouseSquare = houseSquareText.getText();
        String gender = genderBox.getValue();
        String img = "Ben.png";
        if( accountNumber.isEmpty() || newPass.isEmpty() || newName.isEmpty()
                || newHouseNumber.isEmpty() || newHouseSquare.isEmpty() ){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all information.");
            alert.show();
        }
        else {
            //db操作
            HouseHolderDAO houseHolderDAO = new HouseHolderDAO();
            houseHolderDAO.updateHouseHolderByAccountNumber(Integer.valueOf(accountNumber), newName, newPass,
                    newHouseNumber, Double.valueOf(newHouseSquare), gender,img);
            //生成随机值传给主窗口,保证model的text值变化，使主窗口动态更新数据
            ManagerStageController.setText(getRandomString(4));
            //关闭窗口
            Context.stageManager.closeStage("UpdateHolderStage");
        }
    }
}
