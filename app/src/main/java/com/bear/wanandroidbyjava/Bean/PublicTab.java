package com.bear.wanandroidbyjava.Bean;

import com.bear.wanandroidbyjava.Entity.PublicTabE;

public class PublicTab {
    public int publicTabId;
    public String name;
    public int order;

    public PublicTabE toPublicTabE() {
        PublicTabE publicTabE = new PublicTabE();
        publicTabE.publicTabId = publicTabId;
        publicTabE.name = name;
        publicTabE.order = order;
        return publicTabE;
    }
}
