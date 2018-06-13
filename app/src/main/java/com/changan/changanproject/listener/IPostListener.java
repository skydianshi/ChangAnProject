package com.changan.changanproject.listener;

import cn.zlg.www.ErrData;
import cn.zlg.www.ItemData;

/**
 * Created by 张海逢 on 2017/3/3.
 * Connect FigureFragment and MyService
 */

public interface IPostListener {
    void toast(String s);
    void figureUpdate(String message);
    void showDialog(String s);
    void dismissDialog();
    void addFrameData(ItemData itemData);
    void addSendData(ItemData itemData);
    void addErrData(ErrData errData);
    void updateMenu(String connectsts);
}
