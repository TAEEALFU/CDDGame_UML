package com.oosad.cddgame.Util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.oosad.cddgame.R;
import com.oosad.cddgame.Data.Entity.Card;
import com.oosad.cddgame.UI.Widget.CardLayout;

import java.util.ArrayList;

public class CardUtil {

    /**
     * 从 Card 获得 CardLayout
     * @param context
     * @param card
     * @param IsShowCardLayout
     *      true: 指定布局为 用于展示的 CardLayout
     *      false: 指定布局为 个人用有的 CardLayout
     * @param isGaming 在哪个活动中
     * @return
     */
    public static CardLayout getCardLayoutFromCard(Context context, Card card, boolean IsShowCardLayout, boolean isGaming) {
        CardLayout cl = new CardLayout(context);
        cl.setCard(card);

        int height, width;
        if (IsShowCardLayout) { // ShowCardLayout
            if (isGaming) {
                // Gaming Act
                height = (int) context.getResources().getDimension(R.dimen.ShowCard_Height);
                width = (int) context.getResources().getDimension(R.dimen.ShowCard_Width);
            }
            else {
                // Score Act
                height = (int) context.getResources().getDimension(R.dimen.ScoreCard_Height);
                width = (int) context.getResources().getDimension(R.dimen.ScoreCard_Width);
            }
        }
        else { // MainCardLayout
            height = (int) context.getResources().getDimension(R.dimen.Card_Height);
            width = (int) context.getResources().getDimension(R.dimen.Card_Width);
        }

        cl.setLayoutSize(width, height, context.getResources().getDisplayMetrics());
        cl.setBackground(CardUtil.getCardBackGroundFromCard(context, card));
        cl.setCanCardSelected(!IsShowCardLayout); // 拥有的牌 -> 可选
        return cl;
    }

    /**
     * 从 Card 获得 CardLayout (Gaming 内)
     * @param context
     * @param card
     * @param IsShowCardLayout
     *      true: 指定布局为 用于展示的 CardLayout
     *      false: 指定布局为 个人用有的 CardLayout
     * @return
     */
    public static CardLayout getCardLayoutFromCard(Context context, Card card, boolean IsShowCardLayout) {
        return getCardLayoutFromCard(context, card, IsShowCardLayout, true);
    }


    /**
     * 从 CardLayout[] 获得 Card[]
     * @param cardLayouts
     * @return
     */
    public static Card[] getCardsFromCardLayouts(CardLayout[] cardLayouts) {
        if (cardLayouts == null)
            return new Card[] {};

        Card[] cards = new Card[cardLayouts.length];
        int idx = 0;
        for (CardLayout cl : cardLayouts) {
            if (cl != null)
                cards[idx++] = cl.getCard();
        }
        return cards;
    }

    /**
     * 通过 CardLayout[] 获取选中的 CardSet
     * @param cardLayouts
     * @return
     */
    public static CardLayout[] getCardSetLayoutUp(CardLayout[] cardLayouts) {
        if (cardLayouts == null)
            return new CardLayout[]{};

        ArrayList<CardLayout> cards = new ArrayList<>();
        for (CardLayout v : cardLayouts) {
            if (v.getIsUp())
                cards.add(v);
        }
        return (CardLayout[]) cards.toArray(new CardLayout[cards.size()]);
    }

