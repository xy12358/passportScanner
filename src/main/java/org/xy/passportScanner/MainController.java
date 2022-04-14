package org.xy.passportScanner;

import com.tencentcloudapi.ocr.v20181119.models.MLIDPassportOCRResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xy.passportScanner.data.PassportBean;
import org.xy.passportScanner.utils.PdfUtil;
import org.xy.passportScanner.utils.RotateImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MainController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    //选择的文件
    @FXML
    private TextArea filePath;
    //选择文件按钮
    @FXML
    private Button fileButton;
    //开始识别按钮
    @FXML
    private Button scanButton;
    //进度展示label
    @FXML
    private Label tipLabel;
    //结果表
    @FXML
    private TableView resultTable;

    //选择的文件列表
    private List<File> filePathChoosed;
    private SimpleStringProperty filePathText = new SimpleStringProperty();
    private SimpleStringProperty tipText = new SimpleStringProperty();

    private List<PassportBean> resultList = new ArrayList<>();
    private ObservableList<PassportBean> data =  FXCollections.observableArrayList(resultList);
    private KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        filePath.textProperty().bind(filePathText);
        tipLabel.textProperty().bind(tipText);

        //初始化结果表
        resultTable.setItems(data);
        resultTable.getSelectionModel().setCellSelectionEnabled(true);
        resultTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        MenuItem item = new MenuItem("复制");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                copyToClipboard();
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        resultTable.setContextMenu(menu);

        resultTable.setOnKeyPressed(event -> {
            if (keyCodeCopy.match(event)) {
                copyToClipboard();
            }
        });

        TableColumn<PassportBean,String> id = new TableColumn<>("序号");
        id.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.05));
        id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(id);

        TableColumn<PassportBean,String> file = new TableColumn<>("文件");
        file.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.15));
        file.setCellValueFactory(new PropertyValueFactory<>("file"));
        file.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(file);

        TableColumn<PassportBean,String> number = new TableColumn<>("护照号");
        number.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.1));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        number.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(number);

        TableColumn<PassportBean,String> name = new TableColumn<>("姓名");
        name.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.25));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(name);

        TableColumn<PassportBean,String> gender = new TableColumn<>("性别");
        gender.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.05));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(gender);

        TableColumn<PassportBean,String> birthday = new TableColumn<>("出生日期");
        birthday.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.1));
        birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        birthday.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(birthday);

        TableColumn<PassportBean,String> expireDate = new TableColumn<>("有效期");
        expireDate.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.1));
        expireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        expireDate.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(expireDate);

        TableColumn<PassportBean,String> issuingCountry = new TableColumn<>("签发国");
        issuingCountry.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.05));
        issuingCountry.setCellValueFactory(new PropertyValueFactory<>("issuingCountry"));
        issuingCountry.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(issuingCountry);

        TableColumn<PassportBean,String> nationality = new TableColumn<>("国籍");
        nationality.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.05));
        nationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        nationality.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(nationality);

        TableColumn<PassportBean,String> comments = new TableColumn<>("备注");
        comments.prefWidthProperty().bind(resultTable.widthProperty().multiply(0.1));
        comments.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments.getStyleClass().add("alarm-table");
        resultTable.getColumns().add(comments);


