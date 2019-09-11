package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTimePicker;
import entity.Announcement;
import entity.Manager;
import entity.RepairRecord;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import persistence.AnnouncementDAO;
import persistence.ManagerDAO;
import persistence.RepairRecordDAO;
import tool.Weather;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class HouseHolderStageController implements Initializable {

    @FXML
    private JFXTabPane contentTabPane;
    @FXML
    private Tab holderTab;
    @FXML
    private Tab repairTab;
    @FXML
    private Label tomorrowTemp;
    @FXML
    private Label todayTemp;
    @FXML
    private ImageView todayWeatherImg;
    @FXML
    private ImageView tomorrowWeatherImg;
    @FXML
    private ImageView informImg;

    @FXML
    private JFXComboBox<String> brokenTypeBox;
    @FXML
    private TextArea brokenDetailsText;

    @FXML
    private TableView<RepairRecord> holderRecordTableView;
    @FXML
    private TableColumn<RepairRecord,Timestamp> recordTimeColumn;
    @FXML
    private TableColumn<RepairRecord,String> recordContentColumn;
    @FXML
    private ImageView holderRepairmanImg;

    //轮播动画用
    @FXML
    private AnchorPane animationPane;
    @FXML
    private ImageView left_button;
    @FXML
    private ImageView right_button;
    //图片数组
    private ArrayList<ImageView> img_list;
    private Duration time = Duration.seconds(0.5);

    private double img_y = 0;
    private double img_z = 0;
    private double index0_x = 0;
    private double index1_x = 0;
    private double index2_x = 0;

    @FXML
    private ImageView announceImgView;
    @FXML
    private Label announceTimeLabel;
    @FXML
    private Label managerIdLabel;
    @FXML
    private TextArea announceContentText;
    @FXML
    private ImageView holderImgView;
    @FXML
    private ImageView holderRepairmanImg2;
    @FXML
    private Label holderNameLabel;

    @FXML
    private ImageView tab1Img;
    @FXML
    private ImageView tab2Img;
    @FXML
    private ImageView exitImg;

    public HouseHolderStageController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //设置边栏样式
        tab1Img.setImage(new Image("images/pointToTab.png"));
        tab2Img.setImage(new Image("images/pointToTab.png"));
        tab1Img.setVisible(true);
        tab2Img.setVisible(false);

        contentTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                String tabName = newValue.getText();
                if(tabName.equals("HouseHolder")){
                    tab1Img.setVisible(true);
                    tab2Img.setVisible(false);
                }
                else{
                    tab1Img.setVisible(false);
                    tab2Img.setVisible(true);
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
        //天气部分的图片加载
//        informImg.setImage(new Image("images/informImg.png");

        //将最新公告加载进相应控件中
        AnnouncementDAO announcementDAO = new AnnouncementDAO();
        Announcement latestAnnouncement = announcementDAO.getLatestAnnouncement();
        announceTimeLabel.setText(latestAnnouncement.getTime().toString());
        managerIdLabel.setText(String.valueOf(latestAnnouncement.getManagerId()));
        announceContentText.setWrapText(true);
        announceContentText.setEditable(false);
        announceContentText.setText(latestAnnouncement.getContent());
        ManagerDAO managerDAO = new ManagerDAO();
        Manager manager = managerDAO.getManagerByAccountNumber(latestAnnouncement.getManagerId());
        announceImgView.setImage(new Image("images/" + manager.getImg()));

        //设置用户头像
        holderImgView.setImage(new Image("images/" + Context.houseHolder.getImg()));
        //设置用户名字
        holderNameLabel.setText(Context.houseHolder.getName());
        holderNameLabel.setStyle("-fx-text-fill: #ffffff");
        //轮播动画设置
        Pane pane = getView(800,200);

        animationPane.getChildren().addAll(pane);
        AnchorPane.setTopAnchor(pane,50.0);
        AnchorPane.setLeftAnchor(pane,50.0);

        brokenTypeBox.getItems().addAll("Door Damaged","Wall Damaged",
                "EletricSystem Damaged","Elevator Damaged","Others");
        //加载表格右键的菜单
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Repair Completed");
        contextMenu.getItems().addAll(deleteItem);
        holderRecordTableView.setContextMenu(contextMenu);
        //将该住户的维修信息导入
        RepairRecordDAO repairRecordDAO = new RepairRecordDAO();
        ObservableList<RepairRecord> recordList = repairRecordDAO.getRecordListByHolderId(Context.houseHolder.getId());
        holderRecordTableView.setItems(recordList);
        recordTimeColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,Timestamp>("time"));
        recordContentColumn.setCellValueFactory(new PropertyValueFactory<RepairRecord,String>("content"));
        //动态监视表格，实现删除效果
        holderRecordTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RepairRecord>() {
            @Override
            public void changed(ObservableValue<? extends RepairRecord> observable, RepairRecord oldValue, RepairRecord newValue) {
                //删除操作
                deleteItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //db操作
                        repairRecordDAO.changeRecordStatusById(newValue.getId());
                        //更新表格数据
                        ObservableList<RepairRecord> recordList = repairRecordDAO.getRecordListByHolderId(Context.houseHolder.getId());
                        holderRecordTableView.setItems(recordList);
                    }
                });
            }
        });

        left_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                left_to_right(img_list);
            }
        });
        right_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                right_to_left(img_list);
            }
        });


        //维修申报样式设置
        brokenDetailsText.setWrapText(true);
        holderRepairmanImg.setImage(new Image("images/holderRepairmanImg.jpg"));
        holderRepairmanImg2.setImage(new Image("images/holderRepairmanImg2.jpg"));
        //获取天气信息
        Weather weather = new Weather();
        Map<String,String> tempMap = weather.getTemperature();
        todayTemp.setText(tempMap.get("todayTemp") + "℃");
        tomorrowTemp.setText(tempMap.get("tomorrowTemp") + "℃");
        String todayWeather = tempMap.get("todayWeather");
        String tomorrowWeather = tempMap.get("tomorrowWeather");
        String weatherImgUrl = "";

        //当天天气图片设置
        if(todayWeather.equals("晴")){
            weatherImgUrl = "0.png";
        }
        else if(todayWeather.equals("多云")){
            weatherImgUrl = "1.png";
        }
        else if(todayWeather.equals("阴")){
            weatherImgUrl = "2.png";
        }
        else if(todayWeather.equals("阵雨")){
            weatherImgUrl = "3.png";
        }
        else if(todayWeather.equals("雷阵雨")){
            weatherImgUrl = "4.png";
        }
        else if(todayWeather.equals("雷阵雨并伴有冰雹")){
            weatherImgUrl = "5.png";
        }
        else if(todayWeather.equals("雨加雪")){
            weatherImgUrl = "6.png";
        }
        else if(todayWeather.equals("小雨")){
            weatherImgUrl = "7.png";
        }
        else if(todayWeather.equals("中雨")){
            weatherImgUrl = "8.png";
        }
        else if(todayWeather.equals("大雨")){
            weatherImgUrl = "9.png";
        }
        else if(todayWeather.equals("暴雨") ){
            weatherImgUrl = "10.png";
        }
        todayWeatherImg.setImage(new Image("images/" + weatherImgUrl));

        if(tomorrowWeather.equals("晴")){
            weatherImgUrl = "0.png";
        }
        else if(tomorrowWeather.equals("多云")){
            weatherImgUrl = "1.png";
        }
        else if(tomorrowWeather.equals("阴")){
            weatherImgUrl = "2.png";
        }
        else if(tomorrowWeather.equals("阵雨")){
            weatherImgUrl = "3.png";
        }
        else if(tomorrowWeather.equals("雷阵雨")){
            weatherImgUrl = "4.png";
        }
        else if(tomorrowWeather.equals("雷阵雨并伴有冰雹")){
            weatherImgUrl = "5.png";
        }
        else if(tomorrowWeather.equals("雨加雪")){
            weatherImgUrl = "6.png";
        }
        else if(tomorrowWeather.equals("小雨")){
            weatherImgUrl = "7.png";
        }
        else if(tomorrowWeather.equals("中雨")){
            weatherImgUrl = "8.png";
        }
        else if(tomorrowWeather.equals("大雨")){
            weatherImgUrl = "9.png";
        }
        else if(tomorrowWeather.equals("暴雨") ){
            weatherImgUrl = "10.png";
        }
        tomorrowWeatherImg.setImage(new Image("images/" + weatherImgUrl));


    }

    public void jumpToHolder(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(holderTab);
    }

    public void jumpToRepair(ActionEvent actionEvent) {
        contentTabPane.getSelectionModel().select(repairTab);
    }

    public void repairApplyCommit(ActionEvent actionEvent) {

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String brokenType = brokenTypeBox.getValue();
        String details = brokenDetailsText.getText();

        if(brokenType.isEmpty()){
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please fill in the brokenType.");
            alert.show();
        }
        else{
            String content = brokenType + ": " + brokenDetailsText.getText();
            RepairRecordDAO repairRecordDAO = new RepairRecordDAO();
            repairRecordDAO.insertIntoRepairRecord(currentTime,content,Context.houseHolder.getId());
            //更新表格数据
            ObservableList<RepairRecord> recordList = repairRecordDAO.getRecordListByHolderId(Context.houseHolder.getId());
            holderRecordTableView.setItems(recordList);
            //清空表单
            brokenDetailsText.clear();
        }
    }

    public void repairApplyClear(ActionEvent actionEvent) {

        brokenDetailsText.clear();

    }

    //以下为轮播动态效果
    public Pane getView(int w, int h){

        left_button = new ImageView("images/left.png");
        right_button = new ImageView("images/right.png");
        HBox hBox = new HBox(w-(left_button.prefWidth(-1)*2));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(left_button,right_button);

        ImageView iv1 = new ImageView("images/animationImg1.jpg");
        iv1.setPreserveRatio(true);
        iv1.setFitWidth(w/2.5);
        ImageView iv2 = new ImageView("images/animationImg2.jpg");
        iv2.setPreserveRatio(true);
        iv2.setFitWidth(w/2.5);
        ImageView iv3 = new ImageView("images/animationImg3.jpg");
        iv3.setPreserveRatio(true);
        iv3.setFitWidth(w/2.5);

        img_z = 0;
        img_y = (h / 2) - (iv1.prefHeight(-1)/2);
        index0_x = 0-img_z;
        index1_x = ((w/2) - (iv1.getFitWidth()/2));
        index2_x = w - iv2.getFitWidth() + img_z;

        iv1.setTranslateX(index0_x);
        iv2.setTranslateX(index1_x);
        iv3.setTranslateX(index2_x);

        iv1.setTranslateY(img_y);
        iv2.setTranslateY(img_y);
        iv3.setTranslateY(img_y);

        iv1.setTranslateZ(img_z);
        iv2.setTranslateZ(-50);
        iv3.setTranslateZ(img_z);

        img_list = new ArrayList<ImageView>();
        img_list.add(iv1);
        img_list.add(iv2);
        img_list.add(iv3);

        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:white");
        ap.getChildren().addAll(iv1,iv2,iv3);
        SubScene subScene = new SubScene(ap,w,h,true, SceneAntialiasing.BALANCED);

        //相机
        PerspectiveCamera camera = new PerspectiveCamera();
        subScene.setCamera(camera);


        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: green");
        pane.getChildren().addAll(subScene,hBox);
        return pane;
    }

    //实现轮播图片移动函数
    public void right_to_left(ArrayList<ImageView> list){

        ImageView iv0 = list.get(0);
        ImageView iv1 = list.get(1);
        ImageView iv2 = list.get(2);
        left_to_middle_Animation(iv0);
        middle_to_right_Animation(iv1);
        right_to_left_Animation(iv2);

        list.clear();
        list.add(iv2);
        list.add(iv0);
        list.add(iv1);


    }
    public void left_to_right(ArrayList<ImageView> list){

        ImageView iv0 = list.get(0);
        ImageView iv1 = list.get(1);
        ImageView iv2 = list.get(2);
        left_to_right_Animation(iv0);
        middle_to_left_Animation(iv1);
        right_to_middle_Animation(iv2);

        list.clear();
        list.add(iv1);
        list.add(iv2);
        list.add(iv0);

    }
    public void left_to_middle_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index0_x);
        tt.setFromZ(img_z);
        tt.setToX(index1_x);
        tt.setToZ(-50);

        tt.play();
    }

    public void middle_to_right_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index1_x);
        tt.setFromZ(-50);
        tt.setToX(index2_x);
        tt.setToZ(img_z);

        tt.play();
    }

    public void right_to_left_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index2_x);
        tt.setFromZ(img_z);
        tt.setToX(index0_x);
        tt.setToZ(img_z);

        tt.play();
    }
    public void left_to_right_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index0_x);
        tt.setFromZ(img_z);
        tt.setToX(index2_x);
        tt.setToZ(img_z);

        tt.play();
    }
    public void middle_to_left_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index1_x);
        tt.setFromZ(-50);
        tt.setToX(index0_x);
        tt.setToZ(img_z);

        tt.play();
    }
    public void right_to_middle_Animation(ImageView iv){

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(time);
        tt.setNode(iv);

        tt.setFromX(index2_x);
        tt.setFromZ(img_z);
        tt.setToX(index1_x);
        tt.setToZ(-50);
        tt.play();
    }
}
