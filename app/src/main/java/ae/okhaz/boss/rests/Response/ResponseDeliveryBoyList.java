package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.DeliveryBoyModel;

import java.util.ArrayList;

public class ResponseDeliveryBoyList {

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    private int totalRecords;
    private boolean status;
    private String message;
    public Result result;


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

    public class Result {
        private ArrayList<DeliveryBoyModel> deliveryManList;

        public ArrayList<DeliveryBoyModel> getDeliveryManList() {
            return deliveryManList;
        }

        public void setDeliveryManList(ArrayList<DeliveryBoyModel> deliveryManList) {
            this.deliveryManList = deliveryManList;
        }
    }
}
