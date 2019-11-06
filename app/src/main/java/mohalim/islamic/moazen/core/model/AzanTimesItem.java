package mohalim.islamic.moazen.core.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.firebase.database.annotations.NotNull;

@Entity(primaryKeys = {"month", "day", "city"}, tableName = "azan_times")
public class AzanTimesItem {
    @NonNull
    String month;
    @NonNull
    String day;
    @NonNull
    String city;

    String fagr, shoroq, zohr, asr, maghreb, eshaa;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFagr() {
        return fagr;
    }

    public void setFagr(String fagr) {
        this.fagr = fagr;
    }

    public String getShoroq() {
        return shoroq;
    }

    public void setShoroq(String shoroq) {
        this.shoroq = shoroq;
    }

    public String getZohr() {
        return zohr;
    }

    public void setZohr(String zohr) {
        this.zohr = zohr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getMaghreb() {
        return maghreb;
    }

    public void setMaghreb(String maghreb) {
        this.maghreb = maghreb;
    }

    public String getEshaa() {
        return eshaa;
    }

    public void setEshaa(String eshaa) {
        this.eshaa = eshaa;
    }
}
