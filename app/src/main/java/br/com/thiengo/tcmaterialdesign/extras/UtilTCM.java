package br.com.thiengo.tcmaterialdesign.extras;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by viniciusthiengo on 5/25/15.
 */
public class UtilTCM {
    public static boolean verifyConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @SuppressLint("NewApi")
    public static String getEmailAccountManager(Context context){
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",Pattern.CASE_INSENSITIVE);

        if(context != null){
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(context).getAccounts();

            for(Account account : accounts){
                if(emailPattern.matcher(account.name).matches()) {
                    String aux = account.name;

                    if(pattern.matcher(aux).matches()){
                        return(aux);
                    }
                }
            }
        }
        return("");
    }
}
