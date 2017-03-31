package com.lq.beauty.base.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ListAdapter;

/**
 * Created by wuqingqing on 2017/3/31.
 */

public class UIDialog {

    public static AlertDialog.Builder createBaseDialog(
            Context context,
            String title) {
        return createBaseDialog(context, title, -1);
    }

    public static AlertDialog.Builder createBaseDialog(
            Context context,
            String title,
            int iconResId) {

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        if (iconResId > -1) {
            normalDialog.setIcon(iconResId);
        }
        normalDialog.setTitle(title);
        return normalDialog;
    }

    public static AlertDialog.Builder createNormalDialog(
            Context context,
            String title,
            String message,
            int iconResId,
            DialogInterface.OnClickListener positiveClickListener) {

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        if (iconResId > -1) {
            normalDialog.setIcon(iconResId);
        }
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定", positiveClickListener);
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return normalDialog;
    }

    public static AlertDialog.Builder createListDialog(
            Context context,
            String title,
            ListAdapter adapter,
            int iconResId,
            DialogInterface.OnClickListener onItemClickListener) {

        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        if (iconResId > -1) {
            listDialog.setIcon(iconResId);
        }
        listDialog.setTitle(title);
        listDialog.setAdapter(adapter, onItemClickListener);
        return listDialog;
    }

    public static AlertDialog.Builder createSingleChoiceDialog(
            Context context,
            String title,
            ListAdapter adapter,
            int defaultSelectIndex,
            int iconResId,
            DialogInterface.OnClickListener onItemClickListener) {

        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        if (iconResId > -1) {
            singleChoiceDialog.setIcon(iconResId);
        }
        singleChoiceDialog.setTitle(title);
        singleChoiceDialog.setSingleChoiceItems(adapter, defaultSelectIndex, onItemClickListener);

        return singleChoiceDialog;
    }

}
