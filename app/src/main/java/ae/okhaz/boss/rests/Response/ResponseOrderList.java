package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.Counts;
import ae.okhaz.boss.Model.Order;

import java.util.ArrayList;

/**
 * Created by Avinash on 16,December,2020
 */
public class ResponseOrderList {
    boolean status;
    String message;
    Result result;
    Counts counts;


    public Counts getCounts() {
        return counts;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Result getResult() {
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

    public  class Result
    {
        ArrayList<Order> orderList;

        public ArrayList<Order> getOrderList() {
            return orderList;
        }
    }
}
