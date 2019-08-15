package ftpspider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * 列出FTP服务器上指定目录下面的所有文件
 */
public class FTPListAllFiles {
	private static Logger logger = Logger.getLogger(FTPListAllFiles.class);
	public FTPClient ftp;
	public ArrayList<String> arFiles;

	/**
	 * 重载构造函数
	 *
	 * @param isPrintCommmand 是否打印与FTPServer的交互命令
	 */
	public FTPListAllFiles(boolean isPrintCommmand) {
		ftp = new FTPClient();
		arFiles = new ArrayList<String>();
		if (isPrintCommmand) {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
	}

	/**
	 * 登陆FTP服务器
	 *
	 * @param host     FTPServer IP地址
	 * @param port     FTPServer 端口
	 * @param username FTPServer 登陆用户名
	 * @param password FTPServer 登陆密码
	 * @return 是否登录成功
	 * @throws IOException
	 */
	public boolean login(String host, int port, String username, String password) throws IOException {
		this.ftp.connect(host, port);
		if (FTPReply.isPositiveCompletion(this.ftp.getReplyCode())) {
			if (this.ftp.login(username, password)) {
				/**
				 * 需要注意这句代码，如果调用List()方法出现，文件的无线递归，与真实目录结构不一致的时候，可能就是因为转码后，读出来的文件夹与正式文件夹字符编码不一致所导致。
				 * 则需去掉转码，尽管递归是出现乱码，但读出的文件就是真实的文件，不会死掉。等读完之后再根据情况进行转码。 如果ftp部署在windows下，则： for
				 * (String arFile : f.arFiles) { arFile = new
				 * String(arFile.getBytes("iso-8859-1"), "GBK"); logger.info(arFile); }
				 */
				this.ftp.setControlEncoding("GBK");
				return true;
			}
		}
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
		return false;
	}

	/**
	 * 关闭数据链接
	 *
	 * @throws IOException
	 */
	public void disConnection() throws IOException {
		if (this.ftp.isConnected()) {
			this.ftp.disconnect();
		}
	}

	/**
	 * 递归遍历出目录下面所有文件
	 *
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束
	 * @throws IOException
	 */
	public void List(String pathName) throws IOException {
		if (pathName.startsWith("/") && pathName.endsWith("/")) {
			// 更换目录到当前目录
			this.ftp.changeWorkingDirectory(pathName);
			FTPFile[] files = this.ftp.listFiles();
			for (FTPFile file : files) {
				if (file.isFile()) {
					arFiles.add(pathName + file.getName());
				} else if (file.isDirectory()) {
					// 需要加此判断。否则，ftp默认将‘项目文件所在目录之下的目录（./）’与‘项目文件所在目录向上一级目录下的目录（../）’都纳入递归，这样下去就陷入一个死循环了。需将其过滤掉。
					if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
						List(pathName + file.getName() + "/");
					}
				}
			}
		}
	}

	/**
	 * 递归遍历目录下面指定的文件名
	 *
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束
	 * @param ext      文件的扩展名
	 * @throws IOException
	 */
	public void List(String pathName, String ext) throws IOException {
		if (pathName.startsWith("/") && pathName.endsWith("/")) {
			// 更换目录到当前目录
			this.ftp.changeWorkingDirectory(pathName);
			FTPFile[] files = this.ftp.listFiles();
			for (FTPFile file : files) {
				if (file.isFile()) {
					if (file.getName().endsWith(ext)) {
						arFiles.add(pathName + file.getName());
					}
				} else if (file.isDirectory()) {
					if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
						List(pathName + file.getName() + "/", ext);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		FTPListAllFiles f = new FTPListAllFiles(true);
		if (f.login("218.247.142.200", 80, "tarenacode", "code_2019")) {
			f.List("/", "*");
		}
		f.disConnection();
		for (String arFile : f.arFiles) {
			logger.info(arFile);
		}

	}
}