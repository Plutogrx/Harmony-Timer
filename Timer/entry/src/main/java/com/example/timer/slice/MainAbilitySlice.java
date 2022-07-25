package com.example.timer.slice;

import com.example.timer.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.Revocable;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

public class MainAbilitySlice extends AbilitySlice {

    private Text txtCountdown;
    private Button btnCountdown;
    private Button btnRedPocket;
    private Button btnFlush;
    private Revocable revocable;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        // 获取控件对象
        getComponents();
        // 给"开始倒计时"按钮绑定监听器
        btnCountdown.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                //
                revocable = startCountdown();
            }
        });
        // 给“抢红包”按钮绑定监听器
        btnRedPocket.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                redPocket();
            }
        });
        // 给“刷新”按钮绑定监听器
        btnFlush.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                flush(revocable);
            }
        });
    }

    // 刷新
    private void flush(Revocable revocable) {
        txtCountdown.setText("10");
        revocable.revoke();
    }

    // 抢红包
    private void redPocket() {
        int time = Integer.parseInt(txtCountdown.getText());
        if (time>0 && time<10) {
            new ToastDialog(getContext()).setText("倒计时还未结束").show();
        } else if (time == 10) {
            new ToastDialog(getContext()).setText("倒计时还未开始").show();
        } else {
            txtCountdown.setText("抢到红包");
        }
    }

    // 倒计时
    private Revocable startCountdown() {
        TaskDispatcher uiTaskDispatcher = getUITaskDispatcher();
        Revocable revocable = uiTaskDispatcher.delayDispatch(new Runnable() {
            @Override
            public void run() {
                int countdown = Integer.parseInt(txtCountdown.getText());
                if (countdown > 0) {
                    countdown--;
                    txtCountdown.setText(countdown + "");
                    startCountdown();
                }
            }
        }, 1000L);
        return revocable;
    }

    // 获取控件对象
    private void getComponents() {
        txtCountdown = (Text) findComponentById(ResourceTable.Id_txt_countdown);
        btnCountdown = (Button) findComponentById(ResourceTable.Id_btn_countdown);
        btnRedPocket = (Button) findComponentById(ResourceTable.Id_btn_redPocket);
        btnFlush = (Button) findComponentById(ResourceTable.Id_btn_flush);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}
