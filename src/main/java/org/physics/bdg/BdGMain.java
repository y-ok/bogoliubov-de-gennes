package org.physics.bdg;

import java.io.IOException;
import java.util.List;

/**
 * 実行クラス
 * 
 * @author y-ok
 */
public class BdGMain {

	/**
	 * エントリーポイント
	 * 
	 * @param args 未使用変数
	 * @throws IOException 入出力変数
	 */
	public static void main(String[] args) throws IOException {

		BdGFacade bdGFacade = new BdGFacade();

		// パラメータ読み込み
		bdGFacade.setUp();

		List<Double> temperaturelist = bdGFacade.getTemperatureList();
		for (double temperature : temperaturelist) {
			// BdGハミルトニアン作成
			BdGHamiltonian hamiltonian = bdGFacade.makeBdGHamiltonian(temperature);

			// BdGハミルトニアン対角化
			bdGFacade.solveEigenEq(hamiltonian, temperature);
			
			bdGFacade.addBdGResult();
		}
		
		// 計算結果出力
		bdGFacade.outputCalculationResult();
	}
}