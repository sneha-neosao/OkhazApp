package ae.okhaz.boss.Model;

/**
 * Created by Avinash on 01,December,2020
 */
public class Products {

    boolean flag;
    String
    itemId,itemName,uom,orderItemId,orderId,itemSellingprice,perUnitPrice,quantity,barcode,image,suppName,itemScanStatus;

    public String getItemScanStatus() {
        return itemScanStatus;
    }

    public void setItemScanStatus(String itemScanStatus) {
        this.itemScanStatus = itemScanStatus;
    }

    public String getSuppName() {
        return suppName;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUom() {
        return uom;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemSellingprice() {
        return itemSellingprice;
    }

    public String getPerUnitPrice() {
        return perUnitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Products{" +
                "flag=" + flag +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", uom='" + uom + '\'' +
                ", orderItemId='" + orderItemId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", itemSellingprice='" + itemSellingprice + '\'' +
                ", perUnitPrice='" + perUnitPrice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", barcode='" + barcode + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
