package ae.okhaz.boss.rests.Response;

import java.util.ArrayList;

import ae.okhaz.boss.Model.ProductModel;

public class ResponseProductList
{

    private boolean status;
    private String message;
    private ArrayList<ProductModel> result;


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

    public ArrayList<ProductModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ProductModel> result) {
        this.result = result;
    }
}
