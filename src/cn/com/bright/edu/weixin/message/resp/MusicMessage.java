package cn.com.bright.edu.weixin.message.resp;

/**
 * ������Ϣ
 */
public class MusicMessage extends RespBaseMessage {
	// ����
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}