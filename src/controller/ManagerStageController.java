package controller;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import entity.Announcement;
import entity.HouseHolder;
import entity.RepairRecord;
import entity.Staff;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistence.AnnouncementDAO;
import persistence.HouseHolderDAO;
import persistence.RepairRecordDAO;
import persistence.StaffDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class ManagerStageController implements Initializable {

    public static AppModel model = new AppModel();
    //tabPane部分
    @FXML
    private JFXTabPane contentTabPane;
    @FXML
    private Tab holderTab;
    @FXML
    private Tab staffTab;
    @FXML
    private Tab repairTab;
    @FXML
    private Tab announceTab;
    //表格部分
    @FXML
    private TableView<HouseHolder> holderTableView;
    @FXML
    private TableColumn<HouseHolder,Number> holderIdColumn;
    @FXML
    private TableColumn<HouseHolder,String> holderNameColumn;
    @FXML
    private TableColumn<HouseHolder,String> holderPasswordColumn;
    @FXML
    private TableColumn<HouseHolder,String> houseNumColumn;
    @FXML
    private TableColumn<HouseHolder,Number> houseSquareColumn;
    @FXML
    private TableColumn<HouseHolder,String> holderGenderColumn;
    @FXML
    private JFXTextField searchHolderText;
    @FXML
    private ImageView holderSearchImgView;
    @FXML
    private PieChart pieChartHolder;
    @FXML
    private Label holderNameLabel;
    @FXML
    private Label holderGenderLabel;
    @FXML
    private ImageView holderImgView;

    @FXML
    private TableView<Staff> staffTableView;
    @FXML
    private TableColumn<Staff,Number> staffIdColumn;
    @FXML
    private TableColumn<Staff,String> staffNameColumn;
    @FXML
    private TableColumn<Staff,String> staffPositionColumn;
    @FXML
    private TableColumn<Staff,String> staffGenderColumn;
    @FXML
    private TableColumn<Staff,String> staffTelephoneColumn;
    @FXML
    private JFXTextField searchStaffText;
    @FXML
    private PieChart pieChartStaff;
    @FXML
    private BarChart<String,Number> barChartStaff;
    @FXML
    private CategoryAxis barChartX;
    @FXML
    private NumberAxis barChartY;
    @FXML
    private ImageView staffSearchImgView;

    @FXML
    private TableView<RepairRecord> recordTableView;
    @FXML
    private TableColumn<RepairRecord,Number> repairIdColumn;
    @FXML
    private TableColumn<RepairRecord,Timestamp> repairTimeColumn;
    @FXML
    private TableColumn<RepairRecord,String> repairContentColumn;
    @FXML
    private TableColumn<RepairRecord,Number> repairHolderIdColumn;
    @FXML
    private TableColumn<RepairRecord,String> repairIsRepairedColumn;
    @FXML
    private ImageView repairmanImgView;
    @FXML
    private TableView<Staff> repairmanTableView;
    @FXML
    private TableColumn<Staff,String> repairmanNameColumn;
    @FXML
    private TableColumn<Staff,String> repairmanTelephoneColumn;


    @FXML
    private TextArea announceContentText;
    @FXML
    private ImageView announceImgView;
    @FXML
    private ImageView managerImgView;
    @FXML
    private Label managerNameLabel;
    @FXML
    private TableView<Announcement> announceTableView;
    @FXML
    private TableColumn<Announcement,Number> announceIdColumn;
    @FXML
    private TableColumn<Announcement,Timestamp> announceTimeColumn;
    @FXML
    private TableColumn<Announcement,String> announceContentColumn;
    @FXML
    private TableColumn<Announcement,Number> announceManagerIdColumn;
    @FXML
    private TextArea showAnnounceText;


    @FXML
    private ImageView tab1Img;
    @FXML
    private ImageView tab2Img;
    @FXML
    private ImageView tab3Img;
    @FXML
    private ImageView tab4Img;
    @FXML
    private ImageView exitImg;


    public ManagerStageController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //设置左侧边栏的样式
        tab1Img.setImage(new Image("images/pointToTab.png"));
        tab2Img.setImage(new Image("images/pointToTab.png"));
        tab3Img.setImage(new Image("images/pointToTab.png"));
        tab4Img.setImage(new Image("images/pointToTab.png"));

        tab1Img.setVisible(true);
        tab2Img.setVisible(false);
        tab3Img.setVisible(false);
        tab4Img.setVisible(false);

        contentTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                String tabName = newValue.getText();
                if(tabName.equals("HouseHolder")){
                    tab1Img.setVisible(true);
                    tab2Img.setVisible(false);
                    tab3Img.setVisible(false);
                    tab4Img.setVisible(false);
                }
                else if(tabName.equals("Staff")){
                    tab1Img.setVisible(false);
                    tab2Img.setVisible(true);
                    tab3Img.setVisible(false);
                    tab4Img.setVisible(false);
                }
                else if(tabName.equals("RepairRecord")){
                    tab1Img.setVisible(false);
                    tab2Img.setVisible(false);
                    tab3Img.setVisible(true);
                    tab4Img.setVisible(false);
                }
                else{
                    tab1Img.setVisible(false);
                    tab2Img.setVisible(false);
                    tab3Img.setVisible(false);
                    tab4Img.setVisible(true);
                }
            }
        });
        exitImg.setImage(new Image("images/exit.png"));
        exitImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"To exit the system, please press Confirm.",new ButtonType("Cancel", ButtonBar.ButtonData.NO),
                        new ButtonType("Confirm", ButtonBar.ButtonData.YES));
                //设置窗口的标题
                alert.setTitle("Information");
                alert.setHeaderText("Information");

                //showAndWait() 将在对话框消失以前不会执行之后的代码
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
                    System.exit(0);
                }
                else {
                    alert.close();
                }
            }
        });

        //测试
