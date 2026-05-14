package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.Comments;
import ae.okhaz.boss.Model.Order;
import ae.okhaz.boss.Model.ProductListModel;
import ae.okhaz.boss.Model.Products;

import java.util.ArrayList;

/**
 * Created by Avinash on 16,December,2020
 */
public class ResponseOrderDetails {
    String message;
    boolean status;
    Result result;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ResponseOrderDetails{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", result=" + result +
                '}';
    }

    public class Result
    {
        ArrayList<Products> orderItems;
        public  ArrayList<ProductListModel> subOrders;
        ArrayList<Comments> orderComments;

        private Order mainOrder;

        public ArrayList<Products> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(ArrayList<Products> orderItems) {
            this.orderItems = orderItems;
        }

        public ArrayList<Comments> getOrderComments() {
            return orderComments;
        }

        public void setOrderComments(ArrayList<Comments> orderComments) {
            this.orderComments = orderComments;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "orderItems=" + orderItems +
                    ", orderComments=" + orderComments +
                    '}';
        }

        public ArrayList<ProductListModel> getSubOrders() {
            return subOrders;
        }

        public void setSubOrders(ArrayList<ProductListModel> subOrders) {
            this.subOrders = subOrders;
        }

        public Order getOrder() {
            return mainOrder;
        }

        public void setOrder(Order order) {
            this.mainOrder = order;
        }
    }
}
