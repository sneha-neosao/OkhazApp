package ae.okhaz.boss.Utils;

import android.text.TextUtils;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Avinash on 27,March,2020
 */
public class Validations {
    public static final String regPh="^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";
    public  static  final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String DOCUMENTS_DIR = "documents";
    static final String TAG = "Utilss";
    private static final boolean DEBUG = false;
    public static final String AUTHORITY =  "YOUR_AUTHORITY.provider";
    public static final String pan_pattern = "(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))";
    public static final String adhar_pattern = "(([0-9]{12}))";
    public static final String contact_pattern = "(([7-9]{1})([0-9]{9}))";
    public static final String gst_pattern ="^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
    public static final String doc_weight ="[0]{1}+\\.+[0-9]{3}";


    public static boolean isValidMobile(String phone) {
        Pattern p2= Pattern.compile(regPh);
        Matcher m2=p2.matcher(phone);
        return m2.find();
    }

    public static boolean requireValidate(EditText editText)        {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    public static boolean requireTILValidate(TextInputEditText edt)        {
        if (edt.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && email.matches(emailPattern);
    }

    public static boolean isValidContact(String contact)
    {
        return !TextUtils.isEmpty(contact) && contact.matches(contact_pattern);
    }


    public static boolean isValidString(String name)
    {
        return !TextUtils.isEmpty(name);
    }



}
