package ae.okhaz.boss.rests.Response;

/**
 * Created by Avinash on 08,September,2020
 */
public class ResponseLogin {
    boolean status;
    String message;
   Result[] result;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result[] getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }


    public class Result
    {
        String userName;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        String userType;
        String stockView;
        String stockSales;
        String costView;
        String graphView;
        String undercostsale;
        String branchid;
        String empCode;
        String loginstatus;
        String deliveryMan;
        String emailId;

        public String getOwnerID() {
            return ownerID;
        }

        public void setOwnerID(String ownerID) {
            this.ownerID = ownerID;
        }

        String ownerID;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        String id;


        public String getEmailId() {
            return emailId;
        }

        public String getDeliveryMan() {
            return deliveryMan;
        }

        public String getUserName() {
            return userName;
        }

        public String getStockView() {
            return stockView;
        }

        public String getStockSales() {
            return stockSales;
        }

        public String getCostView() {
            return costView;
        }

        public String getGraphView() {
            return graphView;
        }

        public String getUndercostsale() {
            return undercostsale;
        }

        public String getBranchid() {
            return branchid;
        }

        public String getEmpCode() {
            return empCode;
        }

        public String getLoginstatus() {
            return loginstatus;
        }
    }
}
