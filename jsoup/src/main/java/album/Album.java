package album;

/*
 * 专辑
 */
public class Album {
	/**
	 * 专辑名
	 */
	private String title;
	/**
	 * 专辑所属账号
	 */
	private String user;
	/**
	 * 介绍
	 */
	private String intro;
	/**
	 * 专辑播放量
	 */
	private String totalPlayCount;
	/**
	 * 专辑地址
	 */
	private String href;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	Album(String title, String anchorman, String info, String totalPlayCount, String href) {
		this.title = title;
		this.user = anchorman;
		this.intro = info;
		this.totalPlayCount = totalPlayCount;
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnchorman() {
		return user;
	}

	public void setAnchorman(String anchorman) {
		this.user = anchorman;
	}

	public String getInfo() {
		return intro;
	}

	public void setInfo(String info) {
		this.intro = info;
	}

	public String getTotalPlayCount() {
		return totalPlayCount;
	}

	public void setTotalPlayCount(String totalPlayCount) {
		this.totalPlayCount = totalPlayCount;
	}

	@Override
	public String toString() {
		return "Album [title=" + title + ", user=" + user + ", intro=" + intro + ", totalPlayCount=" + totalPlayCount
				+ ", href=" + href + "]";
	}

}