//        holderTableView.setStyle(" -fx-border-width: 1px;\n" +
//                "    -fx-border-color: #CACACA;\n" +
//                "    -fx-background-color: transparent;");

//        holderTab.getContent().setStyle("-fx-background-color: green");
//        staffTab.getContent().setStyle("-fx-background-color: red");
//        repairTab.getContent().setStyle("-fx-background-color: blue");
//        announceTab.getContent().setStyle("-fx-background-color: black");

        /*左侧导航栏*/
        managerImgView.setImage(new Image("images/" + Context.manager.getImg()));
        managerNameLabel.setText(Context.manager.getName());
//        holderImgView.setImage(new Image("images/codepatient.png"));
        /*holder表*/
        holderNameLabel.setText("CodePatient");
        holderGenderLabel.setText("male");
        HouseHolderDAO houseHolderDAO = new HouseHolderDAO();
        int maleNum = houseHolderDAO.getHolderByGender("male");
        int femaleNum = houseHolderDAO.getHolderByGender("female");
        PieChart.Data data1 = new PieChart.Data("male",maleNum);
        PieChart.Data data2 = new PieChart.Data("female",femaleNum);
        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();
        dataList.addAll(data1,data2);
        pieChartHolder.setData(dataList);
        pieChartHolder.setLabelLineLength(10);
        pieChartHolder.setAnimated(true);
        pieChartHolder.setStyle("-fx-font-size: 18;-fx-font-family: Calibri;");
        pieChartHolder.setTitle("Proportion of Gender");

        holderSearchImgView.setImage(new Image("images/Search.png"));
        //加载表格右键的菜单
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(deleteItem);
        holderTableView.setContextMenu(contextMenu);
        //加载所有的住户信息
        ObservableList<HouseHolder> holderList = houseHolderDAO.getHouseHolderList();
        holderTableView.setItems(holderList);
        holderIdColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,Number>("id"));
        holderNameColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,String>("name"));
        holderPasswordColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,String>("password"));
        houseNumColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,String>("houseNumber"));
        houseSquareColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,Number>("houseSquare"));
        holderGenderColumn.setCellValueFactory(new PropertyValueFactory<HouseHolder,String>("gender"));
        //双击一行，弹出修改框修改
        holderTableView.setRowFactory( tv -> {
            TableRow<HouseHolder> row = new TableRow<HouseHolder>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Stage stage = null;
                    try {
                        //将id值传给更改界面
                        Context.accountNumber = row.getItem().getId();
                        //弹出窗口
                        stage = Context.factory.createStage("UpdateHolderStage",400,600,"fxml/UpdateHolderStage.fxml");
                        stage.getIcons().add(new Image("images/logo.png"));
                        //设置为模态框
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        //删除操作
        holderTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HouseHolder>() {
            @Override
            public void changed(ObservableValue<? extends HouseHolder> observable, HouseHolder oldValue, HouseHolder newValue) {

                /*BUG:空指针,解决，因删除操作会导致newValue为空，加上条件判断即可*/
                //显示信息操作
                if(newValue != null){
                    HouseHolder houseHolder = houseHolderDAO.getHolderByAccountNumberAndPassword(newValue.getId(),newValue.getPassword());
                    holderNameLabel.setText(houseHolder.getName());
                    holderGenderLabel.setText(houseHolder.getGender());
                    holderImgView.setImage(new Image("images/"+ houseHolder.getImg()));
                }
                //删除操作
                deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //db操作
                        houseHolderDAO.deleteHouseHolder(newValue.getId());
                        //更新表格数据
                        /*bug:此处的级联删除*/
                        ObservableList<HouseHolder>  list = houseHolderDAO.getHouseHolderList();
                        holderTableView.setItems(list);
                        //更新图表
                        int maleNum = houseHolderDAO.getHolderByGender("male");
                        int femaleNum = houseHolderDAO.getHolderByGender("female");
                        PieChart.Data data1 = new PieChart.Data("male",maleNum);
                        PieChart.Data data2 = new PieChart.Data("female",femaleNum);
                        ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();
                        dataList.addAll(data1,data2);
                        pieChartHolder.setData(dataList);

                    }
                });

            }
        });
        //搜索操作
        searchHolderText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //更新表格
                ObservableList<HouseHolder>list = houseHolderDAO.fuzzySearch(newValue);
                holderTableView.setItems(list);
            }
        });


        /*staff表*/
        //加载所有工作人员的信息
        StaffDAO staffDAO = new StaffDAO();
        barChartX.setLabel("Position");
