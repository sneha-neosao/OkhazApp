package ae.okhaz.boss.Data;

import ae.okhaz.boss.rests.Response.ResponseDailySalesDashboard;

import java.util.ArrayList;

public class DashboardData {
   public String lifetimeSale,avgMonthlySale,todaySale,monthlySale,yearlySale,weeklySale;
   public ArrayList<ResponseDailySalesDashboard.Result> dailySaleList;
   private static final DashboardData ourInstance = new DashboardData();

   public static DashboardData getInstance() {
      return ourInstance;
   }

   private DashboardData() {
   }


   public int getHourlySaleByHr(int hour)
   {
      int returnVal=0;
      for (ResponseDailySalesDashboard.Result result:dailySaleList) {
         if(result.getHour()==hour)
         {
            returnVal=result.getSales();
            break;
         }
      }
      return returnVal;
   }

}
