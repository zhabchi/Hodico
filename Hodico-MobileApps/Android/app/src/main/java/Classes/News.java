package Classes;

public class News {

	private int id;
	private String title;
	private String data;
	private String link;
	private String img;

	public News() {

	}

	public News(int id, String title, String data, String link, String img) {
		this.id = id;
		this.title = title;
		this.data = data;
		this.link = link;
		this.img = img;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
