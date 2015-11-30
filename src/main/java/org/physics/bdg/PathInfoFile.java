package org.physics.bdg;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * パス情報ファイルクラス
 * 
 * @author y-ok
 */
public class PathInfoFile {

	private final static String PATHINFO = "path-Info";
	private final static String PARAMETER = "parameter";
	private final static String OUTPUT = "output";
	private final static String LOG = "log";
	private final static String SPARSE = "sparse";

	private PathInfo pathInfo = new PathInfo();
	private Document document;

	/**
	 * パス情報を取得する
	 * 
	 * @return パス情報
	 */
	public PathInfo getPathInfo() {
		return pathInfo;
	}

	/**
	 * パス情報を設定する
	 * 
	 * @param pathInfo パス情報
	 */
	public void setPathInfo(PathInfo pathInfo) {
		this.pathInfo = pathInfo;
	}

	/**
	 * パス情報ファイル読み込み
	 * 
	 * @param strPathInfoFilePath パス情報ファイルパス
	 */
	public void readPathInfoFile(String strPathInfoFilePath) {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(strPathInfoFilePath);

			String strParameterXPath = "/" + PATHINFO + "/" + PARAMETER;
			String strParameter = getTagValue(strParameterXPath);
			pathInfo.setParameterPath(strParameter);

			String strOutputXPath = "/" + PATHINFO + "/" + OUTPUT;
			String strOutput = getTagValue(strOutputXPath);
			pathInfo.setOutputPath(strOutput);

			String strLogXPath = "/" + PATHINFO + "/" + LOG;
			String strLog = getTagValue(strLogXPath);
			pathInfo.setLogPath(strLog);

			String strSparseXPath = "/" + PATHINFO + "/" + SPARSE;
			String strSparse = getTagValue(strSparseXPath);
			pathInfo.setSparsePath(strSparse);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * タグの値を取得する
	 * 
	 * @param xPath
	 * @return タグ値
	 */
	String getTagValue(String xPath) {
		return document.valueOf(xPath);
	}

}
