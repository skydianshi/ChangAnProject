package com.changan.changanproject.listener;

import cn.zlg.www.ErrData;
import cn.zlg.www.ItemData;

/**
 * Created by 张海逢 on 2017/3/3.
 * Connect DataFragment and MyService
 */

public interface DataServiceListener {
    void addFrameData(ItemData itemData);
    void addSendFrame(ItemData itemData);
    void addErrFrame(ErrData errData);
}
