package persistence;

import entity.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StaffDAO {
    private static final String getStaffrListString  = "select * from staff";
    private static final String updateStaffByAccountNumberString = "update staff " +
            "set staff_name = ?,staff_position = ?,staff_gender = ?, staff_telephone = ? where staff_id = ?";
    private static final String insertIntoStaffString = "insert into staff (staff_name," +
            "staff_position,staff_gender,staff_telephone) values (?,?,?,?)";
    private static final String deleteStaffString = "delete from staff where staff_id = ?";
    private static final String fuzzySearchString = "select * from staff where concat(staff_id," +
            "staff_name,staff_position,staff_gender) like ?";

    private static final String getPositionNumberString = "select * from staff where staff_position = ?";
    private static final String getStaffByGenderString = "select * from staff where staff_gender = ?";

    public ObservableList<Staff> getStaffrList(){

        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getStaffrListString);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt(1));
                staff.setName(resultSet.getString(2));
                staff.setPosition(resultSet.getString(3));
                staff.setGender(resultSet.getString(4));
                staff.setTelephone(resultSet.getString(5));
                staffList.add(staff);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return staffList;
    }

    public void updateStaffByAccountNumber(int accountNumber, String name, String position,String gender,String telephone){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(updateStaffByAccountNumberString);
            pStatement.setString(1,name);
            pStatement.setString(2,position);
            pStatement.setString(3,gender);
            pStatement.setString(4,telephone);
            pStatement.setInt(5,accountNumber);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertIntoStaff(String name,String position,String gender,String telephone){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(insertIntoStaffString);
            pStatement.setString(1,name);
            pStatement.setString(2,position);
            pStatement.setString(3,gender);
            pStatement.setString(4,telephone);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteStaff(int accountNumber){

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(deleteStaffString);
            pStatement.setInt(1,accountNumber);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ObservableList<Staff> fuzzySearch(String searchContent){

        String keywords = "%" + searchContent + "%";

        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(fuzzySearchString);
            pStatement.setString(1,keywords);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt(1));
                staff.setName(resultSet.getString(2));
                staff.setPosition(resultSet.getString(3));
                staff.setGender(resultSet.getString(4));
                staff.setTelephone(resultSet.getString(5));
                staffList.add(staff);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return staffList;
    }

    public int getPositionNumber(String position){
        int count = 0;
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getPositionNumberString);
            pStatement.setString(1,position);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                count= count + 1;
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;

    }

    public int getStaffByGender(String gender){
        int count = 0;
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getStaffByGenderString);
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

    public ObservableList<Staff> getRepairmanList(){

        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getPositionNumberString);
            pStatement.setString(1,"Repairman");
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                Staff staff = new Staff();
                staff.setId(resultSet.getInt(1));
                staff.setName(resultSet.getString(2));
                staff.setPosition(resultSet.getString(3));
                staff.setGender(resultSet.getString(4));
                staff.setTelephone(resultSet.getString(5));
                staffList.add(staff);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return staffList;

    }

}
