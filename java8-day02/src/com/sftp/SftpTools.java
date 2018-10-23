package com.sftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * sftp 基础类库
 * 类描述: 通过配置代码连接sftp及提供相关工具函数
 * 说明:	1:本工具不提供除配置连接方式之外的连接方式
 * 		2:构造函数必须传入参数获取配置信息
 * 		3:本工具只支持sftp配置连接方式,不支持ftp等其它方式.
 */
public class SftpTools {
	private Session session;
	private Channel channel;
	
	private String host;
	private String username;
	private String password;
	private int port;
	
	/** 对外可访问 ChannelSftp对象提供的所有底层方法*/
	public ChannelSftp chnSftp;
	/**配置的远程目录地址*/
	public String cfgRemotePath;
	/**配置的远程目录历史地址	*/
	public String cfgRemotePathHis;
	/**文件类型*/
	public static final int FILE_TYPE = 1;
	/**目录类型*/
	public static final int DIR_TYPE = 2;
	/**配置的本地地址*/
	public  String cfgLocalPath;
	/**配置的本地历史地址*/
	public String cfgLocalPathHis;
	/**配置的本地临时地址*/
	public String cfgLocalPathTemp;
	
	/**
	 * 说明:构造函数必须传入配置中的ftpPathCode对应的值,注意检查正确性
	 * @param sftpPathCode
	 * @throws Exception
	 */
	public SftpTools(String sftpPathCode) throws Exception {
		//可以根据 sftpPathCode得到数据库或者文件配置的主机端口用户密码等信息
		
//		setHost("127.0.0.1");
//		setPort(22);
//		setUsername("newtouch");
//		setPassword("newtouch");
//		cfgRemotePath = "";
//		cfgRemotePathHis ="";
//		cfgLocalPath ="";
//		cfgLocalPathHis = "";
//		cfgLocalPathTemp ="";
		//cfgRemotePath="/";// test

	}
	/**
	 * 通过配置 打开sftp连接资源
	 * @throws JSchException
	 * @throws SftpException
	 */
	public void open () throws JSchException,SftpException {
//		this.connect(this.getHost(), this.getPort(), this.getUsername(), this.getPassword());
	}
	/**
	 *  连接SFTP
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @throws JSchException
	 * @throws SftpException
	 */
	public void connect (String host, int port, String username, String password) throws JSchException,SftpException {
		JSch jsch = new JSch();
		session = jsch.getSession(username, host, port);
		//System.out.println("Session created.");
		session.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.connect();
		//System.out.println("Session connected.");
		channel = session.openChannel("sftp");
		//System.out.println("Channel is Opened!");
		channel.connect();
		//System.out.println("Channel is connected!");
		chnSftp = (ChannelSftp) channel;
		//System.out.println("Connected to " + host + "  is sucess!");
	}



	/**
	 * 进入指定的目录并设置为当前目录
	 * @param sftpPath
	 * @throws Exception
	 */
	public void cd (String sftpPath) throws SftpException {
		chnSftp.cd(sftpPath);
	}

	/**
	 * 得到当前用户当前工作目录地址
	 * @return 返回当前工作目录地址
	 * 
	 */
	public String pwd () {
		return chnSftp.pwd();
	}

	/**
	 * 根据目录地址,文件类型返回文件或目录列表
	 * @param directory 如:/home/newtouch/kftesthis/201006/08/
	 * @param fileType  如：FILE_TYPE或者DIR_TYPE
	 * @return 文件或者目录列表 List
	 * @throws SftpException 
	 * @throws Exception
	 * 
	 */
	public List<String> listFiles (String directory, int fileType) throws SftpException {
		List<String> fileList = new ArrayList<String>();
		if (isDirExist(directory)) {
			boolean itExist = false;
			Vector vector;
			vector = chnSftp.ls(directory);
			for (int i = 0; i < vector.size(); i++) {
				Object obj = vector.get(i);
				String str = obj.toString().trim();
				int tag = str.lastIndexOf(":") + 3;
				String strName = str.substring(tag).trim();
				itExist = isDirExist(directory + "/" + strName);
				if (fileType == FILE_TYPE) {
					if (!(itExist)) {
						fileList.add(directory + "/" + strName);
					}
				}
				if (fileType == DIR_TYPE) {
					if (itExist) {
						//目录列表中去掉目录名为.和..
						if (!(strName.equals(".") || strName.equals(".."))) {
							fileList.add(directory + "/" + strName);
						}
					}
				}

			}
		}
		return fileList;
	}

