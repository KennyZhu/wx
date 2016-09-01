package com.kennyzhu.wx.core.util;

import com.kennyzhu.wx.core.enums.WeiXinPublicMenuTypeEnum;
import com.kennyzhu.wx.core.model.WeiXinPublicMenu;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:微信公众平台工具类
 * <br/>Time: 11:39
 * <br/>User: ylzhu
 */
public final class WeiXinPublicUtil {
    private static final Log LOG = LogFactory.getLog(WeiXinPublicUtil.class);

    private WeiXinPublicUtil() {
    }

    /**
     * 获取易信自定义菜单Json串
     *
     * @param mainMenus
     * @return
     */
    public static String convertMenuToJson(List<WeiXinPublicMenu> mainMenus) {
        if (CollectionUtils.isEmpty(mainMenus)) {
            return null;
        }
        Map<String, List<Map<String, Object>>> buttons = new HashMap<>();
        List<Map<String, Object>> menus = new ArrayList<>();
        for (WeiXinPublicMenu menu : mainMenus) {
            menus.add(convertMenuToMap(menu));
        }

        buttons.put("button", menus);
        String result = null;
        try {
            result = JsonUtil.getJsonString(buttons);
        } catch (Exception e) {
            LOG.fatal("#Get YiXin public Menu error!Cause:", e);
        }
        return result;
    }

    /**
     * 获取易信自定义菜单Json串
     *
     * @param menu
     * @return
     */
    public static String convertMenuToJson(WeiXinPublicMenu menu) {
        Map<String, Object> resultMap = convertMenuToMap(menu);
        if (resultMap == null) {
            return null;
        }
        try {
            String result = JsonUtil.getJsonString(convertMenuToMap(menu));
            return result;
        } catch (Exception e) {
            LOG.fatal("#Convert yixin public menu to json error!Cause:", e);
        }
        return null;
    }

    public static Map<String, Object> convertMenuToMap(WeiXinPublicMenu menu) {
        if (menu == null || menu.getName() == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();

        result.put("name", menu.getName());
        if (CollectionUtils.isNotEmpty(menu.getSubMenus())) {
            result.put("sub_button", new ArrayList<Map<String, Object>>());
            for (WeiXinPublicMenu subMenu : menu.getSubMenus()) {
                Map<String, Object> subDataMap = convertMenuToMap(subMenu);
                if (subDataMap == null) {
                    return null;
                }
                ArrayList<Map<String, Object>> subButton = (ArrayList) (result.get("sub_button"));
                subButton.add(subDataMap);
            }
        } else {
            result.put("type", menu.getMenuType().getType());
            if (WeiXinPublicMenuTypeEnum.CLICK == menu.getMenuType()) {
                if (StringUtils.isBlank(menu.getKey())) {
                    return null;
                }
                result.put("key", menu.getKey());

            } else if (WeiXinPublicMenuTypeEnum.VIEW == menu.getMenuType()) {
                if (StringUtils.isBlank(menu.getUrl())) {
                    return null;
                }
                result.put("url", menu.getUrl());
            }
        }

        return result;
    }

    public static String buildWeiXinPublicMenu() {
        WeiXinPublicMenu award = new WeiXinPublicMenu("查开奖").buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        award.setUrl("http://caipiao.163.com/third/public/weixinindex.html?gotoUrl=http://caipiao.163.com/t/award/");
        WeiXinPublicMenu buy = new WeiXinPublicMenu("买彩票").buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        buy.setUrl("http://caipiao.163.com/third/public/weixinindex.html");

        WeiXinPublicMenu myLottery = new WeiXinPublicMenu("我的彩票").buildMenuType(WeiXinPublicMenuTypeEnum.VIEW);
        myLottery
                .setUrl("http://caipiao.163.com/third/public/weixinindex.html?gotoUrl=http://caipiao.163.com/t/mylottery/");

        Map<String, List<Map<String, Object>>> buttons = new HashMap<>();
        List<Map<String, Object>> menus = new ArrayList<>();
        menus.add(convertMenuToMap(award));
        menus.add(convertMenuToMap(buy));
        menus.add(convertMenuToMap(myLottery));

        buttons.put("button", menus);

        String result = JsonUtil.getJsonString(buttons);
        LOG.info("#WeiXin Pub Menu is " + result);
        return result;
    }
}
