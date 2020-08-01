package com.fh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 功能描述：文件下载工具类
 * @author Ajie
 * @date 2019/11/9 0009
 */
public class FileDownloadUtil {

	private static Logger logger = LoggerFactory.getLogger(FileDownloadUtil.class);


	/**
	 * @param response
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception{
		// 获取文件后缀名{ .xxx } 形式
		String nameSuffix = filePath.substring(filePath.lastIndexOf("."));
		fileName += nameSuffix;
		byte[] data = FileUtil.toByteArray2(filePath);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}

	/** 这个方法下载最快
	 * @param response 响应
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 */
	public static void fileDownload1(final HttpServletResponse response, String filePath, String fileName) {
		// 获取文件后缀名{ .xxx } 形式
		String nameSuffix = filePath.substring(filePath.lastIndexOf("."));
		fileName += nameSuffix;
		// 得到要下载的文件
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				// 注意text/html，和application/html
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print("<html><body><script type='text/javascript'>alert('您要下载的资源已被删除！');</script></body></html>");
				response.getWriter().close();
				logger.debug("您要下载的资源已被删除！！");
				return;
			}
			// 创建缓冲区
			byte[] data = FileUtil.toByteArray2(filePath);
			// 转码，免得文件名中文乱码
			fileName = URLEncoder.encode(fileName, "UTF-8");
			// 清空response
			response.reset();
			// 更改文件名 设置一个响应头，无论是否被浏览器解析，都下载
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 设置文件下载长度
			response.addHeader("Content-Length", "" + data.length);
			// 设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("application/octet-stream;charset=UTF-8");
			// 创建缓冲输出流
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			// 开始输出
			outputStream.write(data);
			outputStream.flush();
			// 关闭输出流
			outputStream.close();
			// 刷新缓冲区
			response.flushBuffer();
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * 文件下载方法
	 * @param response 响应
	 * @param filePath 文件完整路径(包括文件名和扩展名)
	 * @param encode 编码 默认为UTF-8
	 */
	public static void download(HttpServletResponse response, String filePath, String encode) {
		if (Tools.isEmpty(encode)) {
			encode = "UTF-8";
		}
		response.setContentType("text/html;charset=" + encode);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String downLoadPath = filePath;
		try {
			File file = new File(downLoadPath);
			long fileLength = file.length();
			String fileName = file.getName();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(encode), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
		}
	}

	/**
	 * 以流的方式下载
	 * @param response 响应
	 * @param filePath 文件完整路径(包括文件名和扩展名)
	 * @param encode 编码 默认为UTF-8
	 * @return response
	 */
	public static HttpServletResponse downloadStream(HttpServletResponse response, String filePath, String encode) {
		if (Tools.isEmpty(encode)) {
			encode = "UTF-8";
		}
		response.setContentType("text/html;charset=" + encode);
		try {
			// path是指欲下载的文件的路径
			File file = new File(filePath);
			// 取得文件名
			String filename = file.getName();
			// 取得文件的后缀名
			// String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
			// 以流的形式下载文件
			InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(encode), "ISO8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
		return response;
	}

	/**
	 * 下载本地文件
	 * @param response 响应
	 * @param filePath 文件完整路径（包括文件名和拓展名）
	 * @param encode 编码 默认为UTF-8编码 默认为UTF-8
	 */
	public static void downloadLocal(HttpServletResponse response, String filePath,String encode) {
		if (Tools.isEmpty(encode)) {
			encode = "UTF-8";
		}
		response.setContentType("text/html;charset=" + encode);
		try {
			// 读到流中
			InputStream inStream = new FileInputStream(filePath); // 文件的存放路径
			// path是指欲下载的文件的路径
			File file = new File(filePath);
			// 取得文件名
			String fileName = file.getName();
			// 设置输出的格式
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(encode), "ISO8859-1") + "\"");
			// 循环取出流中的数据
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
			}
			inStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}






}
