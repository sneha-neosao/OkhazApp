package ae.okhaz.boss.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView product_prices_tv, product_names_tv,product_barcode_tv,product_suplier_tv,product_qty_tv;
    public ImageView product_iv,barcode_iv,add_barcode_iv;
    public ImageView btn_add_barcode;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_iv = itemView.findViewById(R.id.product_iv);
        product_qty_tv = itemView.findViewById(R.id.product_qty_tv);
        product_prices_tv = itemView.findViewById(R.id.product_prices_tv);
        product_names_tv = itemView.findViewById(R.id.product_names_tv);
        product_barcode_tv = itemView.findViewById(R.id.product_barcode_tv);
        product_suplier_tv = itemView.findViewById(R.id.product_suplier_tv);
        barcode_iv = itemView.findViewById(R.id.barcode_iv);
        btn_add_barcode = itemView.findViewById(R.id.btn_add_barcode);
        add_barcode_iv = itemView.findViewById(R.id.add_barcode_iv);
    }
}
