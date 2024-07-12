package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.daos.HolidayDAO;
import com.innovhr.innovhrapp.daos.RequestDAO;
import com.innovhr.innovhrapp.daos.TeamDAO;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.Holiday;
import com.innovhr.innovhrapp.models.Team;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyTeamPlanController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private VBox holidayInfoBox;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;
    @FXML
    private Label monthYearLabel;

    private final String pageName = "Team Plan";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;
    private final Map<Employee, Color> employeeColorMap = new HashMap<>();

    private YearMonth currentMonth;

    public MyTeamPlanController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
        this.currentMonth = YearMonth.now();
    }

    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            showAlertError("Access Denied", "You do not have permission to view this page.");
            disableComponents();
        }
    }

    private void disableComponents() {
        calendarGrid.setVisible(false);
        holidayInfoBox.setVisible(false);
        prevMonthButton.setVisible(false);
        nextMonthButton.setVisible(false);
    }

    @FXML
    public void initialize() {
        prevMonthButton.setOnAction(e -> changeMonth(-1));
        nextMonthButton.setOnAction(e -> changeMonth(1));
        updateMonthYearLabel();
        loadTeamAndPopulateCalendar();
    }

    private void loadTeamAndPopulateCalendar() {
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();
        Team team = TeamDAO.findTeamById(loggedInUser.getEmployee().getTeam().getTeam_id());
        if (team != null) {
            populateCalendar(team);
        } else {
            showAlert("Team not found", "No team found for the current user.");
        }
    }

    private void changeMonth(int delta) {
        currentMonth = currentMonth.plusMonths(delta);
        updateMonthYearLabel();
        loadTeamAndPopulateCalendar();
    }

    private void updateMonthYearLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthYearLabel.setText(currentMonth.format(formatter));
    }

    private void populateCalendar(Team team) {
        LocalDate firstOfMonth = currentMonth.atDay(1);
        LocalDate lastOfMonth = currentMonth.atEndOfMonth();

        // Get holidays for the current month for the team
        List<Holiday> holidays = HolidayDAO.findHolidaysByTeamAndMonth(team, firstOfMonth, lastOfMonth);
        // Filter holidays to include only approved ones
        holidays = holidays.stream()
                .filter(holiday -> "APPROVED".equals(RequestDAO.findRequestsByHoliday(holiday).getFirst().getRequest_state()))
                .collect(Collectors.toList());

        // Populate calendar grid with days of the month horizontally
        calendarGrid.getChildren().clear();
        holidayInfoBox.getChildren().clear();

        // Set up the grid with day headers
        for (int day = 1; day <= lastOfMonth.getDayOfMonth(); day++) {
            LocalDate date = firstOfMonth.withDayOfMonth(day);
            Text dayText = new Text(String.valueOf(day));
            calendarGrid.add(dayText, day, 0);
        }

        // Group holidays by employee
        Map<Employee, List<Holiday>> employeeHolidaysMap = holidays.stream()
                .collect(Collectors.groupingBy(Holiday::getEmployee));

        int rowIndex = 1;
        for (Map.Entry<Employee, List<Holiday>> entry : employeeHolidaysMap.entrySet()) {
            Employee employee = entry.getKey();
            List<Holiday> employeeHolidays = entry.getValue();

            // Add employee name to the first column
            Text employeeName = new Text(employee.getEmp_fname() + " " + employee.getEmp_lname());
            calendarGrid.add(employeeName, 0, rowIndex);

            // Assign a color to each employee if not already assigned
            Color employeeColor = employeeColorMap.computeIfAbsent(employee, e -> getRandomColor());

            for (Holiday holiday : employeeHolidays) {
                LocalDate startDate = convertToLocalDate(holiday.getHoliday_start_date());
                LocalDate endDate = convertToLocalDate(holiday.getHoliday_end_date());

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    int dayIndex = date.getDayOfMonth();
                    Pane pane = new Pane();
                    pane.setPrefSize(40, 20);  // Adjust size as needed

                    pane.setStyle("-fx-background-color: " + toRGBCode(employeeColor) + ";");

                    String holidayInfo = holiday.getHoliday_type() + " (" + startDate + " to " + endDate + ")";
                    Tooltip tooltip = new Tooltip(employee.getEmp_fname() + " " + employee.getEmp_lname() + ": " + holidayInfo);
                    Tooltip.install(pane, tooltip);

                    calendarGrid.add(pane, dayIndex, rowIndex);
                }
            }
            rowIndex++;
        }
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Color getRandomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
