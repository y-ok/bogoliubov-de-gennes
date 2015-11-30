package org.physics.bdg;

/**
 * パス情報クラス
 * 
 * @author y-ok
 */
public class PathInfo {

	// カレントパス
	private String strPwd;
	// パラメータファイルパス
	private String parameterPath;
	// 結果出力パス
	private String outputPath;
	// ログ出力パス
	private String logPath;

	private String sparsePath;

	/**
	 * パラメータファイルパスを取得する
	 * 
	 * @return パラメータファイルパス
	 */
	public String getParameterPath() {
		return parameterPath;
	}

	/**
	 * パラメータファイルパスを設定する
	 * 
	 * @param parameterPath パラメータファイルパス
	 */
	public void setParameterPath(String parameterPath) {
		this.parameterPath = parameterPath;
	}

	/**
	 * 結果出力パスを取得する
	 * 
	 * @return 結果出力パス
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * 結果出力パスを設定する
	 * 
	 * @param outputPath 結果出力パス
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * ログ出力パスを取得する
	 * 
	 * @return ログ出力パス
	 */
	public String getLogPath() {
		return logPath;
	}

	/**
	 * ログ出力パスを
	 * 
	 * @param logPath ログ出力パス
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	/**
	 * カレントパスを取得する
	 * 
	 * @return カレントパス
	 */
	public String getStrPwd() {
		return strPwd;
	}

	/**
	 * カレントパスを設定する
	 * 
	 * @param strPwd カレントパス
	 */
	public void setStrPwd(String strPwd) {
		this.strPwd = strPwd;
	}
	
	/**
	 * 疎行列情報ファイルパスを取得する
	 * @return 疎行列情報ファイルパス
	 */
	public String getSparsePath() {
		return sparsePath;
	}

	/**
	 * 疎行列情報ファイルパスを設定する
	 * @param sparsePath 疎行列情報ファイルパス
	 */
	public void setSparsePath(String sparsePath) {
		this.sparsePath = sparsePath;
	}

}
