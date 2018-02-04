package com.app.extras;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.app.chilindo.R;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Created by Yash on 4/2/18.
 */

public class Utility
{
    /* @method getCurrentDate
     * @param String value of date in "yyyy-MM-dd" Format
     * @return String format of current date Format "MMM dd" using SimpleDateFormat.*/
    public static String convertCurrentDateFormat(String date)
    {
        String datePattern = "yyyy-MM-dd";
        String convertPattern = "MMM dd";

        SimpleDateFormat spf = new SimpleDateFormat(datePattern,Locale.US);
        Date newDate= null;
        try
        {
            newDate = spf.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat(convertPattern,Locale.US);
        date = spf.format(newDate);
        System.out.println(date);

        return spf.format(newDate);
    }

    /* @method getCurrDateFormat
     * @return String format of current date Format "yyyy-MM-dd" using SimpleDateFormat.*/
    public static String getCurrDateFormat()
    {
        Calendar c = Calendar.getInstance();

        String datePattern = "yyyy-MM-dd";

        SimpleDateFormat df = new SimpleDateFormat(datePattern,Locale.US);

        return df.format(c.getTime());
    }

    /* @method convertKelvinToCelsius
    * @param float value of temperature in Kelvin from the server
    * @return String format of temperature in using DecimalFormat.*/
    public static String convertKelvinToCelsius(float temp)
    {
        DecimalFormat df = new DecimalFormat("###");
        // Get the input for the temperature
        return df.format(temp - 273.15F);
    }

    // 24 Hours converter
    public static String convertTimeFormat(String _24HourTime)
    {
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss",Locale.US); //HH for hour of the day (0 - 23)
        Date date = null;
        try
        {
            date = f1.parse(_24HourTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h a",Locale.US);
        return f2.format(date).toLowerCase(); // "12 am"
    }

    /* @method getCurrTimeInto24Format
     * @return String format of current time in 24 Hour Format using SimpleDateFormat.*/
    public static String getCurrTimeInto24Format()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);

        return sdf.format(new Date());
    }

    private static boolean isBetween(int x, int lower, int upper)
    {
        return lower <= x && x <= upper;
    }

    public static int timeRange(int currentTime)
    {
        int number = 0;

        if (isBetween(currentTime, 0, 3))
        {
           number = 3;
        }
        else if (Utility.isBetween(currentTime, 4, 6))
        {
            number = 6;
        }
        else if (Utility.isBetween(currentTime, 5, 9))
        {
            number = 9;
        }
        else if (Utility.isBetween(currentTime, 10, 12))
        {
            number = 12;
        }
        else if (Utility.isBetween(currentTime, 13, 15))
        {
            number = 15;
        }
        else if (Utility.isBetween(currentTime, 16, 18))
        {
            number = 18;
        }
        else if (Utility.isBetween(currentTime, 19, 21))
        {
            number = 21;
        }
        return  number;
    }

    /* @method getDayOfWeek
     * @param String value of datetxt from the server
     * @return String format of day of the week after converting date using SimpleDateFormat.*/
    public static String getDayOfWeek(String date)
    {
        String datePattern = "yyyy-MM-dd";

        SimpleDateFormat format1=new SimpleDateFormat(datePattern,Locale.US);
        Date dt1= null;
        try
        {
            dt1 = format1.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        DateFormat format2 = new SimpleDateFormat("EE",Locale.US);

        return format2.format(dt1);
    }

    // showDialog for Alerting the user for providing access to gps
    // To ask user whether the user is sure that he want to logout.
    public static void showDialog(final Context context, String message,final AlertDialogCallback<String> callback)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.Yes),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.alertDialogCallback( context.getResources().getString(R.string.Yes));
                    }
                });
        builder.setNegativeButton(context.getResources().getString(R.string.No),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.alertDialogCallback( context.getResources().getString(R.string.No));
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
