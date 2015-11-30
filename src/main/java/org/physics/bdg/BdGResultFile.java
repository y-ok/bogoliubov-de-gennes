package org.physics.bdg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import no.uib.cipr.matrix.Vector;

/**
 * BdG算出結果ファイルクラス
 * 
 * @author y-ok
 *
 */
public class BdGResultFile {

	// BdG算出結果情報クラス
	private BdGResultInfo resultInfo = new BdGResultInfo();

	// パス情報クラス
	private PathInfo pathInfo;

	/**
	 * コンストラクタ
	 * 
	 * @param a_pathInfo
	 */
	BdGResultFile(BdGResultInfo a_BdGRePathInfo, PathInfo a_pathInfo) {
		pathInfo = a_pathInfo;
	}

	/**
	 * BdG算出結果情報を取得する
	 * 
	 * @return BdG算出結果情報
	 */
	public BdGResultInfo getBdGResultInfo() {
		return resultInfo;
	}

	/**
	 * BdG算出結果情報を設定する
	 * 
	 * @param resultInfo
	 *            BdG算出結果情報
	 */
	public void setBdGResultInfo(BdGResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	/**
	 * 粒子数算出結果を出力する
	 * 
	 * @param temperature
	 *            温度
	 * 
	 */
	public void writeResultOrderParameter(double temperature) {

		String strOutputFilePath = pathInfo.getStrPwd() + "/" + pathInfo.getOutputPath() + "T=" + String.valueOf(temperature) + ".txt";

		File particleNumberResultFile = new File(strOutputFilePath);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(particleNumberResultFile)));

			Vector vecUpSpinParticleNumber = resultInfo.getVecUpSpinParticleNumber();
			Vector vecDownSpinParticleNumber = resultInfo.getVecDownSpinParticleNumber();
			Vector vecPairPotentialEnergy = resultInfo.getVecPairPotentialEnergy();

			int N = (int) Math.sqrt(vecUpSpinParticleNumber.size() / 2);

			DecimalFormat df = new DecimalFormat("0.000000");
			pw.println("# x座標 \t y座標 \t アップスピン粒子数 \t ダウンスピン粒子数 \t 超流動パラメータ \t 磁化");
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					pw.println(i + "\t" + j + "\t" + df.format(vecUpSpinParticleNumber.get(i * N + j)) + "\t" + df.format(vecDownSpinParticleNumber.get(i * N + j)) + "\t" + df.format(vecPairPotentialEnergy.get(i * N + j)) + "\t" + df.format(vecUpSpinParticleNumber.get(i * N + j) - vecDownSpinParticleNumber.get(i * N + j)));
				}
				pw.println(""); // これを入れないと、gnuplotで正しく表示されない
			}
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("算出エリア");
		System.out.println(strOutputFilePath);
	}
}
