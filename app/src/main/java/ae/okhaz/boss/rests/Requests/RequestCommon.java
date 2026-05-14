package ae.okhaz.boss.rests.Requests;

/**
 * Created by Avinash on 11,September,2020
 */
public class RequestCommon {
    String code,date,offset,productionlineCode,planCode,shiftCode,tillDate;


    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getTillDate() {
        return tillDate;
    }

    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getProductionlineCode() {
        return productionlineCode;
    }

    public void setProductionlineCode(String productionlineCode) {
        this.productionlineCode = productionlineCode;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RequestCommon{" +
                "code='" + code + '\'' +
                ", date='" + date + '\'' +
                ", offset='" + offset + '\'' +
                ", productionlineCode='" + productionlineCode + '\'' +
                ", planCode='" + planCode + '\'' +
                '}';
    }
}
