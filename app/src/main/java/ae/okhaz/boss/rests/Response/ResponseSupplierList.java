package ae.okhaz.boss.rests.Response;

import java.util.ArrayList;

import ae.okhaz.boss.Model.SupplierModel;

public class ResponseSupplierList
{

    private boolean status;
    private Result result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result{

       private ArrayList<SupplierModel> supplierList;

        public ArrayList<SupplierModel> getSupplierList() {
            return supplierList;
        }

        public void setSupplierList(ArrayList<SupplierModel> supplierList) {
            this.supplierList = supplierList;
        }
    }
}
