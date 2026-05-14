package ae.okhaz.boss.rests.Response;

/**
 * Created by Avinash on 18,December,2020
 */
public class ResponseCheckStatus {
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
        return "ResponseCheckStatus{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", result=" + result +
                '}';
    }

    public class Result
    {
       boolean onlineStatus;
       String  aid;

        public boolean isOnlineStatus() {
            return onlineStatus;
        }

        public String getAid() {
            return aid;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "onlineStatus=" + onlineStatus +
                    ", aid='" + aid + '\'' +
                    '}';
        }
    }
}
