package ae.okhaz.boss.rests.Response;

/**
 * Created by Avinash on 17,December,2020
 */
public class ResponseCommon {
    boolean status;
    String message;
    Result[] result;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public class Result
    {
        String Aid;

        public String getAid() {
            return Aid;
        }
    }
}