//        data.add(new PassportBean("1", "c:/123", "12345", "LI SI", "M", "20010302", "20250909", "CHN", "CHN", ""));
//        data.add(new PassportBean("2", "c:/123", "12345", "LI SI", "M", "20010302", "20250909", "CHN", "CHN", ""));

        fileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("选择文件");
                    //设置可选择的文件类型
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("护照文件", "*.png","*.jpg","*.bmp","*.pdf")
                    );
                    //允许选择多个文件
                    filePathChoosed = fileChooser.showOpenMultipleDialog(fileButton.getScene().getWindow());
                    if(filePathChoosed != null && filePathChoosed.size() != 0){
                        StringBuilder sb = new StringBuilder();
                        for(File file: filePathChoosed){
                            sb.append(file.getPath() + "\n");
                        }
                        //将选择的文件展示到文本域中
                        filePathText.set(sb.toString());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });

        scanButton.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                try {
                    if(filePathChoosed == null || filePathChoosed.size() == 0){
                        tipText.set("请选择要识别的文件");
                        return;
                    }
                    data.clear();
                    int fileNum = filePathChoosed.size();
                    new Thread(()->{
                        for(int i = 0; i < fileNum; i++){
                            File file = filePathChoosed.get(i);
                            String tip = (i + 1) + "/" + fileNum + "    正在识别:" + file.getPath();
                            Platform.runLater(() -> tipText.set(tip));
                            try {
                                String filePath = "file://" + file.getPath();
                                String fileName = file.getName();
                                String extension = fileName.substring(fileName.lastIndexOf(".")+1);
                                if(extension.equalsIgnoreCase("pdf")){
                                    List<BufferedImage> pdfImages = PdfUtil.pdfToImage(file);
                                    for(BufferedImage image : pdfImages){
                                        boolean success = false;
                                        int rotate = 0;
                                        while(rotate++<4){
                                            image = RotateImage.Rotate(image, rotate*90);
                                            ByteArrayOutputStream imageIO = new ByteArrayOutputStream();
                                            ImageIO.write(image, "PNG", imageIO);
                                            MLIDPassportOCRResponse ocrResponse = TencentOcr.apiMLIDPassportOCR(imageIO.toByteArray(), "png");
                                            if(ocrResponse == null){
                                                continue;
                                            }else{
                                                success = true;
                                                data.add(new PassportBean(""+(i+1), filePath, ocrResponse.getID(), ocrResponse.getName(), ocrResponse.getSex(), formatDate(ocrResponse.getDateOfBirth()), formatDate(ocrResponse.getDateOfExpiration()), ocrResponse.getIssuingCountry(), ocrResponse.getNationality(), ""));
                                                break;
                                            }
                                        }
                                        if(!success){
                                            PassportBean pb = new PassportBean();
                                            pb.setId(""+(i+1));
                                            pb.setFile(filePath);
                                            pb.setComments("OCR识别失败");
                                            data.add(pb);
                                        }
                                    }

                                }else{
                                    boolean success = false;
                                    int rotate = 0;
                                    BufferedImage image = ImageIO.read(file);
                                    while(rotate++<4){
                                        image = RotateImage.Rotate(image, rotate*90);
                                        ByteArrayOutputStream imageIO = new ByteArrayOutputStream();
                                        ImageIO.write(image, "PNG", imageIO);
                                        MLIDPassportOCRResponse ocrResponse = TencentOcr.apiMLIDPassportOCR(imageIO.toByteArray(), "png");
                                        if(ocrResponse != null){
                                            success = true;
                                            data.add(new PassportBean(""+(i+1), filePath, ocrResponse.getID(), ocrResponse.getName(), ocrResponse.getSex(), formatDate(ocrResponse.getDateOfBirth()), formatDate(ocrResponse.getDateOfExpiration()), ocrResponse.getIssuingCountry(), ocrResponse.getNationality(), ""));
                                            break;
                                        }
                                    }
                                    if(!success){
                                        PassportBean pb = new PassportBean();
                                        pb.setId(""+(i+1));
                                        pb.setFile(filePath);
                                        pb.setComments("OCR识别失败");
                                        data.add(pb);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("OCR识别异常", e);
                            }
                        }
                        Platform.runLater(() -> tipText.set(fileNum + "个文件已识别完成"));
                    }).start();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }

    private void copyToClipboard(){
        ObservableList<TablePosition> posList = resultTable.getSelectionModel().getSelectedCells();
        int old_r = -1;
        StringBuilder clipboardString = new StringBuilder();
        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();
            Object cell = p.getTableColumn().getCellData(r);
            if (cell == null)
                cell = "";
            if (old_r == r)
                clipboardString.append('\t');
            else if (old_r != -1)
                clipboardString.append('\n');
            clipboardString.append(cell);
            old_r = r;
        }
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    private String formatDate(String mrzDate){
        if(mrzDate==null || "".equals(mrzDate.trim()) || mrzDate.length()<6){
            return "";
        }
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int century = year / 100;
        int currYear = year % 100;
        String strMrzDateYear = mrzDate.substring(0,2);
        int mrzDateYear;
        if(strMrzDateYear.startsWith("0")){
            mrzDateYear = Integer.parseInt(strMrzDateYear.substring(1,1));
        }else{
            mrzDateYear = Integer.parseInt(strMrzDateYear);
        }
        String mrzDateMonth = mrzDate.substring(2,4);
        String mrzDateDay = mrzDate.substring(4,6);
        String realYear;
        if(100 - mrzDateYear + currYear > 80){
            realYear = century + "" + strMrzDateYear;
        }else{
            realYear = (century-1) + "" + strMrzDateYear;
        }
        return mrzDateMonth + "/" + mrzDateDay + "/" + realYear;
    }


}
