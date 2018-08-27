package io.akwa.frigdoor;


import java.util.Date;

public class BreachInfo {

    public Date start;
    public Date end;
    public int duration;
    public double avgTemp;
    public String minMaxTemp;
    public float minMaxTempNumber;
    public String breachType="";// values can be "Max" 0r "Min"

    public static enum BreachType {
        MIN,
        NONE,
        MAX;

        private BreachType() {
        }
    }

    public float getMinMaxTempNumber() {
        return minMaxTempNumber;
    }

    public void setMinMaxTempNumber(float minMaxTempNumber) {
        this.minMaxTempNumber = minMaxTempNumber;
    }

    /**
     *
     * @return
     * The start
     */
    public Date getStart() {
        return start;
    }

    /**
     *
     * @param start
     * The start
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     *
     * @return
     * The end
     */
    public Date getEnd() {
        return end;
    }

    /**
     *
     * @param end
     * The end
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     *
     * @return
     * The avgTemp
     */
    public double getAvgTemp() {
        return avgTemp;
    }

    /**
     *
     * @param avgTemp
     * The avgTemp
     */
    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    /**
     *
     * @return
     * The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The breachType
     */
    public String getBreachType() {
        return breachType;
    }

    /**
     *
     * @param breachType
     * The breachType
     */
    public void setBreachType(String breachType) {
        this.breachType = breachType;
    }

    /**
     *
     * @return
     * The minMaxTemp
     */
    public String getMinMaxTemp() {
        return minMaxTemp;
    }

    /**
     *
     * @param minMaxTemp
     * The minMaxTemp
     */
    public void setMinMaxTemp(String minMaxTemp) {
        this.minMaxTemp = minMaxTemp;
    }

    @Override
    public String toString() {
        return "BreachInfo{" +
                "start=" + start +
                ", end=" + end +
                ", duration=" + duration +
                ", avgTemp=" + avgTemp +
                ", minMaxTemp='" + minMaxTemp + '\'' +
                ", breachType='" + breachType + '\'' +
                '}';
    }
}
