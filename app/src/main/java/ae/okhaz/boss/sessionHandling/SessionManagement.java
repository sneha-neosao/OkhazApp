package ae.okhaz.boss.sessionHandling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import ae.okhaz.boss.Activitys.LoginActivity;
import ae.okhaz.boss.Activitys.SplashActivity;

import java.util.HashMap;


/**
 * Created by Akshay on 2/15/2017.
 */

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "qtp_del_sf";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String IS_REQUEST="isrequest";
    public static final String KEY_CODE = "empCode";
    public static final String KEY_ID = "id";
    public static final String KEY_USER_TYPE = "userType";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_STOCK_VIEW = "stockView";
    public static final String KEY_STOCK_SALES = "stockSales";
    public static final String KEY_COST_VIEW = "costView";
    public static final String KEY_GRAPH_VIEW = "graphView";
    public static final String KEY_UNDERCOST_SALE ="undercostsale";
    public static final String KEY_BRANCH_ID ="branchid";
    public static final String KEY_LOIGIN_STATUS ="loginstatus";
    public static final String KEY_DELIVERY_MAN ="deliveryMan";
    public static final String KEY_EMAIL ="emailID";
    public static final String KEY_SELECTED_TAB ="selected";
    public static final String KEY_DELIVERY_SUPPLIER_ID ="supplierID";
    public static final String KEY_AID ="Aid";

    public static final String KEY_All_FILTER_STATUS ="allstatus";
    public static final String KEY_All_FILTER_START_DATE ="allstartDate";
    public static final String KEY_All_FILTER_END_DATE ="allendDate";
    public static final String KEY_All_FILTER_ORDER_ID ="allorderId";
    public static final String KEY_All_SORT_ID ="allsortId";

    public static final String KEY_PLACED_FILTER_STATUS ="placedstatus";
    public static final String KEY_PLACED_FILTER_START_DATE ="placedstartDate";
    public static final String KEY_PLACED_FILTER_END_DATE ="placedendDate";
    public static final String KEY_PLACED_FILTER_ORDER_ID ="placedorderId";
    public static final String KEY_PLACED_SORT_ID ="placedsortId";


    public static final String KEY_PENDING_FILTER_STATUS ="pendingstatus";
    public static final String KEY_PENDING_FILTER_START_DATE ="pendingstartDate";
    public static final String KEY_PENDING_FILTER_END_DATE ="pendingendDate";
    public static final String KEY_PENDING_FILTER_ORDER_ID ="pendingorderId";
    public static final String KEY_PENDING_SORT_ID ="pendingsortId";

    public static final String KEY_READYPICKUP_FILTER_STATUS ="readystatus";
    public static final String KEY_READYPICKUP_FILTER_START_DATE ="readystartDate";
    public static final String KEY_READYPICKUP_FILTER_END_DATE ="readyendDate";
    public static final String KEY_READYPICKUP_FILTER_ORDER_ID ="readyorderId";
    public static final String KEY_READYPICKUP_SORT_ID ="readysortId";

    public static final String KEY_ACCEPTED_FILTER_STATUS ="acceptedstatus";
    public static final String KEY_ACCEPTED_FILTER_START_DATE ="acceptedstartDate";
    public static final String KEY_ACCEPTED_FILTER_END_DATE ="acceptedendDate";
    public static final String KEY_ACCEPTED_FILTER_ORDER_ID ="acceptedorderId";
    public static final String KEY_ACCEPTED_SORT_ID ="acceptedsortId";


    public static final String KEY_PROCESSING_FILTER_STATUS ="processingstatus";
    public static final String KEY_PROCESSING_FILTER_START_DATE ="processingstartDate";
    public static final String KEY_PROCESSING_FILTER_END_DATE ="processingendDate";
    public static final String KEY_PROCESSING_FILTER_ORDER_ID ="processingorderId";
    public static final String KEY_PROCESSING_SORT_ID ="processingsortId";


    public static final String KEY_ONTHEWAY_FILTER_STATUS ="onthewaystatus";
    public static final String KEY_ONTHEWAY_FILTER_START_DATE ="onthewaystartDate";
    public static final String KEY_ONTHEWAY_FILTER_END_DATE ="onthewayendDate";
    public static final String KEY_ONTHEWAY_FILTER_ORDER_ID ="onthewayorderId";
    public static final String KEY_ONTHEWAY_SORT_ID ="onthewaysortId";


    public static final String KEY_DELIVERED_FILTER_STATUS ="deliveredstatus";
    public static final String KEY_DELIVERED_FILTER_START_DATE ="deliveredstartDate";
    public static final String KEY_DELIVERED_FILTER_END_DATE ="deliveredendDate";
    public static final String KEY_DELIVERED_FILTER_ORDER_ID ="deliveredorderId";
    public static final String KEY_DELIVERED_SORT_ID ="deliveredsortId";


    public static final String KEY_CANCELED_FILTER_STATUS ="cancelledstatus";
    public static final String KEY_CANCELED_FILTER_START_DATE ="cancelledstartDate";
    public static final String KEY_CANCELED_FILTER_END_DATE ="cancelledendDate";
    public static final String KEY_CANCELED_FILTER_ORDER_ID ="cancelledorderId";
    public static final String KEY_CANCELED_SORT_ID ="cancelledsortId";

    public static final String KEY_SUPPLIER_ID ="supplierID";
    public static final String KEY_isPaymentDetailSection_On_OD_Available4Supplier ="isPaymentDetailSection";
    public static final String KEY_customerDetailSection_On_OD_Available4Supplier ="customerDetailSection";
    public static final String KEY_orderSummarySection_On_OD_Available4Supplier ="orderSummarySection";


    private static SessionManagement sharedPreferences;


    public static SessionManagement getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences=new SessionManagement(context);
        }
        return sharedPreferences;
    }

    // Constructor
    SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String userName,String ownerID,String id,String userType,String stockView,
                                   String stockSales , String costView, String graphView, String undercostsale,
                                   String branchid, String empCode, String loginstatus,String deliveryman,String email){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_CODE, empCode);
        editor.putString(KEY_DELIVERY_SUPPLIER_ID, empCode);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_STOCK_VIEW, stockView);
        editor.putString(KEY_STOCK_SALES, stockSales);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_COST_VIEW, costView);
        editor.putString(KEY_GRAPH_VIEW, graphView);
        editor.putString(KEY_UNDERCOST_SALE, undercostsale);
        editor.putString(KEY_BRANCH_ID, branchid);
        editor.putString(KEY_LOIGIN_STATUS, loginstatus);
        editor.putString(KEY_DELIVERY_MAN, deliveryman);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public void createSupplierSession(String id,String key1,String key2,String key3){
        editor.putString(KEY_SUPPLIER_ID,id);
        editor.putString(KEY_customerDetailSection_On_OD_Available4Supplier,key2);
        editor.putString(KEY_orderSummarySection_On_OD_Available4Supplier,key3);
        editor.putString(KEY_isPaymentDetailSection_On_OD_Available4Supplier,key1);
        editor.commit();
    }

    public void UpdateAllFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_All_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_All_FILTER_START_DATE, startdate);
        editor.putString(KEY_All_FILTER_END_DATE, ednDate);
        editor.putString(KEY_All_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdateAllSort(String orderId){

        editor.putString(KEY_All_SORT_ID, orderId);

        editor.commit();
    }

    public void UpdatePlacedFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_PLACED_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_PLACED_FILTER_START_DATE, startdate);
        editor.putString(KEY_PLACED_FILTER_END_DATE, ednDate);
        editor.putString(KEY_PLACED_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdateReadyPickFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_READYPICKUP_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_READYPICKUP_FILTER_START_DATE, startdate);
        editor.putString(KEY_READYPICKUP_FILTER_END_DATE, ednDate);
        editor.putString(KEY_READYPICKUP_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdateAcceptedFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_ACCEPTED_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_ACCEPTED_FILTER_START_DATE, startdate);
        editor.putString(KEY_ACCEPTED_FILTER_END_DATE, ednDate);
        editor.putString(KEY_ACCEPTED_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdatePendingFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_PENDING_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_PENDING_FILTER_START_DATE, startdate);
        editor.putString(KEY_PENDING_FILTER_END_DATE, ednDate);
        editor.putString(KEY_PENDING_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdatePendingSort(String orderId){

        editor.putString(KEY_PENDING_SORT_ID, orderId);


        editor.commit();
    }

    public void UpdateProcessingFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_PROCESSING_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_PROCESSING_FILTER_START_DATE, startdate);
        editor.putString(KEY_PROCESSING_FILTER_END_DATE, ednDate);
        editor.putString(KEY_PROCESSING_FILTER_STATUS, status);

        editor.commit();
    }

    public void UpdateProcessingSort(String orderId){

        editor.putString(KEY_PROCESSING_SORT_ID, orderId);


        editor.commit();
    }

    public void UpdateOntheWayFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_ONTHEWAY_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_ONTHEWAY_FILTER_START_DATE, startdate);
        editor.putString(KEY_ONTHEWAY_FILTER_END_DATE, ednDate);
        editor.putString(KEY_ONTHEWAY_FILTER_STATUS, status);


        editor.commit();
    }

    public void UpdateOntheWaySort(String orderId){

        editor.putString(KEY_ONTHEWAY_SORT_ID, orderId);


        editor.commit();
    }

    public void UpdateDeliveredFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_DELIVERED_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_DELIVERED_FILTER_START_DATE, startdate);
        editor.putString(KEY_DELIVERED_FILTER_END_DATE, ednDate);
        editor.putString(KEY_DELIVERED_FILTER_STATUS, status);



        editor.commit();
    }

    public void UpdateDeliveredSort(String orderId){

        editor.putString(KEY_DELIVERED_SORT_ID, orderId);



        editor.commit();
    }

    public void UpdateCancelledFilter(String orderId,String startdate, String ednDate , String status){

        editor.putString(KEY_CANCELED_FILTER_ORDER_ID, orderId);
        editor.putString(KEY_CANCELED_FILTER_START_DATE, startdate);
        editor.putString(KEY_CANCELED_FILTER_END_DATE, ednDate);
        editor.putString(KEY_CANCELED_FILTER_STATUS, status);



        editor.commit();
    }

    public void UpdateCancelSort(String orderId){

        editor.putString(KEY_CANCELED_SORT_ID, orderId);


        editor.commit();
    }


    public HashMap<String, String> getAllFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_All_FILTER_STATUS, pref.getString(KEY_All_FILTER_STATUS, ""));
        user.put(KEY_All_FILTER_START_DATE, pref.getString(KEY_All_FILTER_START_DATE, ""));
        user.put(KEY_All_FILTER_END_DATE, pref.getString(KEY_All_FILTER_END_DATE, ""));
        user.put(KEY_All_FILTER_ORDER_ID, pref.getString(KEY_All_FILTER_ORDER_ID, ""));
        user.put(KEY_All_SORT_ID,pref.getString(KEY_All_SORT_ID,""));


        return user;
    }


    public HashMap<String, String> getPendingFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_PENDING_FILTER_STATUS, pref.getString(KEY_PENDING_FILTER_STATUS, ""));
        user.put(KEY_PENDING_FILTER_START_DATE, pref.getString(KEY_PENDING_FILTER_START_DATE, ""));
        user.put(KEY_PENDING_FILTER_END_DATE, pref.getString(KEY_PENDING_FILTER_END_DATE, ""));
        user.put(KEY_PENDING_FILTER_ORDER_ID, pref.getString(KEY_PENDING_FILTER_ORDER_ID, ""));
        user.put(KEY_PENDING_SORT_ID,pref.getString(KEY_PENDING_SORT_ID,""));


        return user;
    }

    public HashMap<String, String> getPlacedFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_PLACED_FILTER_STATUS, pref.getString(KEY_PLACED_FILTER_STATUS, ""));
        user.put(KEY_PLACED_FILTER_START_DATE, pref.getString(KEY_PLACED_FILTER_START_DATE, ""));
        user.put(KEY_PLACED_FILTER_END_DATE, pref.getString(KEY_PLACED_FILTER_END_DATE, ""));
        user.put(KEY_PLACED_FILTER_ORDER_ID, pref.getString(KEY_PLACED_FILTER_ORDER_ID, ""));
        user.put(KEY_PLACED_SORT_ID,pref.getString(KEY_PLACED_SORT_ID,""));


        return user;
    }

    public HashMap<String, String> getAcceptedFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ACCEPTED_FILTER_STATUS, pref.getString(KEY_ACCEPTED_FILTER_STATUS, ""));
        user.put(KEY_ACCEPTED_FILTER_START_DATE, pref.getString(KEY_ACCEPTED_FILTER_START_DATE, ""));
        user.put(KEY_ACCEPTED_FILTER_END_DATE, pref.getString(KEY_ACCEPTED_FILTER_END_DATE, ""));
        user.put(KEY_ACCEPTED_FILTER_ORDER_ID, pref.getString(KEY_ACCEPTED_FILTER_ORDER_ID, ""));
        user.put(KEY_ACCEPTED_SORT_ID,pref.getString(KEY_ACCEPTED_SORT_ID,""));


        return user;
    }

    public HashMap<String, String> getReadyPickupFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_READYPICKUP_FILTER_STATUS, pref.getString(KEY_READYPICKUP_FILTER_STATUS, ""));
        user.put(KEY_READYPICKUP_FILTER_START_DATE, pref.getString(KEY_READYPICKUP_FILTER_START_DATE, ""));
        user.put(KEY_READYPICKUP_FILTER_END_DATE, pref.getString(KEY_READYPICKUP_FILTER_END_DATE, ""));
        user.put(KEY_READYPICKUP_FILTER_ORDER_ID, pref.getString(KEY_READYPICKUP_FILTER_ORDER_ID, ""));
        user.put(KEY_READYPICKUP_SORT_ID,pref.getString(KEY_READYPICKUP_SORT_ID,""));


        return user;
    }



    public HashMap<String, String> getProcessingFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_PROCESSING_FILTER_STATUS, pref.getString(KEY_PROCESSING_FILTER_STATUS, ""));
        user.put(KEY_PROCESSING_FILTER_START_DATE, pref.getString(KEY_PROCESSING_FILTER_START_DATE, ""));
        user.put(KEY_PROCESSING_FILTER_END_DATE, pref.getString(KEY_PROCESSING_FILTER_END_DATE, ""));
        user.put(KEY_PROCESSING_FILTER_ORDER_ID, pref.getString(KEY_PROCESSING_FILTER_ORDER_ID, ""));
        user.put(KEY_PROCESSING_SORT_ID,pref.getString(KEY_PROCESSING_SORT_ID,""));


        return user;
    }


    public HashMap<String, String> getOntheWayFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ONTHEWAY_FILTER_STATUS, pref.getString(KEY_ONTHEWAY_FILTER_STATUS, ""));
        user.put(KEY_ONTHEWAY_FILTER_START_DATE, pref.getString(KEY_ONTHEWAY_FILTER_START_DATE, ""));
        user.put(KEY_ONTHEWAY_FILTER_END_DATE, pref.getString(KEY_ONTHEWAY_FILTER_END_DATE, ""));
        user.put(KEY_ONTHEWAY_FILTER_ORDER_ID, pref.getString(KEY_ONTHEWAY_FILTER_ORDER_ID, ""));
        user.put(KEY_ONTHEWAY_SORT_ID,pref.getString(KEY_ONTHEWAY_SORT_ID,""));


        return user;
    }

    public HashMap<String, String> getDeliveredFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_DELIVERED_FILTER_STATUS, pref.getString(KEY_DELIVERED_FILTER_STATUS, ""));
        user.put(KEY_DELIVERED_FILTER_START_DATE, pref.getString(KEY_DELIVERED_FILTER_START_DATE, ""));
        user.put(KEY_DELIVERED_FILTER_END_DATE, pref.getString(KEY_DELIVERED_FILTER_END_DATE, ""));
        user.put(KEY_DELIVERED_FILTER_ORDER_ID, pref.getString(KEY_DELIVERED_FILTER_ORDER_ID, ""));
        user.put(KEY_DELIVERED_SORT_ID,pref.getString(KEY_DELIVERED_SORT_ID,""));


        return user;
    }

    public HashMap<String, String> getCancelledFilters()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_CANCELED_FILTER_STATUS, pref.getString(KEY_CANCELED_FILTER_STATUS, ""));
        user.put(KEY_CANCELED_FILTER_START_DATE, pref.getString(KEY_CANCELED_FILTER_START_DATE, ""));
        user.put(KEY_CANCELED_FILTER_END_DATE, pref.getString(KEY_CANCELED_FILTER_END_DATE, ""));
        user.put(KEY_CANCELED_FILTER_ORDER_ID, pref.getString(KEY_CANCELED_FILTER_ORDER_ID, ""));
        user.put(KEY_CANCELED_SORT_ID,pref.getString(KEY_CANCELED_SORT_ID,""));


        return user;
    }

    public void ClearSortandFilter()
    {
        editor.putString(KEY_All_FILTER_ORDER_ID, "");
        editor.putString(KEY_All_FILTER_START_DATE, "");
        editor.putString(KEY_All_FILTER_END_DATE, "");
        editor.putString(KEY_All_FILTER_STATUS, "");
        editor.putString(KEY_All_SORT_ID, "");
        editor.putString(KEY_PENDING_SORT_ID, "");
        editor.putString(KEY_PENDING_FILTER_ORDER_ID, "");
        editor.putString(KEY_PENDING_FILTER_START_DATE, "");
        editor.putString(KEY_PENDING_FILTER_END_DATE, "");
        editor.putString(KEY_PENDING_FILTER_STATUS, "");

        editor.putString(KEY_CANCELED_SORT_ID, "");
        editor.putString(KEY_CANCELED_FILTER_ORDER_ID, "");
        editor.putString(KEY_CANCELED_FILTER_START_DATE, "");
        editor.putString(KEY_CANCELED_FILTER_END_DATE, "");
        editor.putString(KEY_CANCELED_FILTER_STATUS, "");

        editor.putString(KEY_DELIVERED_SORT_ID, "");

        editor.putString(KEY_DELIVERED_FILTER_ORDER_ID, "");
        editor.putString(KEY_DELIVERED_FILTER_START_DATE, "");
        editor.putString(KEY_DELIVERED_FILTER_END_DATE, "");
        editor.putString(KEY_DELIVERED_FILTER_STATUS, "");

        editor.putString(KEY_ONTHEWAY_SORT_ID, "");

        editor.putString(KEY_ONTHEWAY_FILTER_ORDER_ID, "");
        editor.putString(KEY_ONTHEWAY_FILTER_START_DATE, "");
        editor.putString(KEY_ONTHEWAY_FILTER_END_DATE, "");
        editor.putString(KEY_ONTHEWAY_FILTER_STATUS, "");

        editor.putString(KEY_PROCESSING_SORT_ID, "");

        editor.putString(KEY_PROCESSING_FILTER_ORDER_ID, "");
        editor.putString(KEY_PROCESSING_FILTER_START_DATE, "");
        editor.putString(KEY_PROCESSING_FILTER_END_DATE, "");
        editor.putString(KEY_PROCESSING_FILTER_STATUS, "");

        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_customerDetailSection_On_OD_Available4Supplier,pref.getString(KEY_customerDetailSection_On_OD_Available4Supplier,""));
        user.put(KEY_orderSummarySection_On_OD_Available4Supplier,pref.getString(KEY_orderSummarySection_On_OD_Available4Supplier,""));
        user.put(KEY_isPaymentDetailSection_On_OD_Available4Supplier,pref.getString(KEY_isPaymentDetailSection_On_OD_Available4Supplier,""));

        user.put(KEY_STOCK_VIEW, pref.getString(KEY_STOCK_VIEW, ""));
        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, ""));
        user.put(KEY_CODE, pref.getString(KEY_CODE, ""));
        user.put(KEY_ID, pref.getString(KEY_ID, ""));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_STOCK_SALES, pref.getString(KEY_STOCK_SALES, ""));
        user.put(KEY_COST_VIEW,pref.getString(KEY_COST_VIEW,""));
        user.put(KEY_GRAPH_VIEW,pref.getString(KEY_GRAPH_VIEW,""));
        user.put(KEY_UNDERCOST_SALE,pref.getString(KEY_UNDERCOST_SALE,""));
        user.put(KEY_BRANCH_ID,pref.getString(KEY_BRANCH_ID,""));
        user.put(KEY_LOIGIN_STATUS,pref.getString(KEY_LOIGIN_STATUS,""));
        user.put(KEY_DELIVERY_MAN,pref.getString(KEY_DELIVERY_MAN,""));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,""));
        user.put(KEY_SELECTED_TAB,pref.getString(KEY_SELECTED_TAB,""));
        user.put(KEY_AID,pref.getString(KEY_AID,""));


        return user;
    }

    public void updateLoginStatus(String loginstatus)
    {
        editor.putString(KEY_LOIGIN_STATUS, loginstatus);

        editor.commit();
    }

    public void updateTABStatus(String loginstatus)
    {
        editor.putString(KEY_SELECTED_TAB, loginstatus);

        editor.commit();
    }

    public void updateAID(String loginstatus)
    {
        editor.putString(KEY_AID, loginstatus);

        editor.commit();
    }


    public void checkLogin()
    {
        // Check login status
        if(!this.isLoggedIn())
        {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     * */

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, SplashActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}