package io.akwa.frigdoor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BluTagInfo implements Parcelable {

    String id;
    String hardware;
    String startTime;
    String endTime;
    int breachCount;
    int breachDuration;
    String maxTemperature;
    String minTemperature;
    String lastRecordedTemperature;
    float lastRecordedTemp;
    String averageTemperature;
    String kineticMeanTemperature;
    String maxRecordedTemperature;
    String minRecordedTemperature;
    float maxTemp;
    float minTemp;
    long epochTempRecordStartTime;
    long epochTempExpDate;
    String expirationDate;
    public Float[] temp;
    ArrayList<BreachInfo> breachInfos;
    String totalDuration;
    long measurementCycle;
    float maxRecordedTemp;
    float minRecordedTemp;

    protected BluTagInfo(Parcel in) {
        id = in.readString();
        hardware = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        breachCount = in.readInt();
        breachDuration = in.readInt();
        maxTemperature = in.readString();
        minTemperature = in.readString();
        lastRecordedTemperature = in.readString();
        lastRecordedTemp=in.readFloat();
        averageTemperature = in.readString();
        kineticMeanTemperature = in.readString();
        maxRecordedTemperature = in.readString();
        minRecordedTemperature = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(hardware);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeInt(breachCount);
        dest.writeInt(breachDuration);
        dest.writeString(maxTemperature);
        dest.writeString(minTemperature);
        dest.writeString(lastRecordedTemperature);
        dest.writeFloat(lastRecordedTemp);
        dest.writeString(averageTemperature);
        dest.writeString(kineticMeanTemperature);
        dest.writeString(maxRecordedTemperature);
        dest.writeString(minRecordedTemperature);
    }

    public float getLastRecordedTemp() {
        return lastRecordedTemp;
    }

    public void setLastRecordedTemp(float lastRecordedTemp) {
        this.lastRecordedTemp = lastRecordedTemp;
    }

    public float getMaxRecordedTemp() {
        return maxRecordedTemp;
    }

    public void setMaxRecordedTemp(float maxRecordedTemp) {
        this.maxRecordedTemp = maxRecordedTemp;
    }

    public float getMinRecordedTemp() {
        return minRecordedTemp;
    }

    public void setMinRecordedTemp(float minRecordedTemp) {
        this.minRecordedTemp = minRecordedTemp;
    }

    public long getMeasurementCycle() {
        return measurementCycle;
    }

    public void setMeasurementCycle(long measurementCycle) {
        this.measurementCycle = measurementCycle;
    }

    public long getEpochTempRecordStartTime() {
        return epochTempRecordStartTime;
    }

    public void setEpochTempRecordStartTime(long epochTempRecordStartTime) {
        this.epochTempRecordStartTime = epochTempRecordStartTime;
    }

    public long getEpochTempExpDate() {
        return epochTempExpDate;
    }

    public void setEpochTempExpDate(long epochTempExpDate) {
        this.epochTempExpDate = epochTempExpDate;
    }

    public ArrayList<BreachInfo> getBreachInfos() {
        return breachInfos;
    }


    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }


    public void setBreachInfos(ArrayList<BreachInfo> breachInfos) {
        this.breachInfos = breachInfos;
    }

    public Float[] getTemp() {
        return temp;
    }

    public void setTemp(Float[] temp) {
        this.temp = temp;
    }


    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }



    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getBreachCount() {
        return breachCount;
    }

    public void setBreachCount(int breachCount) {
        this.breachCount = breachCount;
    }

    public int getBreachDuration() {
        return breachDuration;
    }

    public void setBreachDuration(int breachDuration) {
        this.breachDuration = breachDuration;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getLastRecordedTemperature() {
        return lastRecordedTemperature;
    }

    public void setLastRecordedTemperature(String lastRecordedTemperature) {
        this.lastRecordedTemperature = lastRecordedTemperature;
    }

    public String getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(String averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public String getKineticMeanTemperature() {
        return kineticMeanTemperature;
    }

    public void setKineticMeanTemperature(String kineticMeanTemperature) {
        this.kineticMeanTemperature = kineticMeanTemperature;
    }

    public String getMaxRecordedTemperature() {
        return maxRecordedTemperature;
    }

    public void setMaxRecordedTemperature(String maxRecordedTemperature) {
        this.maxRecordedTemperature = maxRecordedTemperature;
    }

    public String getMinRecordedTemperature() {
        return minRecordedTemperature;
    }

    public void setMinRecordedTemperature(String minRecordedTemperature) {
        this.minRecordedTemperature = minRecordedTemperature;
    }

    public BluTagInfo(){

    }



    @Override
    public int describeContents() {
        return 0;
    }



    @SuppressWarnings("unused")
    public static final Creator<BluTagInfo> CREATOR = new Creator<BluTagInfo>() {
        @Override
        public BluTagInfo createFromParcel(Parcel in) {
            return new BluTagInfo(in);
        }

        @Override
        public BluTagInfo[] newArray(int size) {
            return new BluTagInfo[size];
        }
    };


    public static float devideByTen(int data) {

        boolean isNegative = false;
        if (data < 0) {
            isNegative = true;
            data = Math.abs(data);
//            temperature = (float) data * 100 / (float) 100.00;
        }

        String temperature = Integer.toString(data / 10) + "." + Integer.toString(data % 10);
        if (isNegative) {
            temperature = "-" + temperature;
        }

        return Float.parseFloat(temperature);
    }

    public static String toStringDevideByTen(int data) {

        boolean isNegative = false;
        if (data < 0) {
            isNegative = true;
            data = Math.abs(data);
//            temperature = (float) data * 100 / (float) 100.00;
        }

        String temperature = Integer.toString(data / 10) + "." + Integer.toString(data % 10);
        if (isNegative) {
            temperature = "-" + temperature;
        }
        return temperature;
    }

    @Override
    public String toString() {
        return "BluTagInfo{" +
                "id='" + id + '\'' +
                ", hardware='" + hardware + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", breachCount=" + breachCount +
                ", breachDuration=" + breachDuration +
                ", maxTemperature='" + maxTemperature + '\'' +
                ", minTemperature='" + minTemperature + '\'' +
                ", lastRecordedTemperature='" + lastRecordedTemperature + '\'' +
                ", lastRecordedTemp=" + lastRecordedTemp +
                ", averageTemperature='" + averageTemperature + '\'' +
                ", kineticMeanTemperature='" + kineticMeanTemperature + '\'' +
                ", maxRecordedTemperature='" + maxRecordedTemperature + '\'' +
                ", minRecordedTemperature='" + minRecordedTemperature + '\'' +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", epochTempRecordStartTime=" + epochTempRecordStartTime +
                ", epochTempExpDate=" + epochTempExpDate +
                ", expirationDate='" + expirationDate + '\'' +
                ", breachInfos=" + breachInfos +
                ", totalDuration='" + totalDuration + '\'' +
                ", measurementCycle=" + measurementCycle +
                ", maxRecordedTemp=" + maxRecordedTemp +
                ", minRecordedTemp=" + minRecordedTemp +
                '}';
    }
}
