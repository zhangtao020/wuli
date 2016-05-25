package com.wuliwuli.haitao.update;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.wuliwuli.haitao.base.AppApplication;
import com.wuliwuli.haitao.util.StorageUtil;
import com.wuliwuli.haitao.util.ToastUtil;

import java.io.File;

public class DownloadApp extends BroadcastReceiver {

    private static final int DOWNLOAD_ERROR = 7;
    private static final int GET_ERROR = 9;

    private Context context;
    private DownloadManager myDM;
    private long downlaodId;
    private final String path = StorageUtil.getFileCachePath() + "/updata/";
    private String apkName;
    private String downloadUrl;
    private int logoId;
    private static final String packageName = AppApplication.getInstance().getPackageName();

    private boolean inDownload = false;
    private boolean isComplete = false;

    private boolean install = false;

    private UpdateListener updateListener;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case DOWNLOAD_ERROR:
                    ToastUtil.show("下载错误, 版本下载错误，请稍候再试！");
                    break;
                case GET_ERROR:
                    ToastUtil.show("网络异常, 无法获取版本信息，请检查网络状况");
                    break;
            }
        }
    };


    public DownloadApp(Context context, String _appName, String _downloadUrl, int logoResId,UpdateListener updateListener) {
        super();
        this.context = context;
        this.downloadUrl = _downloadUrl;
        this.updateListener = updateListener;
        logoId = logoResId;
        apkName = _appName;
    }

    public void toDownload() {
        try {
            File p = new File(path);
            if (!p.exists()) {
                p.mkdirs();
            }
            //check the apk is exeist? yes open, no download
            File apkFile = new File(path, apkName);
            if (apkFile.exists()) {
                apkFile.delete();
            }
            //android skd > 8, use system download
            if (android.os.Build.VERSION.SDK_INT > 8) {
                myDM = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(downloadUrl);
                Request r = new Request(uri);
                r.setMimeType(".apk");
                r.setTitle(apkName);
                r.setShowRunningNotification(true);
                r.setVisibleInDownloadsUi(true);
                r.setDescription("点击取消下载" + apkName);
                r.setDestinationUri(Uri.fromFile(new File(path, apkName)));
                downlaodId = myDM.enqueue(r);
                //listener download complete event
                IntentFilter filter = new IntentFilter();
                filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
                context.registerReceiver(this, filter);
                Toast.makeText(context, apkName + "开始下载", Toast.LENGTH_SHORT).show();
                inDownload = true;

                return;
            }
        } catch (Exception e) {
            Toast.makeText(context, "没有可用存储空间,版本下载失败", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void downLoadComplete(boolean notify) {
        //notification download complete in top system bar
        isComplete = true;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path + apkName),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (notify) {
            String msg = apkName + "下载完成，点击安装";
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context)
                    .setAutoCancel(false)
                    .setContentTitle("下载完成")
                    .setContentText("下载完成，点击安装")
                    .setContentIntent(contentIntent)
                    .setSmallIcon(logoId)
                    .setWhen(System.currentTimeMillis());
            Notification notification = builder.getNotification();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.when = System.currentTimeMillis() + 100;
            notification.tickerText = msg;
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify((int) notification.when, notification);
            return;
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        if (downlaodId == 0 || arg1 == null || !arg1.getPackage().equals(context.getPackageName()))
            return;
        inDownload = false;
        String action = arg1.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Cursor cur = myDM.query((new DownloadManager.Query()).setFilterById(downlaodId).
                    setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL));
            if (cur.getCount() > 0)
                downLoadComplete(!install);
            else
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            context.unregisterReceiver(this);
            cur.close();
        } else/* if(DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action))*/ {
            context.unregisterReceiver(this);
            myDM.remove(downlaodId);
            File apkFile = new File(path, apkName);
            if (apkFile.exists()) {
                apkFile.delete();
            }
            updateListener.cancelUpdate();
        }
    }



    public boolean isInDownload() {
        return inDownload;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setInstall(boolean install) {
        this.install = install;
    }



//    // -------------------------------新浪秒车更新特有------------------------------------------
//    /**
//     * 新浪秒车2C端特有
//     */
//    private DownloadChangeObserver observer;
//
//    /**
//     * 新浪秒车2C端特有
//     */
//    private class DownloadChangeObserver extends ContentObserver {
//
//        public DownloadChangeObserver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            queryDownloadStatus();
//        }
//    }
//
//    /**
//     * 新浪秒车2C端特有
//     */
//    private void queryDownloadStatus() {
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(downlaodId);
//        Cursor c = myDM.query(query);
//        try {
//            if (c != null && c.moveToFirst()) {
//                Log.d("downloadapp___", "c != null......");
//                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//
//                int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
//                int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
//                int fileSizeIdx =
//                        c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
//                int bytesDLIdx =
//                        c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
//                String title = c.getString(titleIdx);
//                int fileSize = c.getInt(fileSizeIdx);
//                int bytesDL = c.getInt(bytesDLIdx);
//                Log.d("downloadapp___", "bytesDL="+bytesDL);
//
//                // Translate the pause reason to friendly text.
//                int reason = c.getInt(reasonIdx);
//                StringBuilder sb = new StringBuilder();
//                sb.append(title).append("\n");
//                sb.append("Downloaded ").append(bytesDL).append(" / ").append(fileSize);
//
//                // Display the status
//                Log.d("downloadapp___", "当前进度:" + sb.toString());
//                switch (status) {
//                    case DownloadManager.STATUS_PAUSED:
//                        Log.v("tag", "STATUS_PAUSED");
//                    case DownloadManager.STATUS_PENDING:
//                        Log.v("tag", "STATUS_PENDING");
//                    case DownloadManager.STATUS_RUNNING:
//                        //正在下载，不做任何事情
//                        Log.v("tag", "STATUS_RUNNING");
//                        break;
//                    case DownloadManager.STATUS_SUCCESSFUL:
//                        //完成
//                        Log.v("tag", "下载完成");
//                        myDM.remove(downlaodId);
//                        break;
//                    case DownloadManager.STATUS_FAILED:
//                        //清除已下载的内容，重新下载
//                        Log.v("tag", "STATUS_FAILED");
//                        myDM.remove(downlaodId);
//                        break;
//                }
//            } else {
//                Log.d("downloadapp___", "c == null......");
//            }
//        } catch (Exception e) {
//            Log.d("downloadapp___", "异常:......"+e.getMessage());
//        }

//    }
}