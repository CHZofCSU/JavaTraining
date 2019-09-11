package persistence;

import entity.HouseHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HouseHolderDAO {

    private static final String getHolderByAccountNumberAndPasswordString = "select * from householder " +
            "where holder_id = ? and holder_password = ?";
    private static final String getHouseHolderListString  = "select * from householder";
    private static final String updateHouseHolderByAccountNumberString = "update householder " +
            "set holder_name = ?,holder_password = ?, house_number=?,house_square=?,holder_gender=?,holder_img = ? where holder_id = ?";
    private static final String insertIntoHouseHolderString = "insert into householder (holder_name," +
            "holder_password,house_number,house_square,holder_gender,holder_img) values (?,?,?,?,?,?)";
    private static final String deleteHouseHolderString = "delete from householder where holder_id = ?";
    private static final String fuzzySearchString = "select * from householder where concat(holder_id," +
            "holder_name,house_number,holder_gender) like ?";

    private static final String getHolderByGenderString = "select * from householder where holder_gender = ?";


    public HouseHolder getHolderByAccountNumberAndPassword(int accountNumber, String password){

        HouseHolder houseHolder = new HouseHolder();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getHolderByAccountNumberAndPasswordString);
            pStatement.setInt(1,accountNumber);
            pStatement.setString(2,password);
            ResultSet resultSet = pStatement.executeQuery();
            if(resultSet.next())
            {
                houseHolder.setId(resultSet.getInt(1));
                houseHolder.setName(resultSet.getString(2));
                houseHolder.setPassword(resultSet.getString(3));
                houseHolder.setHouseNumber(resultSet.getString(4));
                houseHolder.setHouseSquare(resultSet.getDouble(5));
                houseHolder.setGender(resultSet.getString(6));
                houseHolder.setImg(resultSet.getString(7));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return houseHolder;
    }


    public ObservableList<HouseHolder> getHouseHolderList(){

        ObservableList<HouseHolder> holderList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getHouseHolderListString);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                HouseHolder holder = new HouseHolder();
                holder.setId(resultSet.getInt(1));
                holder.setName(resultSet.getString(2));
                holder.setPassword(resultSet.getString(3));
                holder.setHouseNumber(resultSet.getString(4));
                holder.setHouseSquare(resultSet.getDouble(5));
                holder.setGender(resultSet.getString(6));
                holder.setImg(resultSet.getString(7));
                holderList.add(holder);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return holderList;
    }

    public void updateHouseHolderByAccountNumber(int accountNumber,String name,String password,
                                                 String houseNumber,double houseSquare,String gender,String img){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(updateHouseHolderByAccountNumberString);
            pStatement.setString(1,name);
            pStatement.setString(2,password);
            pStatement.setString(3,houseNumber);
            pStatement.setDouble(4,houseSquare);
            pStatement.setString(5,gender);
            pStatement.setString(6,img);
            pStatement.setInt(7,accountNumber);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertIntoHouseHolder(String name,String password,String houseNumber,Double houseSquare,String gender){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(insertIntoHouseHolderString);
            pStatement.setString(1,name);
            pStatement.setString(2,password);
            pStatement.setString(3,houseNumber);
            pStatement.setDouble(4,houseSquare);
            pStatement.setString(5,gender);
            pStatement.setString(6,"nullImg.png");
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteHouseHolder(int accoutNumber){

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(deleteHouseHolderString);
            pStatement.setInt(1,accoutNumber);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ObservableList<HouseHolder> fuzzySearch(String searchContent){

        String keywords = "%" + searchContent + "%";

        ObservableList<HouseHolder> holderList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(fuzzySearchString);
            pStatement.setString(1,keywords);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                HouseHolder holder = new HouseHolder();
                holder.setId(resultSet.getInt(1));
                holder.setName(resultSet.getString(2));
                holder.setPassword(resultSet.getString(3));
                holder.setHouseNumber(resultSet.getString(4));
                holder.setHouseSquare(resultSet.getDouble(5));
                holder.setGender(resultSet.getString(6));
                holder.setImg(resultSet.getString(7));
                holderList.add(holder);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return holderList;
    }

    public int getHolderByGender(String gender){
        int count = 0;
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getHolderByGenderString);
            pStatement.setString(1,gender);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                count = count +1;
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
