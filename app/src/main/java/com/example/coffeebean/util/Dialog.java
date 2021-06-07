package com.example.coffeebean.util;

import android.content.DialogInterface;
import android.widget.PopupWindow;

/**
 * “窗口”约定规则
 */
public interface Dialog {
    /**
     * 展示
     */
    void show();

    /**
     * 关闭
     *
     * @param isCrowdOut 是否被挤出
     */
    void dismiss(boolean isCrowdOut);

    /**
     * 设置“窗口”关闭监听
     */
    void setOnDismissListener(DialogInterface.OnDismissListener listener);

    /**
     * 设置“窗口”展示监听
     */
    void setOnShowListener(DialogInterface.OnShowListener listener);

    /**
     * 是否满足show的条件（如处于某个tab，不关心时返回true就行）
     */
    boolean isCanShow();
}