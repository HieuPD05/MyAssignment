package model;

public class LeaveQuota {
    private int eid;
    private int year;
    private double quotaDays;
    private double usedDays;

    public int getEid() { return eid; }
    public void setEid(int eid) { this.eid = eid; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getQuotaDays() { return quotaDays; }
    public void setQuotaDays(double quotaDays) { this.quotaDays = quotaDays; }

    public double getUsedDays() { return usedDays; }
    public void setUsedDays(double usedDays) { this.usedDays = usedDays; }
}
