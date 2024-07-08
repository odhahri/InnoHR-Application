package com.innovhr.innovhrapp.utils.constants;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ViewPresets {
    public static final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    public static final String bgColor = "";

    public static class AdminFxmlViews{
        public static final String fxml_admin_space_path = "/com/innovhr/innovhrapp/view/adminhr/adminhr-space.fxml";
        public static final String fxml_admin_department_path = "/com/innovhr/innovhrapp/view/adminhr/department-manage.fxml";
        public static final String fxml_admin_document_path ="/com/innovhr/innovhrapp/view/adminhr/document-manage.fxml" ;
        public static final String fxml_admin_employee_path = "/com/innovhr/innovhrapp/view/adminhr/employee-manage.fxml";
        public static final String fxml_admin_manager_path = "/com/innovhr/innovhrapp/view/adminhr/manager-manage.fxml";
        public static final String fxml_admin_request_path ="/com/innovhr/innovhrapp/view/adminhr/request-manage.fxml" ;
        public static final String fxml_admin_team_path = "/com/innovhr/innovhrapp/view/adminhr/team-manage.fxml";
        public static final String fxml_admin_training_path ="/com/innovhr/innovhrapp/view/adminhr/training-manage.fxml" ;
    }
    public static class ManagerFxmlViews{
        public static final String fxml_manager_space_path = "";
        public static final String fxml_manager_request_path ="" ;
        public static final String fxml_manager_team_path = "";
    }
    public static class CollabFxmlViews{
        public static final String fxml_collab_space_path = "";
        public static final String fxml_collab_department_path = "";
        public static final String fxml_collab_document_path ="" ;
        public static final String fxml_collab_infos_path = "";
        public static final String fxml_collab_request_path ="" ;
        public static final String fxml_collab_team_path = "";
        public static final String fxml_collab_teamplan_path = "";
        public static final String fxml_collab_training_path ="" ;
    }

    public static class SigninFXMLViews {
        public static final String fxml_login_path = "/com/innovhr/innovhrapp/view/signin/login-view.fxml";
        public static final String fxml_userbar_path = "";
    }

    public static class SharedFXMLViews {
        public static final String fxml_continue_as_path ="/com/innovhr/innovhrapp/view/shared/continue-as.fxml" ;
    }

}
