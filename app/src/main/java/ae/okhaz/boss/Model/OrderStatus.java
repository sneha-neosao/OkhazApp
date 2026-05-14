package ae.okhaz.boss.Model;

public class OrderStatus
{
    private String orderStatusID,orderStatusName,ordercount;

    public String getOrderStatusID() {
        return orderStatusID;
    }

    public void setOrderStatusID(String orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderCount() {
        return ordercount;
    }

    public void setOrderCount(String ordercount) {
        this.ordercount = ordercount;
    }
}
