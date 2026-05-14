package ae.okhaz.boss.Model;

/**
 * Created by Avinash on 31,December,2020
 */
public class Counts {
    String allCount,processingCount,pendingCount,onTheWayCount,deliveredCount,canceledCount;


    public String getAllCount() {
        return allCount;
    }

    public String getProcessingCount() {
        return processingCount;
    }

    public String getPendingCount() {
        return pendingCount;
    }

    public String getOnTheWayCount() {
        return onTheWayCount;
    }

    public String getDeliveredCount() {
        return deliveredCount;
    }

    public String getCanceledCount() {
        return canceledCount;
    }

    @Override
    public String toString() {
        return "Counts{" +
                "allCount='" + allCount + '\'' +
                ", processingCount='" + processingCount + '\'' +
                ", pendingCount='" + pendingCount + '\'' +
                ", onTheWayCount='" + onTheWayCount + '\'' +
                ", deliveredCount='" + deliveredCount + '\'' +
                ", canceledCount='" + canceledCount + '\'' +
                '}';
    }
}
