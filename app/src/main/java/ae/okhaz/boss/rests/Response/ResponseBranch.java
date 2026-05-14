package ae.okhaz.boss.rests.Response;

import java.util.ArrayList;

import ae.okhaz.boss.Model.BranchModel;

public class ResponseBranch {

    private boolean status;
    private String message;
    private ArrayList<BranchModel> result;

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

    public ArrayList<BranchModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<BranchModel> result) {
        this.result = result;
    }
}
