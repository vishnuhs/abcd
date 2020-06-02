package com.parkinncharge.parkinncharge;

public class booking_details {
    private String startDate,startTime,endDate,endTime,amount,type,status,date_of_booking,order_id,scan_in_time,scan_in_date;

    public booking_details() {
        //empty constuctor needed
    }

    public booking_details(String startDate, String startTime, String endDate, String endTime, String amount, String type, String status, String date_of_booking,String order_id,String scan_in_time,String scan_in_date) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.date_of_booking = date_of_booking;
        this.order_id=order_id;
        this.scan_in_time=scan_in_time;
        this.scan_in_date=scan_in_date;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getAmount() {
        return amount;
    }

    public String getScan_in_time() {
        return scan_in_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getType() {
        return type;
    }

    public String getScan_in_date() {
        return scan_in_date;
    }

    public String getStatus() {
        return status;
    }

    public String getDate_of_booking() {
        return date_of_booking;
    }
}
