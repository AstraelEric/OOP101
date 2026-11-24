package com.mycompany.bookbridge.model;

public interface Renewable {
    boolean isRenewable();
    void renew(int additionalDays);
}