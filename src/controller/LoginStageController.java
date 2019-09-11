package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import entity.HouseHolder;
import entity.Manager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistence.HouseHolderDAO;
import persistence.ManagerDAO;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LoginStageController implements Initializable {

    @FXML
    private JFXTextField accountNumber;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXRadioButton radioBtnHolder;
    @FXML
    private JFXRadioButton radioBtnManager;
    @FXML
    private ImageView changeImgView;

    public LoginStageController(){

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        //右部分登录样式设置
        radioBtnHolder.setStyle("-jfx-selected-color:#4059a9");
        radioBtnManager.setStyle("-jfx-selected-color:#4059a9");


        //点击radioButton切换图片
        radioBtnManager.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(radioBtnManager.isSelected()){
                    changeImgView.setImage(new Image("images/loginImg2.jpg"));
                }

            }
        });
        radioBtnHolder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(radioBtnHolder.isSelected()){
                    changeImgView.setImage(new Image("images/loginImg3.jpg"));
                }
            }
        });

        //设置密码输入不超过6位
        password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String pass = password.getText();
                if(pass.length()>6){
                    password.setText(oldValue);
                }
            }
        });
        //设置管理员默认选中
        ToggleGroup group = new ToggleGroup();
        radioBtnHolder.setToggleGroup(group);
        radioBtnManager.setToggleGroup(group);
        radioBtnManager.setSelected(true);

    }

    //管理员登录验证
    public void checkManagerLogin() throws IOException {

        String textAccountNumber = accountNumber.getText();
        String textPassword = password.getText();
        //以下为Md5加密
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    textPassword.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("异常");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }

        ManagerDAO managerDAO = new ManagerDAO();
        /*bug：此处string转int，若原文不是int，如何处理？*/
        Manager manager = managerDAO.getManagerByAccountNumberAndPassword
                (Integer.parseInt(textAccountNumber),md5code);
        System.out.println(md5code);

        if(manager.getMail()== null){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in correct password and accountNumber.");
            alert.show();
        }
        else{
            //跳往ManagerStage页面

            Context.manager = manager;//将成功登录的管理员记录为全局
            Context.factory.createStage("ManagerStage",1100,700,"fxml/ManagerStage.fxml");
            Stage stage = Context.stageManager.getStage("ManagerStage");
            stage.getIcons().add(new Image("images/logo.png"));
            Context.stageManager.jump("LoginStage","ManagerStage");
        }

    }

    //住户登录验证
    public void checkHolderLogin() throws IOException {
        System.out.println("holderchecklogin");

        String textAccountNumber = accountNumber.getText();
        String textPassword = password.getText();
        HouseHolderDAO houseHolderDAO = new HouseHolderDAO();
        HouseHolder houseHolder = houseHolderDAO.
                getHolderByAccountNumberAndPassword(Integer.parseInt(textAccountNumber),textPassword);
        if(houseHolder.getHouseNumber()== null){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in correct password and accountNumber.");
            alert.show();
        }
        else{
            //跳往HouseHolderStage页面

            Context.houseHolder = houseHolder;//将成功登录的住户记录为全局
            Context.factory.createStage("HouseHolderStage",1100,700,"fxml/HouseHolderStage.fxml");
            Stage stage = Context.stageManager.getStage("HouseHolderStage");
            stage.getIcons().add(new Image("images/logo.png"));
            Context.stageManager.jump("LoginStage","HouseHolderStage");
        }
    }

    //判断是以哪种身份登录，调用不同的登录验证方法
    public void checkLogin(ActionEvent actionEvent) throws IOException {

        String textAccountNumber = accountNumber.getText();
        String textPassword = password.getText();

        if(textAccountNumber.isEmpty() || textPassword.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in your account number and password. ");
            alert.show();
        }
        else{
            if(radioBtnManager.isSelected()){
                checkManagerLogin();
            }
            else{
                checkHolderLogin();
            }
        }
    }
    //清空文本
    public void clearText(ActionEvent actionEvent) {

        accountNumber.clear();
        password.clear();

    }

    //跳往忘记密码窗口
    public void jumpToForget(ActionEvent actionEvent) throws IOException {

        if(radioBtnManager.isSelected()){
            Stage stage = Context.factory.createStage("ForgetPassStage",600,445,"fxml/ForgetPassStage.fxml");
            stage.getIcons().add(new Image("images/logo.png"));
            //设置为模态框
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        else{
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Sorry, you hava no right to change your password.\nPlease connect the manager. ");
            alert.show();
        }
    }
}
