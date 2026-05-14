package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

public class ProductListViewHolder  extends RecyclerView.ViewHolder {
    public TextView txt_subOrder_no,txt_subOrder_name,txt_subOrder_Tele,txt_subOrder_status;
    public RecyclerView rv_items;
    public ImageView img_map,img_suborder_status;

    public TextView txtSupplierAddress,txtSupplierContactNo,tvEmailSupplier,txtSupplierName;
    public TextView txt_subOrder_bankName,txt_subOrder_accNum,txt_subOrder_accBranch,txt_subOrder_ifsc,txt_subOrder_bankAddress;
    public LinearLayout ll_subOrder_detail;
    public ImageView img_subOrder_downArrow,img_subOrder_upArrow;
    public TextView btn_more,txt_suborder_total;

    public LinearLayout ll_email_bank_supplier_info,ll_supplier_contact_no,ll_bank_details_supplier,ll_address_supplier,ll_email_supplier,ll_supplier_name;
    public View ll_view_supplier_info;

    public CardView cv_supplier_email,cv_supplier_contact_no;

    public ProductListViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_subOrder_no=itemView.findViewById(R.id.txt_subOrder_no);
        txt_subOrder_status=itemView.findViewById(R.id.txt_subOrder_status);
        txtSupplierAddress=itemView.findViewById(R.id.txtSupplierAddress);
        txtSupplierContactNo=itemView.findViewById(R.id.txtSupplierContactNo);
        tvEmailSupplier=itemView.findViewById(R.id.tvEmailSupplier);
        txtSupplierName=itemView.findViewById(R.id.txtSupplierName);
        txt_suborder_total=itemView.findViewById(R.id.txt_suborder_total);


        txt_subOrder_name=itemView.findViewById(R.id.txt_subOrder_name);
        txt_subOrder_Tele=itemView.findViewById(R.id.txt_subOrder_Tele);
        txt_subOrder_bankName=itemView.findViewById(R.id.txt_subOrder_bankName);
        txt_subOrder_accNum=itemView.findViewById(R.id.txt_subOrder_accNum);
        txt_subOrder_accBranch=itemView.findViewById(R.id.txt_subOrder_accBranch);
        txt_subOrder_ifsc=itemView.findViewById(R.id.txt_subOrder_ifsc);
        txt_subOrder_bankAddress=itemView.findViewById(R.id.txt_subOrder_bankAddress);

        btn_more=itemView.findViewById(R.id.btn_more);
        ll_view_supplier_info=itemView.findViewById(R.id.ll_view_supplier_info);
        ll_email_bank_supplier_info=itemView.findViewById(R.id.ll_email_bank_supplier_info);
        ll_supplier_contact_no=itemView.findViewById(R.id.ll_supplier_contact_no);
        ll_bank_details_supplier=itemView.findViewById(R.id.ll_bank_details_supplier);
        ll_address_supplier=itemView.findViewById(R.id.ll_address_supplier);
        ll_email_supplier=itemView.findViewById(R.id.ll_email_supplier);
        ll_supplier_name=itemView.findViewById(R.id.ll_supplier_name);

        rv_items=itemView.findViewById(R.id.rv_items);
        ll_subOrder_detail=itemView.findViewById(R.id.ll_subOrder_detail);
        img_subOrder_downArrow=itemView.findViewById(R.id.img_subOrder_downArrow);
        img_subOrder_upArrow=itemView.findViewById(R.id.img_subOrder_upArrow);
        img_map=itemView.findViewById(R.id.img_map);
        img_suborder_status=itemView.findViewById(R.id.img_suborder_status);

        cv_supplier_email=itemView.findViewById(R.id.cv_supplier_email);
        cv_supplier_contact_no=itemView.findViewById(R.id.cv_supplier_contact_no);

    }
}
