package com.kennyzhu.wx.core.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Desc:易信消息事件
 * <p/>Date: 2014/11/26
 * <br/>Time: 16:02
 * <br/>User: ylzhu
 */
public enum WeiXinPublicEventTypeEnum
{
	/**
	 * 订阅消息
	 */
	SUBSCRIBE("subscribe"),
	/**
	 * 取消订阅
	 */
	UNSUBSCRIBE("unsubscribe"),
	/**
	 * 菜单点击
	 */
	CLICK("click");

	private String event;

	private WeiXinPublicEventTypeEnum(String event)
	{
		this.event = event;
	}

	public String getEvent()
	{
		return event;
	}

	public static WeiXinPublicEventTypeEnum getEvent(String event)
	{
		if (StringUtils.isNotBlank(event))
		{
			for (WeiXinPublicEventTypeEnum enums : WeiXinPublicEventTypeEnum.values())
			{
				if (enums.getEvent().equals(event))
				{
					return enums;
				}
			}
		}
		return null;
	}
}
