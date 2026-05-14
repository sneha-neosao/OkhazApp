package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import ae.okhaz.admin.R;

public class SupplierListHolder extends RecyclerView.ViewHolder{

   public ImageView img_supplier;
    public TextView txt_supplier_name,txt_supplier_address,txt_close_time,txt_supplierStatus;
    public SwitchMaterial switch_supplier;

    public SupplierListHolder(@NonNull View itemView) {
        super(itemView);

        switch_supplier=itemView.findViewById(R.id.switch_supplier);
        img_supplier=itemView.findViewById(R.id.img_supplier);
        txt_supplier_name=itemView.findViewById(R.id.txt_supplier_name);
        txt_supplier_address=itemView.findViewById(R.id.txt_supplier_address);
        txt_close_time=itemView.findViewById(R.id.txt_close_time);
        txt_supplierStatus=itemView.findViewById(R.id.txt_supplierStatus);


    }
}
