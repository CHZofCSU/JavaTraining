package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import persistence.HouseHolderDAO;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddHolderStageController implements Initializable {

    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private JFXTextField nameText;
    @FXML
    private JFXTextField passwordText;
    @FXML
    private JFXTextField houseNumberText;
    @FXML
    private JFXTextField houseSquareText;

    @FXML
    private ImageView addHolderImg;


    public AddHolderStageController(){

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.getItems().addAll("male","female");
        addHolderImg.setImage(new Image("images/addHolder.jpg"));
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

    public void commitInsert(ActionEvent actionEvent) {
        String name = nameText.getText();
        String password = passwordText.getText();
        String houseNumber = houseNumberText.getText();
        String houseSquare = houseSquareText.getText();
        String gender = genderBox.getValue();

        //逻辑判断
        if(name.isEmpty() || password.isEmpty() || houseNumber.isEmpty()
                || houseSquare.isEmpty() || gender.isEmpty() ){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all information.");
            alert.show();
        }
        else{
            HouseHolderDAO houseHolderDAO = new HouseHolderDAO();
            houseHolderDAO.insertIntoHouseHolder(name,password,houseNumber,Double.valueOf(houseSquare),gender);
            //生成随机值传给主窗口,保证model的text值变化，使主窗口动态更新数据
            ManagerStageController.setText(getRandomString(4));
            //关闭窗口
            Context.stageManager.closeStage("AddHolderStage");
        }
    }

    public void cancelInsert(ActionEvent actionEvent) {
        //关闭窗口
        Context.stageManager.closeStage("AddHolderStage");
    }
}
