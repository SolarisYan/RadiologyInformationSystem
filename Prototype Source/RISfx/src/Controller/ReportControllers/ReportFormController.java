package Controller.ReportControllers;

import Controller.Main;
import Model.Item;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;
import java.lang.*;

public class ReportFormController implements Initializable {
    @FXML
    private TextField NameField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField idField;
    @FXML
    private TextField sexField;
    @FXML
    private TextField apptIDField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField physicianField;
    @FXML
    private TextArea reasonField;
    @FXML
    private DatePicker prevExamField;
    @FXML
    private TextArea historyField;
    @FXML
    private TextField timeField;
    @FXML
    private TextArea techniqueField;
    @FXML
    private TextArea findingField;
    @FXML
    private TextField radiologistField,clinicalIndication, procedureRequested;
    @FXML
    private DatePicker signDateField;

    @FXML
    private ImageView report_image;

    public static String getPatient_id() {
        return patient_id;
    }

    public static void setPatient_id(String patient_id) {
        ReportFormController.patient_id = patient_id;
    }

    static String patient_id;

    public String getImage_id() {
        return image_id;
    }

    public static void setImage_id(String image_id) {
        ReportFormController.image_id = image_id;
    }

    static String image_id;

    public static int getAppointment_id() {
        return appointment_id;
    }

    public static void setAppointment_id(int appointment_id) {
        ReportFormController.appointment_id = appointment_id;
    }

    static int appointment_id;

    public static void setView(int appointment_id, String image_id) throws Exception{
        setAppointment_id(appointment_id);
        System.out.println(appointment_id);
        setPatient_id(patient_id);
        setImage_id(image_id);
        Main.setPopupWindow("ReportViews/ReportForm.fxml");
        Main.getSessionUser().getFullName();

    }

    public void initialize(URL url, ResourceBundle arg1){

        try{

            ResultSet rs = Report.gatherPatientInfo(getAppointment_id(), getImage_id());


            while (rs.next()){



                NameField.setText(rs.getString("name"));
                dobField.setText(rs.getString("date_of_birth"));
                idField.setText(rs.getString("patient_id"));
                sexField.setText(rs.getString("sex"));
                apptIDField.setText(rs.getString("appointment_id"));
                dateField.setText(rs.getString("appointment_date"));
                timeField.setText(rs.getString("patient_sign_in_time"));
                reasonField.setText(rs.getString("reason_for_referral"));
                historyField.setText(rs.getString("special_comments"));
                procedureRequested.setText(rs.getString("procedure_name"));

                InputStream is = rs.getBinaryStream("imagedata");
                Image image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
                report_image.setImage(image);


            }
            rs.close();
            ResultSet rs2 = Report.gatherReferralInfo(getImage_id());
            if(rs2.next()){

                physicianField.setText(rs2.getString("name"));

            }else{
                physicianField.setText("No referring physician");
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public boolean submitReport() throws SQLException{

        if(!findingField.getText().isEmpty() || clinicalIndication.getText().isEmpty()){
            Report.sendReport(clinicalIndication.getText(), findingField.getText(), getImage_id(), procedureRequested.getText(), radiologistField.getText());
        }

        return true;
    }

    //checks if the radiologist is the one logged in
    public boolean checkRadiologist(String radiologist){
        if(radiologist!= Main.getSessionUser().getFullName()){
            return false;
        }
        return true;
    }


}
