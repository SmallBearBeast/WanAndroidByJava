package com.bear.wanandroidbyjava.base;

import com.bear.libcomponent.component.ComponentAct;
import com.bear.libcomponent.component.GroupComponent;
import com.bear.librv.VHBridge;
import com.bear.librv.VHolder;

public abstract class ComponentVHBridge<VH extends VHolder> extends VHBridge<VH> {
    protected <C extends GroupComponent> C getComponent(Class<C> clz) {
        if (getContext() instanceof ComponentAct) {
            return ((ComponentAct)getContext()).getComponentManager().getComponent(clz);
        }
        return null;
    }

    protected <C extends GroupComponent> C getComponent(Class<C> clz, Object tag) {
        if (getContext() instanceof ComponentAct) {
            return ((ComponentAct)getContext()).getComponentManager().getComponent(clz, tag);
        }
        return null;
    }
}
