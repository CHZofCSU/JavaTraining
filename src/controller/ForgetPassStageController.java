package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import persistence.ManagerDAO;
import tool.Mail;
import tool.SMS;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgetPassStageController implements Initializable {


    private String verificationCode;
    @FXML
    private JFXTextField accountNumber;
    @FXML
    private JFXPasswordField password1;
    @FXML
    private JFXPasswordField password2;
    @FXML
    private JFXTextField code;
    @FXML
    private ImageView rightImg1;
    @FXML
    private ImageView rightImg2;



    public ForgetPassStageController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //设置表单的字体颜色为白色
        accountNumber.setStyle("-fx-text-inner-color: #ffffff");
        password1.setStyle("-fx-text-inner-color: #ffffff");
        password2.setStyle("-fx-text-inner-color: #ffffff");
        code.setStyle("-fx-text-inner-color: #ffffff");

        password1.setPrefColumnCount(6);


        //设置两次密码输入校验
        password1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String pass1 =password1.getText();
                //设置长度限制
                if(pass1.length()>6){
                    password1.setText(oldValue);
                }
            }
        });
        password2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String pass1 =password1.getText();
                String pass2 =password2.getText();
                //设置长度限制
                if(pass2.length()>6){
                    password2.setText(oldValue);
                }
                //相同时显示打钩图片
                if(pass1.equals(pass2)){
                    rightImg1.setImage(new Image("images/correct.png"));
                    rightImg2.setImage(new Image("images/correct.png"));
                }

            }
        });
    }

    //获得固定长度的字符串充当验证码
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

    //取消修改密码，退回登录窗口
    public void cancelChangePass(ActionEvent actionEvent) {
        Context.stageManager.closeStage("ForgetPassStage");
    }

    //按下获得验证码按钮
    public void getCode(ActionEvent actionEvent) throws Exception {

        String textAccountNumber = accountNumber.getText();

        //若无账号则弹出提示框
        if(textAccountNumber.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in your account number first. ");
            alert.show();
        }
        else{
            ManagerDAO managerDAO = new ManagerDAO();
            String mail = managerDAO.getMailByAccountNumber(Integer.parseInt(textAccountNumber));
            String telephone = managerDAO.getTelephoneByAccountNumber(Integer.parseInt(textAccountNumber));

            if(mail=="" && telephone== ""){
                Alert alert  = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Please fill in a correct accountNumber. ");
                alert.show();
            }
            else{
                //发送邮件去往邮箱
                Mail realMail = new Mail();
                verificationCode = getRandomString(5);
                System.out.println(verificationCode);
                String content = "Your verification code is " + verificationCode;
                realMail.sendMailToManager(mail,content);
                //发短信给手机
                SMS sms = new SMS();
                String result = sms.sendMsg(verificationCode,telephone);
                //打印返回码看是否发送成功
                System.out.println(result);
            }
        }
    }

    //检查信息填写正确与否后修改密码
    public void checkCommit(ActionEvent actionEvent) throws Exception {

        String textAccountNumber = accountNumber.getText();
        String pass1 = password1.getText();
        String pass2 = password2.getText();
        String v_code = code.getText();
        String mail ="";
        ManagerDAO managerDAO = new ManagerDAO();
        if(!textAccountNumber.isEmpty()){
             mail = managerDAO.getMailByAccountNumber(Integer.parseInt(textAccountNumber));
        }


        //信息填写不全
        if( textAccountNumber.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || v_code.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please complete the information. ");
            alert.show();
        }
        //账号错误
        else if( mail=="" ){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in a correct accountNumber. ");
            alert.show();
        }
        //两次密码输入不一致
        else if( !pass1.equals(pass2) ){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Inconsistent password.Please fill in again. ");
            alert.show();
        }
        //验证码输入错误
        else if( !v_code.equals(verificationCode) ){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in the correct verification code. ");
            alert.show();
        }
        else{
            //以下为Md5加密
            byte[] secretBytes = null;
            try {
                secretBytes = MessageDigest.getInstance("md5").digest(
                        pass1.getBytes());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("没有这个md5算法！");
            }
            String md5code = new BigInteger(1, secretBytes).toString(16);
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code = "0" + md5code;
            }
            //修改db中对应的密码
            managerDAO.updateManagerPasswordByAccountNumber(Integer.valueOf(textAccountNumber),md5code);
            Alert alert  = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Your password has been changed successfully.");
            alert.show();
            //关闭忘记密码窗口
            Context.stageManager.closeStage("ForgetPassStage");
        }
    }
}
