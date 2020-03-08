package com.bear.wanandroidbyjava.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.bear.wanandroidbyjava.Bean.PublicTab;

@Entity(indices = @Index("publicTabId"))
public class PublicTabE {
    @PrimaryKey
    public int publicTabId;
    public String name;
    public int order;

    public PublicTab toPublicTab() {
        PublicTab publicTab = new PublicTab();
        publicTab.publicTabId = publicTabId;
        publicTab.name = name;
        publicTab.order = order;
        return publicTab;
    }
}
