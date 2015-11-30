package org.physics.bdg;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.physics.bdg.BdGHamiltonian.OrderParameters;
import org.physics.bdg.BdGHamiltonian.SpinState;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 * BdGファサードクラス
 * 
 * @author y-ok
 *
 */
public class BdGFacade {

	private ParameterFile paramInfo = new ParameterFile();
	private PathInfo pathInfo = new PathInfo();
	private ParamInfo param = new ParamInfo();
	private Matrix BdGHamiltonian;
	private Vector vecPairPotentialEnergy;
	private Vector vecUpParticleNumber;
	private Vector vecDownParticleNumber;
	private List<BdGResultInfo> bdgResultList = Lists.newArrayList();

	/**
	 * パラメータ情報の読み込み
	 */
	public void setUp() {
		PathInfoFile pathInfoFile = new PathInfoFile();
		String strHome = System.getProperty("user.dir");
		String strPathInfoFilePath = strHome + "/conf/pathInfo.xml";
		pathInfoFile.readPathInfoFile(strPathInfoFilePath);
		pathInfo = pathInfoFile.getPathInfo();
		pathInfo.setStrPwd(strHome);
		String xmlFile = pathInfo.getStrPwd() + pathInfo.getParameterPath();
		paramInfo.readParameterSetting(xmlFile);
		param = paramInfo.getParamInfo();
	}

	/**
	 * 温度リストを取得する
	 * 
	 * @return 温度リスト
	 */
	public List<Double> getTemperatureList() {
		return param.getTemperature();
	}

	/**
	 * BdGハミルトニアンの作成
	 * 
	 * @param temperature
	 *            温度
	 * @return BdGハミルトニアン
	 * @throws IOException
	 *             入出力関係
	 */
	public BdGHamiltonian makeBdGHamiltonian(double temperature) throws IOException {

		BdGHamiltonian Hamiltonian = new BdGHamiltonian(paramInfo, pathInfo);

		// 粒子数&超流動パラメータ初期値取得
		OrderParameters orderparam = Hamiltonian.getInitOrderParameter();
		vecPairPotentialEnergy = orderparam.getInitVecPairPotentialEnergy();
		vecUpParticleNumber = orderparam.getInitVecUpParticleNumber();
		vecDownParticleNumber = orderparam.getInitVecDownParticleNumber();

		Hamiltonian.makeBdGHamiltonian(vecPairPotentialEnergy, vecUpParticleNumber, vecDownParticleNumber, temperature);
		BdGHamiltonian = Hamiltonian.getBdGHamiltonian();

		return Hamiltonian;
	}

	/**
	 * BdGハミルトニアンの対角化
	 * 
	 * @param Hamiltonian
	 *            BdGハミルトニアン
	 * @param temperature
	 *            温度
	 * @throws IOException
	 *             入出力関係
	 */
	public void solveEigenEq(BdGHamiltonian Hamiltonian, double temperature) throws IOException {

		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();

		System.out.println("Calculation is Start");
		ParamInfo param = paramInfo.getParamInfo();

		OrderParameters orderparameters = Hamiltonian.getInitOrderParameter();
		Vector tmpPairPotentialEnergy = orderparameters.getInitVecPairPotentialEnergy();

		for (int cnt = 0; cnt < param.getIterationNum(); cnt++) {

			int info = solver.solveEigenValueEq(BdGHamiltonian);
			if (0 != info) {
				System.out.println("Calculation is failure!");
			}

			Multimap<Double, Vector> mapEigenValueAndEigenVector = ArrayListMultimap.create();
			mapEigenValueAndEigenVector = solver.getEigenValueAndEigenVectorMap();

			// 超流動パラメータ算出
			vecPairPotentialEnergy = Hamiltonian.calcSuperfluidOrderParameter(mapEigenValueAndEigenVector, temperature);

			// 平均粒子数算出
			Map<SpinState, Vector> mapParticleNum = Hamiltonian.calcAverageParticleNum(mapEigenValueAndEigenVector, temperature);
			vecUpParticleNumber = mapParticleNum.get(SpinState.UP_STATE);
			vecDownParticleNumber = mapParticleNum.get(SpinState.DOWN_STATE);

			if (Hamiltonian.isConvergenceOfSuperfluidOrderParameter(tmpPairPotentialEnergy, vecPairPotentialEnergy)) {
				System.out.println("反復計算回数: " + cnt);
				break;
			} else {
				tmpPairPotentialEnergy = vecPairPotentialEnergy;
			}

			// BdGハミルトニアン更新
			Hamiltonian.makeBdGHamiltonian(vecPairPotentialEnergy, vecUpParticleNumber, vecDownParticleNumber, temperature);
			BdGHamiltonian = Hamiltonian.getBdGHamiltonian();
		}
	}

	/**
	 * 計算結果を追加する
	 */
	public void addBdGResult() {
		BdGResultInfo resultInfo = new BdGResultInfo();
		resultInfo.setVecUpSpinParticleNumber(vecUpParticleNumber);
		resultInfo.setVecDownSpinParticleNumber(vecDownParticleNumber);
		resultInfo.setVecPairPotentialEnergy(vecPairPotentialEnergy);
		bdgResultList.add(resultInfo);
	}

	/**
	 * 計算結果出力
	 */
	public void outputCalculationResult() {

		for (int i = 0; i < bdgResultList.size(); i++) {
			BdGResultFile BdGResult = new BdGResultFile(bdgResultList.get(i), pathInfo);
			BdGResult.setBdGResultInfo(bdgResultList.get(i));
			BdGResult.writeResultOrderParameter(getTemperatureList().get(i));
		}

		System.out.print("Calculation is End.");
	}
}
