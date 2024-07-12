package com.innovhr.innovhrapp;

import com.innovhr.innovhrapp.daos.AdminhrDAO;
import com.innovhr.innovhrapp.daos.EmployeeDAO;
import com.innovhr.innovhrapp.daos.UserDAO;
import com.innovhr.innovhrapp.models.Adminhr;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class InnovhrApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLViewLoader.loadLoginScene(stage, "Login",ViewPresets.SigninFXMLViews.fxml_login_path );
    }

    public static void main(String[] args) {
        BDConnectivity.makeSessionFactory();
        launch();
        String adminUsername = "root";
        try{

            Employee employee = new Employee();
            employee.setEmp_username(adminUsername);

            EmployeeDAO.saveEmployee(employee);
            employee = EmployeeDAO.findEmployeeByUsername(employee.getEmp_username());

            User AdminUser = new User();
            // Saving a user admin
            AdminUser.setUsername(adminUsername);
            AdminUser.setEmployee(employee);
            AdminUser.setAccessLevel(User.AccessLevel.ADMIN);
            AdminUser.setPassword(adminUsername);
            UserDAO.saveUser(AdminUser);
            Adminhr admin = new Adminhr();
            admin.setEmployee(employee);
            admin.setName(employee.getEmp_username());
            AdminhrDAO.saveAdminhr(admin);

        }catch (Exception e){
            AlertUtils.showAlertError("Error",e.getMessage());
        }

    }
}