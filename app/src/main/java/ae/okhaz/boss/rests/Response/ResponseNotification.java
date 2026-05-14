package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.NotificationModel;

import java.util.ArrayList;

public class ResponseNotification
{

    private boolean status;
    private String message,totalRecords;

    private Result result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{

       private ArrayList<NotificationModel> notificationList;

        public ArrayList<NotificationModel> getNotificationList() {
            return notificationList;
        }

        public void setNotificationList(ArrayList<NotificationModel> notificationList) {
            this.notificationList = notificationList;
        }
    }


}
