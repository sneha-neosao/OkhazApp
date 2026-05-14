package ae.okhaz.boss.rests.Response;

/**
 * Created by Akshay on 30,May,2021
 */
public class ResponseDashboard {
    boolean status;
    String message;
    Result result;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result
    {
        String lifetimeSale,avgMonthlySale,todaySale,monthlySale,yearlySale,weeklySale;

        public String getLifetimeSale() {
            return lifetimeSale;
        }

        public void setLifetimeSale(String lifetimeSale) {
            this.lifetimeSale = lifetimeSale;
        }

        public String getAvgMonthlySale() {
            return avgMonthlySale;
        }

        public void setAvgMonthlySale(String avgMonthlySale) {
            this.avgMonthlySale = avgMonthlySale;
        }

        public String getTodaySale() {
            return todaySale;
        }

        public void setTodaySale(String todaySale) {
            this.todaySale = todaySale;
        }

        public String getMonthlySale() {
            return monthlySale;
        }

        public void setMonthlySale(String monthlySale) {
            this.monthlySale = monthlySale;
        }

        public String getYearlySale() {
            return yearlySale;
        }

        public void setYearlySale(String yearlySale) {
            this.yearlySale = yearlySale;
        }

        public String getWeeklySale() {
            return weeklySale;
        }

        public void setWeeklySale(String weeklySale) {
            this.weeklySale = weeklySale;
        }
    }


}
