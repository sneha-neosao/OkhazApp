package ae.okhaz.boss.rests.Response;

import ae.okhaz.boss.Model.DeliveryBoyModel;

public class ResponseDeliveryMan
{

    private boolean status;
    private String message;
    private DeliveryBoyModel result;

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

    public DeliveryBoyModel getResult() {
        return result;
    }

    public void setResult(DeliveryBoyModel result) {
        this.result = result;
    }
}
