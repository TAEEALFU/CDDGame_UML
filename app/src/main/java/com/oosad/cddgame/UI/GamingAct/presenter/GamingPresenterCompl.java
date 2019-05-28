package com.oosad.cddgame.UI.GamingAct.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.oosad.cddgame.Data.Constant;
import com.oosad.cddgame.Data.Robot;
import com.oosad.cddgame.Data.Setting;
import com.oosad.cddgame.Data.User;
import com.oosad.cddgame.UI.GamingAct.model.Card;
import com.oosad.cddgame.UI.GamingAct.model.system.GameSystem;
import com.oosad.cddgame.UI.GamingAct.util.CardUtil;
import com.oosad.cddgame.UI.GamingAct.view.CardLayout;
import com.oosad.cddgame.UI.GamingAct.view.CascadeLayout;
import com.oosad.cddgame.UI.GamingAct.view.IGamingView;

public class GamingPresenterCompl implements IGamingPresenter {

    private IGamingView m_GamingView;

    public static final String INT_SETTING_INFO = "SETTING_INFO";
    public static final String INT_BUNDLE_INFO = "BUNDLE_INFO";


    public GamingPresenterCompl(IGamingView iGamingView) {
        this.m_GamingView = iGamingView;
    }

    private void ShowLogE(String FunctionName, String data) {
        String TAG = "CDDGame";
        String CN = "GamingPresenterCompl";
        String msg = CN + "###" + FunctionName + "(): " + data;
        Log.e(TAG, msg);
    }

    /**
     * 处理从 MainAct 传递进来的 Bundle 数据
     * @param intent
     */
    @Override
    public void Handle_SetupBundle(Intent intent) {
        Bundle bundle = intent.getBundleExtra(INT_BUNDLE_INFO);
        Setting setting = (Setting) bundle.getSerializable(INT_SETTING_INFO);
        User currUser = setting.getCurrUser();

        m_GamingView.onSetupUI(currUser.getName());
    }

    /**
     * 暂停游戏
     */
    @Override
    public void Handle_PauseGameButton_Click() {

    }

    /**
     * 处理发牌，并显示
     */
    @Override
    public void Handle_DistributeCard() {
        Context context = m_GamingView.getThisPtr();
        // 记录：更新Cnt，调试问题
        CardLayout.clearCardUpCnt();
        Card[] cards = GameSystem.getInstance().DistributeCardForStartGame();

        for (Card c : cards)
            m_GamingView.onAddCardLayout(CardUtil.getCardLayoutFromCard(context, c, false));

        m_GamingView.onRefreshCardLayout();
    }

    /**
     * 处理出牌，重要
     * @param cardLayouts
     */
    @Override
    public void Handle_PushCard(CardLayout[] cardLayouts) {
        CardLayout[] cardSetLayout = CardUtil.getCardSetLayoutUp(cardLayouts);

        // 想要出的 Card[]
        Card[] cards = CardUtil.getCardsFromCardLayouts(cardSetLayout);
        ShowLogE("Handle_PushCard", ""+cards.length);
        if (cards.length == 0) {
            // 没有选择出牌
            m_GamingView.onShowNoSelectCardAlert();
            return;
        }

        // 处理出牌规则判断 !!!!!!
        int ret = GameSystem.getInstance().canShowCardWithCheckTurn(cards, Setting.getInstance().getCurrUser());

        if (ret == Constant.NO_ERR)  // 允许这样出牌，并且已经在 CardMgr 内更新了相关信息，直接显示出牌更新界面
            m_GamingView.onShowCardSet(cardSetLayout);
        else if (ret == Constant.ERR_NOTRULE)
            m_GamingView.onShowCantShowCardForRuleAlert();
        else if (ret == Constant.ERR_NOTROUND)
            m_GamingView.onShowCantShowCardForRoundAlert();
    }

    /**
     * 跳过出牌
     */
    @Override
    public void Handle_JumpShowCard() {
        GameSystem.getInstance().canShowCardWithCheckTurn(null, Setting.getInstance().getCurrUser());

        Toast.makeText(m_GamingView.getThisPtr(), "跳过出牌", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Handle_SetupRobotShowCardLayout(final CascadeLayout cascadeLayout, int RobotIdx) {
        GameSystem.getInstance().getRobot(RobotIdx).setOnRobotShowCard(new Robot.OnRobotShowCardListener() {
            @Override
            public void onShowCard(final Card[] cards) {

                cascadeLayout.removeAllViews(); // 清除布局后添加时延

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for (Card c : cards) {
                            CardLayout cl = CardUtil.getCardLayoutFromCard(m_GamingView.getThisPtr(), c, true);
                            cascadeLayout.addView(cl); // 从拥有的牌转化成展示的牌
                        }
                    }

                }, Constant.TIME_WaitByClearRobotShowCardLayout);


            }
        });
    }
}