    /**
     * 业余处理显示图片
     * @param context
     * @param card 要显示的扑克牌
     * @return 该扑克牌对应的 Drawable
     */
    public static Drawable getCardBackGroundFromCard(Context context, Card card) {
        Resources r = context.getResources();
        Drawable ret = r.getDrawable(R.drawable.card_40);
        switch (card.getCardSuit()) {
            case Spade: // 黑桃 ♠
                switch (card.getCardNum()) {
                    case 1:
                        ret = r.getDrawable(R.drawable.card_40);
                    break;
                    case 2:
                        ret = r.getDrawable(R.drawable.card_41);
                    break;
                    case 3:
                        ret = r.getDrawable(R.drawable.card_42);
                    break;
                    case 4:
                        ret = r.getDrawable(R.drawable.card_43);
                    break;
                    case 5:
                        ret = r.getDrawable(R.drawable.card_44);
                    break;
                    case 6:
                        ret = r.getDrawable(R.drawable.card_45);
                    break;
                    case 7:
                        ret = r.getDrawable(R.drawable.card_46);
                    break;
                    case 8:
                        ret = r.getDrawable(R.drawable.card_47);
                    break;
                    case 9:
                        ret = r.getDrawable(R.drawable.card_48);
                    break;
                    case 10:
                        ret = r.getDrawable(R.drawable.card_49);
                    break;
                    case 11:
                        ret = r.getDrawable(R.drawable.card_50);
                    break;
                    case 12:
                        ret = r.getDrawable(R.drawable.card_51);
                    break;
                    case 13:
                        ret = r.getDrawable(R.drawable.card_52);
                    break;
                }
            break;
            case Heart: // 红桃 ♥
                switch (card.getCardNum()) {
                    case 1:
                        ret = r.getDrawable(R.drawable.card_27);
                        break;
                    case 2:
                        ret = r.getDrawable(R.drawable.card_28);
                        break;
                    case 3:
                        ret = r.getDrawable(R.drawable.card_29);
                        break;
                    case 4:
                        ret = r.getDrawable(R.drawable.card_30);
                        break;
                    case 5:
                        ret = r.getDrawable(R.drawable.card_31);
                        break;
                    case 6:
                        ret = r.getDrawable(R.drawable.card_32);
                        break;
                    case 7:
                        ret = r.getDrawable(R.drawable.card_33);
                        break;
                    case 8:
                        ret = r.getDrawable(R.drawable.card_34);
                        break;
                    case 9:
                        ret = r.getDrawable(R.drawable.card_35);
                        break;
                    case 10:
                        ret = r.getDrawable(R.drawable.card_36);
                        break;
                    case 11:
                        ret = r.getDrawable(R.drawable.card_37);
                        break;
                    case 12:
                        ret = r.getDrawable(R.drawable.card_38);
                        break;
                    case 13:
                        ret = r.getDrawable(R.drawable.card_39);
                        break;
                }
            break;
            case Diamond: // 方块 ♦
                switch (card.getCardNum()) {
                    case 1:
                        ret = r.getDrawable(R.drawable.card_1);
                        break;
                    case 2:
                        ret = r.getDrawable(R.drawable.card_2);
                        break;
                    case 3:
                        ret = r.getDrawable(R.drawable.card_3);
                        break;
                    case 4:
                        ret = r.getDrawable(R.drawable.card_4);
                        break;
                    case 5:
                        ret = r.getDrawable(R.drawable.card_5);
                        break;
                    case 6:
                        ret = r.getDrawable(R.drawable.card_6);
                        break;
                    case 7:
                        ret = r.getDrawable(R.drawable.card_7);
                        break;
                    case 8:
                        ret = r.getDrawable(R.drawable.card_8);
                        break;
                    case 9:
                        ret = r.getDrawable(R.drawable.card_9);
                        break;
                    case 10:
                        ret = r.getDrawable(R.drawable.card_10);
                        break;
                    case 11:
                        ret = r.getDrawable(R.drawable.card_11);
                        break;
                    case 12:
                        ret = r.getDrawable(R.drawable.card_12);
                        break;
                    case 13:
                        ret = r.getDrawable(R.drawable.card_13);
                        break;
                }
            break;
            case Club: // 梅花 ♣
                switch (card.getCardNum()) {
                    case 1:
                        ret = r.getDrawable(R.drawable.card_14);
                        break;
                    case 2:
                        ret = r.getDrawable(R.drawable.card_15);
                        break;
                    case 3:
                        ret = r.getDrawable(R.drawable.card_16);
                        break;
                    case 4:
                        ret = r.getDrawable(R.drawable.card_17);
                        break;
                    case 5:
                        ret = r.getDrawable(R.drawable.card_18);
                        break;
                    case 6:
                        ret = r.getDrawable(R.drawable.card_19);
                        break;
                    case 7:
                        ret = r.getDrawable(R.drawable.card_20);
                        break;
                    case 8:
                        ret = r.getDrawable(R.drawable.card_21);
                        break;
                    case 9:
                        ret = r.getDrawable(R.drawable.card_22);
                        break;
                    case 10:
                        ret = r.getDrawable(R.drawable.card_23);
                        break;
                    case 11:
                        ret = r.getDrawable(R.drawable.card_24);
                        break;
                    case 12:
                        ret = r.getDrawable(R.drawable.card_25);
                        break;
                    case 13:
                        ret = r.getDrawable(R.drawable.card_26);
                        break;
                }
            break;
        }
        return ret;
    }
}