//        NumberAxis barChartY = new NumberAxis(0,6,1);
        ObservableList<XYChart.Series<String,Number>> barCharList = FXCollections.observableArrayList();
        //柱状图设置
        XYChart.Series<String,Number> xy = new XYChart.Series<String,Number>();
        xy.setName("Amount");
        int cleanerAmount = staffDAO.getPositionNumber("Cleaner");
        int securityAmount = staffDAO.getPositionNumber("Security");
        int repairmanAmount = staffDAO.getPositionNumber("Repairman");
        ObservableList<XYChart.Data<String,Number>> barCharDataList = FXCollections.observableArrayList();
        XYChart.Data<String,Number> barChartData1 = new XYChart.Data<String,Number>("Cleaner",cleanerAmount);
        XYChart.Data<String,Number> barChartData2 = new XYChart.Data<String,Number>("Security",securityAmount);
        XYChart.Data<String,Number> barChartData3 = new XYChart.Data<String,Number>("Repairman",repairmanAmount);
        barCharDataList.addAll(barChartData1,barChartData2,barChartData3);
        xy.setData(barCharDataList);
        barCharList.add(xy);
        barChartStaff.setData(barCharList);
        barChartStaff.setAnimated(true);
        barChartStaff.setPrefSize(400,250);
        //饼状图设置
        int staffMaleNum = staffDAO.getStaffByGender("male");
        int staffFemaleNum = staffDAO.getStaffByGender("female");
        PieChart.Data staffData1 = new PieChart.Data("male",staffMaleNum);
        PieChart.Data staffData2 = new PieChart.Data("female",staffFemaleNum);
        ObservableList<PieChart.Data> staffDataList = FXCollections.observableArrayList();
        staffDataList.addAll(staffData1,staffData2);
        pieChartStaff.setData(staffDataList);
        pieChartStaff.setLabelLineLength(10);
        pieChartStaff.setAnimated(true);
        pieChartStaff.setStyle("-fx-font-size: 18;-fx-font-family: Calibri;");
        pieChartStaff.setTitle("Proportion of Gender");
        //搜索图标设置
        staffSearchImgView.setImage(new Image("images/Search.png"));

        ObservableList<Staff> staffList = staffDAO.getStaffrList();
        staffTableView.setItems(staffList);
        staffTableView.setContextMenu(contextMenu);
        //表格内填入数据
        staffIdColumn.setCellValueFactory(new PropertyValueFactory<Staff,Number>("id"));
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("name"));
        staffPositionColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("position"));
        staffGenderColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("gender"));
        staffTelephoneColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("telephone"));
        //双击一行，弹出修改框修改
        staffTableView.setRowFactory( tv -> {
            TableRow<Staff> row = new TableRow<Staff>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Stage stage = null;
                    try {
                        //将id值传给更改界面
                        Context.accountNumber = row.getItem().getId();
                        //弹出窗口
                        stage = Context.factory.createStage("UpdateStaffStage",400,600,"fxml/UpdateStaffStage.fxml");
                        stage.getIcons().add(new Image("images/logo.png"));
                        //设置为模态框
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        //删除操作
        staffTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue<? extends Staff> observable, Staff oldValue, Staff newValue) {

                //删除操作
                deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //db操作
                        staffDAO.deleteStaff(newValue.getId());
                        //更新表格数据
                        /*bug:此处的级联删除*/
                        ObservableList<Staff>  list = staffDAO.getStaffrList();
                        staffTableView.setItems(list);

                        int staffMaleNum = staffDAO.getStaffByGender("male");
                        int staffFemaleNum = staffDAO.getStaffByGender("female");
                        PieChart.Data staffData1 = new PieChart.Data("male",staffMaleNum);
                        PieChart.Data staffData2 = new PieChart.Data("female",staffFemaleNum);
                        ObservableList<PieChart.Data> staffDataList = FXCollections.observableArrayList();
                        staffDataList.addAll(staffData1,staffData2);
                        pieChartStaff.setData(staffDataList);

                        int cleanerAmount = staffDAO.getPositionNumber("Cleaner");
                        int securityAmount = staffDAO.getPositionNumber("Security");
                        int repairmanAmount = staffDAO.getPositionNumber("Repairman");
                        ObservableList<XYChart.Series<String,Number>> barCharList = FXCollections.observableArrayList();
                        ObservableList<XYChart.Data<String,Number>> barCharDataList = FXCollections.observableArrayList();
                        XYChart.Data<String,Number> barChartData1 = new XYChart.Data<String,Number>("Cleaner",cleanerAmount);
                        XYChart.Data<String,Number> barChartData2 = new XYChart.Data<String,Number>("Security",securityAmount);
                        XYChart.Data<String,Number> barChartData3 = new XYChart.Data<String,Number>("Repairman",repairmanAmount);
                        barCharDataList.addAll(barChartData1,barChartData2,barChartData3);
                        xy.setData(barCharDataList);
                        barCharList.add(xy);
                        barChartStaff.setData(barCharList);
                    }
                });
            }
        });
        //搜索操作
        searchStaffText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //更新表格
                ObservableList<Staff>list = staffDAO.fuzzySearch(newValue);
                staffTableView.setItems(list);
            }
        });
        //维修人员表
        ObservableList<Staff> repairmanList = staffDAO.getRepairmanList();
        repairmanTableView.setItems(repairmanList);
        //表格内填入数据
        repairmanNameColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("name"));
        repairmanTelephoneColumn.setCellValueFactory(new PropertyValueFactory<Staff,String>("telephone"));


        //维修表
        recordTableView.setContextMenu(contextMenu);

        //加载所有的维修信息
        RepairRecordDAO repairRecordDAO = new RepairRecordDAO();
        ObservableList<RepairRecord> list = repairRecordDAO.getRecordList();
        recordTableView.setItems(list);
        //将数据加载进入表格
        repairIdColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,Number>("id"));
        repairTimeColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,Timestamp>("time"));
        repairContentColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,String>("content"));
        repairHolderIdColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,Number>("holderId"));
        repairIsRepairedColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,String>("isRepaired"));

        //删除操作
        recordTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RepairRecord>() {
            @Override
            public void changed(ObservableValue<? extends RepairRecord> observable, RepairRecord oldValue, RepairRecord newValue) {
                deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        repairRecordDAO.deleteRecordById(newValue.getId());
                        //更新表格数据
                        ObservableList<RepairRecord>  list = repairRecordDAO.getRecordList();
                        recordTableView.setItems(list);
                    }
                });
            }
        });

        //将公告的内容显示在textArea中
        announceTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Announcement>() {
            @Override
            public void changed(ObservableValue<? extends Announcement> observable, Announcement oldValue, Announcement newValue) {

                if(newValue!=null){
                    showAnnounceText.setText(newValue.getContent());
                }

            }
        });

        //维修表
        repairmanImgView.setImage(new Image("images/repairmanImg.jpg"));
        //动态监听model的text值，若改变则刷新表格
        model.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //更新表格数据
                ObservableList<HouseHolder>  holderList = houseHolderDAO.getHouseHolderList();
                holderTableView.setItems(holderList);
                ObservableList<Staff>  staffList = staffDAO.getStaffrList();
                staffTableView.setItems(staffList);
                //更新图表
                int maleNum = houseHolderDAO.getHolderByGender("male");
                int femaleNum = houseHolderDAO.getHolderByGender("female");
                PieChart.Data data1 = new PieChart.Data("male",maleNum);
                PieChart.Data data2 = new PieChart.Data("female",femaleNum);
                ObservableList<PieChart.Data> dataList = FXCollections.observableArrayList();
                dataList.addAll(data1,data2);
                pieChartHolder.setData(dataList);

                int staffMaleNum = staffDAO.getStaffByGender("male");
                int staffFemaleNum = staffDAO.getStaffByGender("female");
                PieChart.Data staffData1 = new PieChart.Data("male",staffMaleNum);
                PieChart.Data staffData2 = new PieChart.Data("female",staffFemaleNum);
                ObservableList<PieChart.Data> staffDataList = FXCollections.observableArrayList();
                staffDataList.addAll(staffData1,staffData2);
                pieChartStaff.setData(staffDataList);

                int cleanerAmount = staffDAO.getPositionNumber("Cleaner");
                int securityAmount = staffDAO.getPositionNumber("Security");
                int repairmanAmount = staffDAO.getPositionNumber("Repairman");
                ObservableList<XYChart.Series<String,Number>> barCharList = FXCollections.observableArrayList();
                ObservableList<XYChart.Data<String,Number>> barCharDataList = FXCollections.observableArrayList();
                XYChart.Data<String,Number> barChartData1 = new XYChart.Data<String,Number>("Cleaner",cleanerAmount);
                XYChart.Data<String,Number> barChartData2 = new XYChart.Data<String,Number>("Security",securityAmount);
                XYChart.Data<String,Number> barChartData3 = new XYChart.Data<String,Number>("Repairman",repairmanAmount);
                barCharDataList.addAll(barChartData1,barChartData2,barChartData3);
                xy.setData(barCharDataList);
                barCharList.add(xy);
                barChartStaff.setData(barCharList);

                //更新repairman表
                ObservableList<Staff> repairmanList = staffDAO.getRepairmanList();
                repairmanTableView.setItems(repairmanList);

                //更改model表
                AnnouncementDAO announcementDAO = new AnnouncementDAO();
                ObservableList<Announcement> announceList = announcementDAO.getAnnounceList();
                announceTableView.setItems(announceList);
            }
        });

        //announce部分样式
        showAnnounceText.setWrapText(true);
        showAnnounceText.setEditable(false);
        announceContentText.setWrapText(true);
        announceImgView.setImage(new Image("images/announceImg.png"));
        //公告表格
        AnnouncementDAO announcementDAO = new AnnouncementDAO();
        ObservableList<Announcement> announceList = announcementDAO.getAnnounceList();
        announceTableView.setItems(announceList);
        //将数据加载进入表格
        announceIdColumn.setCellValueFactory(new PropertyValueFactory<Announcement,Number>("id"));
        announceTimeColumn.setCellValueFactory(new PropertyValueFactory<Announcement,Timestamp>("time"));
        announceContentColumn.setCellValueFactory(new PropertyValueFactory<Announcement,String>("content"));
        announceManagerIdColumn.setCellValueFactory(new PropertyValueFactory<Announcement,Number>("managerId"));

    }

    //生成随机字符串，动态调整model
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


    public static void setText(String text)
    {
        model.setText(text);
    }


    //左侧按钮控制tabpane换页
    public void jumpToHolder(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(holderTab);
    }
    public void jumpToStaff(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(staffTab);
    }
    public void jumpToRepair(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(repairTab);
    }
    public void jumpToAnnounce(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(announceTab);
    }


    public void jumpToHolderAdd(ActionEvent actionEvent) throws IOException {

        Stage stage = Context.factory.createStage("AddHolderStage",400,600,"fxml/AddHolderStage.fxml");
        stage.getIcons().add(new Image("images/logo.png"));
        //设置为模态框
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public void jumpToStaffAdd(ActionEvent actionEvent) throws IOException {

        Stage stage = Context.factory.createStage("AddStaffStage",400,600,"fxml/AddStaffStage.fxml");
        stage.getIcons().add(new Image("images/logo.png"));
        //设置为模态框
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void commitAnnounce(ActionEvent actionEvent) {
        String content = announceContentText.getText();
        int id = Context.manager.getId();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //db操作
        AnnouncementDAO announcementDAO = new AnnouncementDAO();
        announcementDAO.InsertIntoAnnouncement(currentTime,content,id);
        //动态更改model
        setText(getRandomString(4));
        announceContentText.clear();

    }

    public void clearAnnounce(ActionEvent actionEvent) {
        announceContentText.clear();
    }
}
