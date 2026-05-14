package ae.okhaz.boss.rests.Response;

import java.util.ArrayList;

import ae.okhaz.boss.Model.SliderModel;

public class ResponseHomeSlider
{

    private boolean status;
    private String message;
    private ArrayList<SliderModel> result;


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

    public ArrayList<SliderModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<SliderModel> result) {
        this.result = result;
    }
}
