package persistence;

import entity.RepairRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class RepairRecordDAO {

    private static final String getRecordListString = "select * from repairrecord";
    private static final String deleteRecordByIdString = "delete from repairrecord where repair_id = ?";
    private static final String insertIntoRepairRecordString = "insert into repairrecord " +
            "(repair_time,repair_content,holder_id, is_repaired) values (?,?,?,?)";
    private static final String getRecordListByHolderIdString = "select * from repairrecord where holder_id = ? and is_repaired = ?";
    private static final String changeRecordStatusByIdString = "update repairrecord set is_repaired = ? where repair_id = ?";


    public ObservableList<RepairRecord>getRecordList(){

        ObservableList<RepairRecord> recordList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getRecordListString);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                RepairRecord repairRecord = new RepairRecord();
                repairRecord.setId(resultSet.getInt(1));
                repairRecord.setTime(resultSet.getTimestamp(2));
                repairRecord.setContent(resultSet.getString(3));
                repairRecord.setHolderId(resultSet.getInt(4));
                repairRecord.setIsRepaired(resultSet.getString(5));
                recordList.add(repairRecord);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return recordList;
    }

    public void deleteRecordById(int id){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(deleteRecordByIdString);
            pStatement.setInt(1,id);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertIntoRepairRecord(Timestamp time, String content, int holderId){

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(insertIntoRepairRecordString);
            pStatement.setTimestamp(1,time);
            pStatement.setString(2,content);
            pStatement.setInt(3,holderId);
            pStatement.setString(4,"no");
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ObservableList<RepairRecord>getRecordListByHolderId(int holderId){
        ObservableList<RepairRecord> recordList = FXCollections.observableArrayList();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getRecordListByHolderIdString);
            pStatement.setInt(1,holderId);
            pStatement.setString(2,"no");
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                RepairRecord repairRecord = new RepairRecord();
                repairRecord.setId(resultSet.getInt(1));
                repairRecord.setTime(resultSet.getTimestamp(2));
                repairRecord.setContent(resultSet.getString(3));
                repairRecord.setHolderId(resultSet.getInt(4));
                repairRecord.setIsRepaired(resultSet.getString(5));
                recordList.add(repairRecord);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return recordList;
    }

    public void changeRecordStatusById(int recordId){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(changeRecordStatusByIdString);
            pStatement.setString(1,"yes");
            pStatement.setInt(2,recordId);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
