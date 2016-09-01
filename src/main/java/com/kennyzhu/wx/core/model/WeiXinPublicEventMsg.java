package com.kennyzhu.wx.core.model;

/**
 * Desc:事件消息
 * <p/>Date: 2014/11/26
 * <br/>Time: 16:53
 * <br/>User: ylzhu
 */
public class WeiXinPublicEventMsg extends WeiXinPublicMsg
{
	private String event;

	public String getEvent()
	{
		return event;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}
}
