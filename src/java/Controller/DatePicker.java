/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Admin
 */
@ManagedBean(name = "datepicker")
@ViewScoped
public class DatePicker implements Serializable{

    private String minDD, minAD, maxDD, maxAD;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

    public String getMinDD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date nextWeek = cal.getTime();
        minAD = sdf.format(nextWeek);
        cal.add(Calendar.DATE, -7);
        return minAD;
    }

    public String getMinAD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 9);
        Date nextWeek = cal.getTime();
        minAD = sdf.format(nextWeek);
        cal.add(Calendar.DATE, -9);
        return minAD;
    }

    public String getMaxDD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        Date nextMonth = cal.getTime();
        maxDD = sdf.format(nextMonth);
        cal.add(Calendar.MONTH, -1);
        return maxDD;
    }

    public String getMaxAD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        Date next2Month = cal.getTime();
        maxAD = sdf.format(next2Month);
        cal.add(Calendar.MONTH, -3);
        return maxAD;
    }

}
