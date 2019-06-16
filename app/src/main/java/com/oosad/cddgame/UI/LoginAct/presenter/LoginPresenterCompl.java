package com.oosad.cddgame.UI.LoginAct.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.oosad.cddgame.DB.UserDAO;
import com.oosad.cddgame.Data.Boundary.GameSystem;
import com.oosad.cddgame.Data.Constant;
import com.oosad.cddgame.Data.Entity.Player.User;
import com.oosad.cddgame.Data.Setting;
import com.oosad.cddgame.UI.LoginAct.view.ILoginView;
import com.oosad.cddgame.UI.MainAct.MainActivity;
import com.oosad.cddgame.Util.PassUtil;

import java.util.Set;

public class LoginPresenterCompl implements ILoginPresenter {

    private ILoginView m_loginView;
    private UserDAO userDAO;


    public LoginPresenterCompl(ILoginView loginView) {
        this.m_loginView = loginView;
        userDAO = new UserDAO(m_loginView.getThisPtr());
    }

    private void ShowLogE(String FunctionName, String data) {
        String TAG = "CDDGame";
        String CN = "LoginPresenterCompl";
        String msg = CN + "###" + FunctionName + "(): " + data;
        Log.e(TAG, msg);
    }

    /**
     * 查询数据库，获取用户密码
     * @param userName
     * @return
     */
    private String getUserPassWord(String userName) {
        return userDAO.queryPassWord(userName);
    }

    /**
     * 查询数据库，获取是否存在用户
     * @param username
     * @return
     */
    private User getUser(String username) {
        return userDAO.queryUser(username);
    }

    /**
     * 判读密码是否可用，长度范围
     * @param plainPassWord
     * @return
     */
    private boolean checkPassWordValidable(String plainPassWord) {
        return plainPassWord.length() > Constant.PassWordMinLen && plainPassWord.length() < Constant.PassWordMaxLen;
    }

    /**
     * 处理登陆，并判断有效性
     * @param UserName
     * @param PlainPassWord
     */
    @Override
    public void HandleLogin(String UserName, String PlainPassWord) {
        if (UserName.isEmpty() || PlainPassWord.isEmpty()) {
            m_loginView.onShowNoUserNameOrPassWordAlert();
            return;
        }

        String passInDb = getUserPassWord(UserName);
        if (passInDb.isEmpty()) {
            m_loginView.onShowNoUserAlert();
            return;
        }

        if (!PassUtil.ComparePassWord(PlainPassWord, passInDb)) {
            m_loginView.onShowErrorPassWordAlert();
            return;
        }

        User currUser = getUser(UserName);
        GameSystem.getInstance().setUser(currUser);
        m_loginView.onShowLoginSuccessToast(UserName);
        gotoMainActivity();
    }

    /**
     * 处理注册，并判断有效性
     * @param UserName
     * @param PlainPassWord
     */
    @Override
    public void HandleRegister(String UserName, String PlainPassWord) {
        // 用户名密码空
        if (UserName.isEmpty() || PlainPassWord.isEmpty()) {
            m_loginView.onShowNoUserNameOrPassWordAlert();
            return;
        }

        // 密码无效
        if (!checkPassWordValidable(PlainPassWord)) {
            m_loginView.onShowErrorPassWordFormatAlert();
            return;
        }

        // 已经存在用户
        User currUser = getUser(UserName);
        if (currUser != null) {
            m_loginView.onShowAlwaysExistUserAlert();
            return;
        }

        // 插入失败
        if (!insertUser(UserName, PlainPassWord)) {
            m_loginView.onShowErrorRegisterAlert();
            return;
        }

        m_loginView.onClearPassWord();
        m_loginView.onShowRegisterSuccessAlert();
    }

    /**
     * 新建用户，在本地数据库与服务器后端同步
     * @param UserName
     * @param PlainPassWord
     * @return
     */
    private boolean insertUser(String UserName, String PlainPassWord) {

        boolean regInServer = true;
        // net back-end register
        if (!regInServer)
            return false;

        userDAO.insertUser(UserName, PassUtil.EncryptPassWord(PlainPassWord));
        User currUser = getUser(UserName);
        if (currUser == null)
            return false;

        ShowLogE("insertUser", "RegisterSuccess");

        return true;
    }

    /**
     * 登陆成功，跳转到主界面
     */
    private void gotoMainActivity() {
        Intent MainActIntent = new Intent(m_loginView.getThisPtr(), MainActivity.class);
        m_loginView.getThisPtr().startActivity(MainActIntent);
    }
}
