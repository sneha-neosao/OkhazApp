package ae.okhaz.boss.Model;

/**
 * Created by Avinash on 27,November,2020
 */
public class Order {


    String     custId,deliveryManName;

    public String getOrderTransactionType() {
        return orderTransactionType;
    }

    String orderTransactionType;
    String sessionId;
    String token;
    String itemDiscount;
    String tax;
    String shipping;
    String total;
    String promo;
    String discount;
    String grandtotal;
    String firstName;
    String middleName;
    String mobile;
    String latitude;
    String longitude;
    String email;
    String addressLine1;
    String addressLine2;
    String city;
    String province;
    String country;
    String createAt;
    String updateAt;
    String contect;
    String deviceName;
    String deliveryMan;
    String branchCode;
    String orderStatus1;
    String orderStatusCode;
    String orderDate;
    String orderTime;
    Boolean isPaymentSuccessful;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    String supplierName;

    public String getPrimaryOrderNumber() {
        return primaryOrderNumber;
    }

    public void setPrimaryOrderNumber(String primaryOrderNumber) {
        this.primaryOrderNumber = primaryOrderNumber;
    }

    String primaryOrderNumber;


    public String getOrderRef() {
        return orderRef;
    }

    String orderRef;

    int orderID,orderStatus,itemCount;
    double subTotal;

    public String getDeliveryManName() {
        return deliveryManName;
    }

    public void setDeliveryManName(String deliveryManName) {
        this.deliveryManName = deliveryManName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getOrderID() {
        return orderID;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public String getCustId() {
        return custId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getToken() {
        return token;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public String getTax() {
        return tax;
    }

    public String getShipping() {
        return shipping;
    }

    public String getTotal() {
        return total;
    }

    public String getPromo() {
        return promo;
    }

    public String getDiscount() {
        return discount;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public String getContect() {
        return contect;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getOrderStatus1() {
        return orderStatus1;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public Boolean getIsPaymentSuccessful() {
        return isPaymentSuccessful;
    }
}
