package persistence;

import entity.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagerDAO {

    private static final String getManagerByAccountNumberAndPasswordString = "select * from manager where manager_id = ? and manager_password = ?";
    private static final String getManagerMailByAccountNumberString = "select manager_mail from manager where manager_id = ?";
    private static final String getManagerTelephoneByAccountNumberString = "select manager_telephone from manager where manager_id = ?";

    private static final String updateManagerPasswordByAccountNumberString = "update manager set manager_password = ? where manager_id = ?";
    private static final String getManagerByAccountNumberString =  "select * from manager where manager_id = ?";



    public Manager getManagerByAccountNumber(int accountNumber){
        Manager manager = new Manager();

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getManagerByAccountNumberString);
            pStatement.setInt(1,accountNumber);
            ResultSet resultSet = pStatement.executeQuery();
            if(resultSet.next())
            {
                manager.setId(resultSet.getInt(1));
                manager.setName(resultSet.getString(2));
                manager.setPassword(resultSet.getString(3));
                manager.setMail(resultSet.getString(4));
                manager.setImg(resultSet.getString(5));
                manager.setTelephone(resultSet.getString(6));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return manager;
    }


    //由账号和密码获得实体
    public Manager getManagerByAccountNumberAndPassword(int accountNumber, String password){
        Manager manager = new Manager();

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getManagerByAccountNumberAndPasswordString);
            pStatement.setInt(1,accountNumber);
            pStatement.setString(2,password);
            ResultSet resultSet = pStatement.executeQuery();
            if(resultSet.next())
            {
                manager.setId(resultSet.getInt(1));
                manager.setName(resultSet.getString(2));
                manager.setPassword(resultSet.getString(3));
                manager.setMail(resultSet.getString(4));
                manager.setImg(resultSet.getString(5));
                manager.setTelephone(resultSet.getString(6));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return manager;
    }
    //由账号获得邮箱
    public String getMailByAccountNumber(int accountNumber){

        String mail = "";

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getManagerMailByAccountNumberString);
            pStatement.setInt(1,accountNumber);
            ResultSet resultSet = pStatement.executeQuery();
            if(resultSet.next())
            {
                mail = resultSet.getString(1);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mail;
    }

    //由账号获得手机号
    public String getTelephoneByAccountNumber(int accountNumber){
        String telephone = "";
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getManagerTelephoneByAccountNumberString);
            pStatement.setInt(1,accountNumber);
            ResultSet resultSet = pStatement.executeQuery();
            if(resultSet.next())
            {
                telephone = resultSet.getString(1);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return telephone;
    }

    //修改账号密码
    public void updateManagerPasswordByAccountNumber(int accountNumber,String newPassword){

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(updateManagerPasswordByAccountNumberString);
            pStatement.setString(1,newPassword);
            pStatement.setInt(2,accountNumber);
            pStatement.executeUpdate();


            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
