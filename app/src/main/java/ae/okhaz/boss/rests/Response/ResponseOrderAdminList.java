package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.CountOrder;
import ae.okhaz.boss.Model.Order;

import java.util.ArrayList;

public class ResponseOrderAdminList {

    boolean status;
    String message;
    ResponseOrderList.Result result;

    private ArrayList<CountOrder> counts;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ResponseOrderList.Result getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "ResposnseOrderList{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public ArrayList<CountOrder> getCounts() {
        return counts;
    }

    public void setCounts(ArrayList<CountOrder> counts) {
        this.counts = counts;
    }

    public  class Result
    {
        ArrayList<Order> orderList;

        public ArrayList<Order> getOrderList() {
            return orderList;
        }
    }
}
