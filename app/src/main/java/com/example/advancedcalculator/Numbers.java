package com.example.advancedcalculator;

public class Numbers
{
    String id, result;

    public Numbers(String id, String result)
    {
        this.id = id;
        this.result = result;
    }

    public Numbers(){}

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }
}
