package persistence;

import entity.Announcement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class AnnouncementDAO {

    private static final String insertIntoAnnouncementString = "insert into announcement " +
            "(announce_time,announce_content,manager_id) values (?,?,?)";
    private static final String getAnnounceListString = "select * from announcement";


    public void InsertIntoAnnouncement(Timestamp time,String content,int id){
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(insertIntoAnnouncementString);
            pStatement.setTimestamp(1,time);
            pStatement.setString(2,content);
            pStatement.setInt(3,id);
            pStatement.executeUpdate();

            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ObservableList<Announcement> getAnnounceList(){

        ObservableList<Announcement> announceList = FXCollections.observableArrayList();

        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getAnnounceListString);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                Announcement announcement = new Announcement();
                announcement.setId(resultSet.getInt(1));
                announcement.setTime(resultSet.getTimestamp(2));
                announcement.setContent(resultSet.getString(3));
                announcement.setManagerId(resultSet.getInt(4));
                announceList.add(announcement);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return announceList;
    }

    public Announcement getLatestAnnouncement(){

        Announcement announcement = new Announcement();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getAnnounceListString);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next())
            {
                announcement.setId(resultSet.getInt(1));
                announcement.setTime(resultSet.getTimestamp(2));
                announcement.setContent(resultSet.getString(3));
                announcement.setManagerId(resultSet.getInt(4));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return announcement;
    }
}