	/**
	 * 判断目录是否存在
	 * @param directory
	 * @return
	 * @throws SftpException
	 */
	public boolean isDirExist (String directory) throws SftpException {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = chnSftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

	/**
	 * 下载文件后返回流文件
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public InputStream getFile (String sftpFilePath) throws SftpException {
		if (isFileExist(sftpFilePath)) {
			return chnSftp.get(sftpFilePath);
		}
		return null;
	}

	/**
	 * 获取远程文件流
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public InputStream getInputStreamFile (String sftpFilePath) throws SftpException {
		return getFile(sftpFilePath);
	}

	/** 
	 * 获取远程文件字节流
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 * @throws IOException
	 */
	public ByteArrayInputStream getByteArrayInputStreamFile (String sftpFilePath) throws SftpException,IOException {
		if (isFileExist(sftpFilePath)) {
			byte[] srcFtpFileByte = inputStreamToByte(getFile(sftpFilePath));
			ByteArrayInputStream srcFtpFileStreams = new ByteArrayInputStream(srcFtpFileByte);
			return srcFtpFileStreams;
		}
		return null;
	}

	/**
	 * 删除远程
	 * 说明:返回信息定义以:分隔第一个为代码，第二个为返回信息
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public String delFile (String sftpFilePath) throws SftpException {
		String retInfo = "";
		if (isFileExist(sftpFilePath)) {
			chnSftp.rm(sftpFilePath);
			retInfo = "1:File deleted.";
		}
		else {
			retInfo = "2:Delete file error,file not exist.";
		}
		return retInfo;
	}

	/**
	 * 移动远程文件到目标目录
	 * @param srcSftpFilePath
	 * @param distSftpFilePath
	 * @return 返回移动成功或者失败代码和信息
	 * @throws SftpException
	 * @throws IOException
	 */
	public String moveFile (String srcSftpFilePath, String distSftpFilePath) throws SftpException,IOException {
		String retInfo = "";
		boolean dirExist = false;
		boolean fileExist = false;
		fileExist = isFileExist(srcSftpFilePath);
		dirExist = isDirExist(distSftpFilePath);
		if (!fileExist) {
			//文件不存在直接反回.
			return "0:file not exist !";
		}
		if (!(dirExist)) {
			//1建立目录
			createDir(distSftpFilePath);
			//2设置dirExist为true
			dirExist = true;
		}
		if (dirExist && fileExist) {

			String fileName = srcSftpFilePath.substring(srcSftpFilePath.lastIndexOf("/"), srcSftpFilePath.length());
			ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
			//二进制流写文件
			this.chnSftp.put(srcFtpFileStreams, distSftpFilePath + fileName);
			this.chnSftp.rm(srcSftpFilePath);
			retInfo = "1:move success!";
		}
		return retInfo;
	}
	/**
	 * 复制远程文件到目标目录
	 * @param srcSftpFilePath
	 * @param distSftpFilePath
	 * @return
	 * @throws SftpException
	 * @throws IOException
	 */
	public String copyFile (String srcSftpFilePath, String distSftpFilePath) throws SftpException,IOException {
		String retInfo = "";
		boolean dirExist = false;
		boolean fileExist = false;
		fileExist = isFileExist(srcSftpFilePath);
		dirExist = isDirExist(distSftpFilePath);
		if (!fileExist) {
			//文件不存在直接反回.
			return "0:file not exist !";
		}
		if (!(dirExist)) {
			//1建立目录
			createDir(distSftpFilePath);
			//2设置dirExist为true
			dirExist = true;
		}
		if (dirExist && fileExist) {

			String fileName = srcSftpFilePath.substring(srcSftpFilePath.lastIndexOf("/"), srcSftpFilePath.length());
			ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
			//二进制流写文件
			this.chnSftp.put(srcFtpFileStreams, distSftpFilePath + fileName);
			retInfo = "1:copy file success!";
		}
		return retInfo;
	}
		
	/**
	 * 创建远程目录
	 * @param sftpDirPath
	 * @return 返回创建成功或者失败的代码和信息
	 * @throws SftpException
	 */
	public String createDir (String sftpDirPath) throws SftpException {
		this.cd("/");
		if (this.isDirExist(sftpDirPath)) {
			return "0:dir is exist !";
		}
		String pathArry[] = sftpDirPath.split("/");
		for (String path : pathArry) {
			if (path.equals("")) {
				continue;
			}
			if (isDirExist(path)) {
				this.cd(path);
			}
			else {
				//建立目录
				this.chnSftp.mkdir(path);
				//进入并设置为当前目录
				this.chnSftp.cd(path);
			}
		}
		this.cd("/");
		return "1:创建目录成功";
	}

	/**
	 * 判断远程文件是否存在
	 * @param srcSftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public boolean isFileExist (String srcSftpFilePath) throws SftpException {
		boolean isExitFlag = false;
		// 文件大于等于0则存在文件
		if (getFileSize(srcSftpFilePath) >= 0) {
			isExitFlag = true;
		}
		return isExitFlag;
	}

	/** 得到远程文件大小
	 * @see   返回文件大小
	 * @param srcSftpFilePath
	 * @return 返回文件大小，如返回-2 文件不存在，-1文件读取异常
	 * @throws SftpException
	 */
	public long getFileSize (String srcSftpFilePath) throws SftpException {
		long filesize = 0;//文件大于等于0则存在
		try {
			SftpATTRS sftpATTRS = chnSftp.lstat(srcSftpFilePath);
			filesize = sftpATTRS.getSize();
		} catch (Exception e) {
			filesize = -1;//获取文件大小异常
			if (e.getMessage().toLowerCase().equals("no such file")) {
				filesize = -2;//文件不存在
			}
		}
		return filesize;
	}

	/**
	 * 关闭资源
	 */
	public void close () {
		if (channel.isConnected()) {
			channel.disconnect();
			//System.out.println("Channel connect  disconnect!");

		}
		if (session.isConnected()) {
			session.disconnect();
			//System.out.println("Session connect disconnect!");
		}
	}

	/**
	 * inputStream类型转换为byte类型
	 * @param iStrm
	 * @return
	 * @throws IOException
	 */
	public byte[] inputStreamToByte (InputStream iStrm) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = iStrm.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	

	/* 实例编程参考
	public static void main (String[] args) {
		SftpTools stpTool=null;
		try {
			//0 模拟操作变量定义
			String srcPath="/home/newtouch/kftest";
			String hisPath="/home/newtouch/kftesthis";
				String listDirPath=srcPath+"/201201/01";
			String opTargetDirPath=hisPath+"/201201/01";
				  String opSourceDirPath=listDirPath+"/601235";
			String copyTargetDirPath=opTargetDirPath+"/601235";
			String moveTargetDirPath=opTargetDirPath+"/601235";
			String file="/title.txt";
			stpTool=new SftpTools("SFTP_TEST_PATH");//创建sftp工具对象
			//0 打开资源
			stpTool.open();
			//1 得到当前工作目录地址
			System.out.println("操作1 得到当前工作目录地址："+stpTool.pwd());
			System.out.println("操作1 配置的远程目录："+stpTool.cfgRemotePath);
			//2 改变目录为配置的远程目录
			stpTool.cd(stpTool.cfgRemotePath);
			System.out.println("操作2 改变目录为配置的远程目录："+stpTool.pwd());
			//3 取文件目录列表
			List<String> floderList=stpTool.listFiles(listDirPath, stpTool.DIR_TYPE);
			//4 取文件列表
			List<String> fileList=stpTool.listFiles(opSourceDirPath, stpTool.FILE_TYPE);
			System.out.println("操作3 读取目录地址:"+listDirPath);
			for (String sfd : floderList)
			{
				System.out.println("操作3 目录列表："+sfd);
			}
			System.out.println("操作4 读取文件地址:"+opSourceDirPath);
			for (String fills : fileList)
			{
				System.out.println("操作4 文件列表："+fills);
			}
			//5 ++++++++++下载文件 开始++++++++++
			InputStream   stream;
			System.out.println("操作5 下载文件:"+opSourceDirPath+file);
			stream=stpTool.getFile(opSourceDirPath+file);
						if (stream != null) {
							BufferedReader br = new BufferedReader(new InputStreamReader(stream));
							String data = null;
							try {
								while ((data = br.readLine()) != null) {
									System.out.println("操作5 文件内容:"+data);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							//关闭过程文件流
							   stream.close();
						}

	       //  ++++++++++下载文件 结束++++++++++
			//-------------复制文件 开始---------------
						
			//-------------复制文件 结束---------------
						 System.out.println("操作6 复制文件:"+opSourceDirPath+file);
						 System.out.println("操作6 复制文件到目录:"+copyTargetDirPath);
						 System.out.println("操作6 文件复制是否成功? "+stpTool.copyFile(opSourceDirPath+file, copyTargetDirPath));
		   //6 ---------删除文件开始------------
				    System.out.println("操作7 即将删除刚才复制的文件:"+copyTargetDirPath+file);
				   System.out.println("操作7 删除刚才复制的文件是否成功? "+stpTool.delFile(copyTargetDirPath+file));
		   // ---------删除文件结束------------
			//7 ++++++++++++++目录是否存在+++++++++++++++++++	
			  System.out.println("操作8 目录:"+opSourceDirPath+"是否存在?"+stpTool.isDirExist(opSourceDirPath));
		   //+++++++++++++++目录是否存在+++++++++++++++		    
			  //------------建立目录开始--------------
			  String dir2=opSourceDirPath+"/aa";
			  System.out.println("操作9 创建目录:"+dir2);
			  System.out.println("操作9 创建目录是否成功? "+stpTool.createDir(dir2));
		  //-------------建立目录结束-------------
		  //++++++++++++9 移动文件开始-----
				 System.out.println("操作10 移动文件:"+opSourceDirPath+file);
				 System.out.println("操作10 移动到历史目录:"+moveTargetDirPath+file);
				 System.out.println("操作10 移动文件是否成功?:"+stpTool.moveFile(opSourceDirPath+file, moveTargetDirPath));
				 System.out.println("操作10 恢复刚才移动掉的源文件是否成功? "+stpTool.copyFile(moveTargetDirPath+file, opSourceDirPath));
		  //+++++++++++++移动文件结束+++++++++++
				    
			} catch (Exception e) {
				//关闭资源
				stpTool.close();
				e.printStackTrace();
			}finally
			{
				//关闭资源
				stpTool.close();
			}

	}
	*/
}
