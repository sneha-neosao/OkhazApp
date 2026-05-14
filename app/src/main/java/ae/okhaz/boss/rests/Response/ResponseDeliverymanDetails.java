package ae.okhaz.boss.rests.Response;

/**
 * Created by Avinash on 17,December,2020
 */
public class ResponseDeliverymanDetails {
    boolean status;
    String message;
    Result[] result;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Result[] getResult() {
        return result;
    }

    public class Result{
        String id,userName,passWord,userType,stockView,stockSales,costView,graphView,undercostsale,branchid,empCode,loginstatus,emailId;

        public String getId() {
            return id;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassWord() {
            return passWord;
        }

        public String getUserType() {
            return userType;
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

        public String getEmailId() {
            return emailId;
        }



    }
}
