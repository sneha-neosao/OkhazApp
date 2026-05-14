package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.OrderStatus;

import java.util.ArrayList;

public class ResponseOrderStatus
{
    private String status,message;

       private ArrayList<OrderStatus> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OrderStatus> getResult() {
        return result;
    }

    public void setResult(ArrayList<OrderStatus> result) {
        this.result = result;
    }
}
