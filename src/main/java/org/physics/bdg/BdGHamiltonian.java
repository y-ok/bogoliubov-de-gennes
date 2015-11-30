package org.physics.bdg;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Bogoliubov de Gennes (BdG) Hamiltonian クラス
 * 
 * @author y-ok
 *
 */
public class BdGHamiltonian {

	// BdGハミルトニアン
	private static Matrix m_BdGHamiltonian;

	// 格子サイズ
	private static int m_latticeSize;

	// BdGハミルトニアンの行列サイズ
	private static int m_matrixSize;

	// ホッピングパラメータ
	private static double m_HoppingEnergy;

	// アップスピン粒子の化学ポテンシャル
	private static double m_upChemicalPotential;

	// ダウンスピン粒子の化学ポテンシャル
	private static double m_downChemicalPotential;

	// 外部ポテンシャルの強さ
	private static double m_potentialStrength;

	// 粒子間相互作用
	private static double m_interparticleInteractionStrength;

	// ボルツマン定数
	private static double m_boltzmannfactor;

	// 超流動パラメータ
	private static double m_pairPotential;

	// アップスピン粒子数
	private static double m_upParticleNumber;

	// ダウンスピン粒子数
	private static double m_downParticleNumber;

	// 収束判定値
	private static double m_convergenceJudgeValue;

	// 収束重み因子
	private static double m_weightFactor;

	// 超流動パラメータ差分最大値
	private static double m_maxDifferenceOfSuperfluidOrderParameter = 0.0;

	private static boolean m_useSparseMatrix;

	private static PathInfo m_pathInfo;

	// スピン状態
	public static enum SpinState {
		UP_STATE, DOWN_STATE;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param paramXmlInfo
	 */
	BdGHamiltonian(ParameterFile paramXmlInfo, PathInfo pathInfo) {
		ParamInfo paramInfo = paramXmlInfo.getParamInfo();
		m_latticeSize = paramInfo.getLatticeSize();
		m_matrixSize = 2 * (int) Math.pow(m_latticeSize, 2);
		m_HoppingEnergy = paramInfo.getHoppingEnergy();
		m_potentialStrength = paramInfo.getExternalPotentialCoefficient();
		m_boltzmannfactor = paramInfo.getBoltzmannConst();
		m_pairPotential = paramInfo.getPairPotential();
		m_upParticleNumber = paramInfo.getUpSpinParticleNum();
		m_downParticleNumber = paramInfo.getDownSpinParticleNum();
		m_upChemicalPotential = paramInfo.getChmicalPotential() + paramInfo.getMagneticField();
		m_downChemicalPotential = paramInfo.getChmicalPotential() - paramInfo.getMagneticField();
		m_interparticleInteractionStrength = paramInfo.getInterparticleInteraction();
		m_convergenceJudgeValue = paramInfo.getConvergenceJudgeValue();
		m_weightFactor = paramInfo.getWeightFactor();
		m_useSparseMatrix = paramInfo.getUseSparseMatrix();
		m_pathInfo = pathInfo;
	}

	/**
	 * 粒子数&超流動パラメータクラス
	 * 
	 * @author y-ok
	 */
	public class OrderParameters {
		private Vector m_vecPairPotentialEnergy;
		private Vector m_vecUpParticleNumber;
		private Vector m_vecDownParticleNumber;

		/**
		 * 粒子数&超流動パラメータ初期化
		 */
		private void initOrderParameters() {
			double[] UpParticleNum = new double[m_matrixSize];
			Vector vecUpParticleNum = new DenseVector(UpParticleNum);
			for (VectorEntry e : vecUpParticleNum) {
				e.set(m_upParticleNumber);
			}
			m_vecUpParticleNumber = vecUpParticleNum;

			double[] DownParticleNum = new double[m_matrixSize];
			Vector vecDownParticleNum = new DenseVector(DownParticleNum);
			for (VectorEntry e : vecDownParticleNum) {
				e.set(m_downParticleNumber);
			}
			m_vecDownParticleNumber = vecDownParticleNum;

			double[] pairPotentialEnergy = new double[m_matrixSize];
			Vector vecPairPotentialEnergy = new DenseVector(pairPotentialEnergy);
			for (VectorEntry e : vecPairPotentialEnergy) {
				e.set(m_pairPotential);
			}
			m_vecPairPotentialEnergy = vecPairPotentialEnergy;
		}

		/**
		 * 超流動パラメータ初期値を取得する
		 * 
		 * @return 超流動パラメータ初期値
		 */
		public Vector getInitVecPairPotentialEnergy() {
			return m_vecPairPotentialEnergy;
		}

		/**
		 * アップスピン粒子数初期値を取得する
		 * 
		 * @return アップスピン粒子数初期値
		 */
		public Vector getInitVecUpParticleNumber() {
			return m_vecUpParticleNumber;
		}

		/**
		 * ダウンスピン粒子数初期値を取得する
		 * 
		 * @return ダウンスピン粒子数初期値
		 */
		public Vector getInitVecDownParticleNumber() {
			return m_vecDownParticleNumber;
		}
	}

	/**
	 * 粒子数、超流動パラメータ初期値取得
	 * 
	 * @return 粒子数、超流動パラメータ初期値
	 */
	public OrderParameters getInitOrderParameter() {
		OrderParameters orderparam = new OrderParameters();
		orderparam.initOrderParameters();
		return orderparam;
	}

	/**
	 * BdG Hamiltonianを作成する
	 * 
	 * @param pairPotentialEnergy 超流動パラメータ
	 * @param upParticleNumber アップスピン粒子数
	 * @param downParticleNumber ダウンスピン粒子数
	 * @throws IOException 入出力関係
	 */
	public void makeBdGHamiltonian(Vector pairPotentialEnergy, Vector upParticleNumber, Vector downParticleNumber, double temperature) throws IOException {

		double[][] matValues = new double[m_matrixSize][m_matrixSize];
		Matrix Hamiltonian = new DenseMatrix(matValues);

		for (int i = 0; i < m_matrixSize; i++) {
			for (int j = 0; j < m_matrixSize; j++) {

				/*-------------------------------
				 * This term is Hopping energy.
				 *-------------------------------*/
				if (i < m_matrixSize / 2 && j < m_matrixSize / 2) {
					if ((i + m_latticeSize == j) || (i == j + m_latticeSize)) {
						Hamiltonian.add(i, j, -m_HoppingEnergy);
					}

					if ((i + m_latticeSize * (m_latticeSize - 1) == j) || (i == j + m_latticeSize * (m_latticeSize - 1))) {
						Hamiltonian.add(i, j, -m_HoppingEnergy);
					}

					if ((i + 1 == j) || (i == j + 1)) {
						Hamiltonian.add(i, j, -m_HoppingEnergy);
					}

					for (int k = 0; k <= m_latticeSize; k++) {
						if ((i == m_latticeSize * k + (m_latticeSize - 1) && j == m_latticeSize * k) || (j == m_latticeSize * k + (m_latticeSize - 1) && i == m_latticeSize * k)) {
							Hamiltonian.add(i, j, -m_HoppingEnergy);
						}
					}

					for (int k = 0; k < m_latticeSize; k++) {
						if ((i == m_latticeSize * k + m_latticeSize && j == m_latticeSize * k + (m_latticeSize - 1)) || (i == m_latticeSize * k + (m_latticeSize - 1) && j == m_latticeSize * k + m_latticeSize)) {
							Hamiltonian.set(i, j, 0.0);
						}
					}

				} else if (i >= m_matrixSize / 2 && j >= m_matrixSize / 2 && i < m_matrixSize && j < m_matrixSize) {
					if ((i + m_latticeSize == j) || (i == j + m_latticeSize)) {
						Hamiltonian.add(i, j, m_HoppingEnergy);
					}

					if ((i + m_latticeSize * (m_latticeSize - 1) == j) || (i == j + m_latticeSize * (m_latticeSize - 1))) {
						Hamiltonian.add(i, j, m_HoppingEnergy);
					}

					if ((i + 1 == j) || (i == j + 1)) {
						Hamiltonian.add(i, j, m_HoppingEnergy);
					}

					for (int k = 0; k <= (int) Math.pow(m_latticeSize, 2); k++) {
						if ((i == m_latticeSize * k + (m_latticeSize - 1) && j == m_latticeSize * k) || (j == m_latticeSize * k + (m_latticeSize - 1) && i == m_latticeSize * k)) {
							Hamiltonian.add(i, j, m_HoppingEnergy);
						}
					}

					for (int k = 0; k < (int) Math.pow(m_latticeSize, 2); k++) {
						if ((i == m_latticeSize * k + m_latticeSize && j == m_latticeSize * k + (m_latticeSize - 1)) || (i == m_latticeSize * k + (m_latticeSize - 1) && j == m_latticeSize * k + m_latticeSize)) {
							Hamiltonian.set(i, j, 0.0);
						}
					}
				} // end of Hopping Energy term.

				/*--------------------------------
				 * This term is Hartree Potential.
				 *--------------------------------*/
				if (i < m_matrixSize / 2 && j < m_matrixSize / 2) {
					if (i == j) {
						Hamiltonian.add(i, j, -m_upChemicalPotential);
					}
				} else if (i >= m_matrixSize / 2 && i < m_matrixSize && j >= m_matrixSize / 2 && j < m_matrixSize) {
					if (i == j) {
						Hamiltonian.add(i, j, m_downChemicalPotential);
					}
				} // end of Hartree Potential term.

				/*-----------------------------------------
				 * This term is the Superfluid Gap effect.
				 *-----------------------------------------*/
				if ((i < m_matrixSize / 2 && j >= m_matrixSize / 2 && j < m_matrixSize) && (i + m_matrixSize / 2 == j)) {
					Hamiltonian.add(i, j, pairPotentialEnergy.get(i));
				} else if ((i >= m_matrixSize / 2 && i < m_matrixSize && j < m_matrixSize / 2) && (i == j + m_matrixSize / 2)) {
					Hamiltonian.add(i, j, pairPotentialEnergy.get(j));
				}
			}
		}

		/*-------------------------------------------
		 * The term is an external Potential effect.
		 *-------------------------------------------*/
		for (int i = m_matrixSize / 2; i < m_matrixSize; i++) {
			for (int k = 0; k < m_latticeSize; k++) {
				if (i >= m_latticeSize * k + m_matrixSize / 2 && i < m_latticeSize * (k + 1) + m_matrixSize / 2) {
					double x = (-((double) m_latticeSize - 1) / 2) + (double) i - (double) (m_matrixSize / 2) - (double) (m_latticeSize) * (double) k;
					double y = ((double) m_latticeSize - 1) / 2 - (double) k;
					// Hamiltonian.add(i, i, -getExternalPotential(x, y, 12, 8) - m_interparticleInteractionStrength * upParticleNumber.get(i - m_matrixSize / 2));
					Hamiltonian.add(i, i, -getExternalPotential(x, y) - m_interparticleInteractionStrength * upParticleNumber.get(i - m_matrixSize / 2));
				}
			}
		}

		for (int i = 0; i < m_matrixSize / 2; i++) {
			for (int k = 0; k < m_latticeSize; k++) {
				if (i >= m_latticeSize * k && i < m_latticeSize * (k + 1)) {
					double x = (-((double) m_latticeSize - 1) / 2) + (double) i - (double) m_latticeSize * (double) k;
					double y = ((double) m_latticeSize - 1) / 2 - (double) k;
					// Hamiltonian.add(i, i, getExternalPotential(x, y, 12, 8) + m_interparticleInteractionStrength * downParticleNumber.get(i));
					Hamiltonian.add(i, i, getExternalPotential(x, y) + m_interparticleInteractionStrength * downParticleNumber.get(i));
				}
			}
		} // end of an external Potential term.

		if (m_useSparseMatrix) {
			SparseMatrixFileIF sparseMatrixData = new SparseMatrixFileIF(m_pathInfo);
			sparseMatrixData.setSparseMatrix(Hamiltonian);
			sparseMatrixData.writeSparseMatrixData();
		}

		setBdGHamiltonian(Hamiltonian);
	} // end of makeBdGHamiltonian

	/**
	 * アップスピン&ダウンスピン粒子数を算出する
	 * 
	 * @param mapEigenValueAndEigenVector 固有値、固有ベクトルマップ
	 * @param temperature 温度
	 * @return スピン状態とスピン状態に対応する粒子数のマップ
	 */
	public Map<SpinState, Vector> calcAverageParticleNum(Multimap<Double, Vector> mapEigenValueAndEigenVector, double temperature) {

		Map<SpinState, Vector> mapAverageParticleNum = Maps.newHashMap();

		double[] UpParticleNum = new double[m_matrixSize];
		Vector vecUpParticleNum = new DenseVector(UpParticleNum);

		double[] DownParticleNum = new double[m_matrixSize];
		Vector vecDownParticleNum = new DenseVector(DownParticleNum);

		for (int j = 0; j < m_matrixSize / 2; j++) {
			vecUpParticleNum.set(j, 0.0);
			vecDownParticleNum.set(j, 0.0);

			for (Map.Entry<Double, Vector> e : mapEigenValueAndEigenVector.entries()) {
				Double eigenValue = e.getKey();
				Vector eigenVector = e.getValue();
				vecUpParticleNum.add(j, Math.pow(eigenVector.get(j), 2) * fermiDistributionfnc(eigenValue, temperature));
				vecDownParticleNum.add(j, Math.pow(eigenVector.get(j + m_matrixSize / 2), 2) * fermiDistributionfnc(-eigenValue, temperature));
			}

			vecUpParticleNum.set(j, vecUpParticleNum.get(j));
			vecDownParticleNum.set(j, vecDownParticleNum.get(j));
		}

		mapAverageParticleNum.put(SpinState.UP_STATE, vecUpParticleNum);
		mapAverageParticleNum.put(SpinState.DOWN_STATE, vecDownParticleNum);

		return mapAverageParticleNum;
	} // end of calcAverageParticleNum

	/**
	 * 超流動パラメータを算出する
	 * 
	 * @param mapEigenValueAndEigenVector
	 * @return 超流動パラメータ
	 */
	public Vector calcSuperfluidOrderParameter(Multimap<Double, Vector> mapEigenValueAndEigenVector, double temperature) {

		double[] pairPotential = new double[m_matrixSize];
		Vector vecPairPotential = new DenseVector(pairPotential);

		for (int j = 0; j < m_matrixSize / 2; j++) {
			double sumPairPotential = 0.0;
			for (Map.Entry<Double, Vector> e : mapEigenValueAndEigenVector.entries()) {
				Double eigenValue = e.getKey();
				Vector eigenVector = e.getValue();
				sumPairPotential += eigenVector.get(j) * eigenVector.get(j + m_matrixSize / 2) * fermiDistributionfnc(eigenValue, temperature);
			}
			vecPairPotential.set(j, m_interparticleInteractionStrength * sumPairPotential);
		}

		return vecPairPotential;
	} // end of calcSuperfluidOrderParameter

	/**
	 * 超流動パラメータ収束判定
	 * 
	 * @param oldSuperfluidOrderParameter 前超流動パラメータ
	 * @param newSuperfluidOrderParameter 新超流動パラメータ
	 * @return true (収束) / false (収束していない)
	 */
	public boolean isConvergenceOfSuperfluidOrderParameter(Vector oldSuperfluidOrderParameter, Vector newSuperfluidOrderParameter) {

		double[] arrayOldSuperfluidOrderParameter = ((DenseVector) oldSuperfluidOrderParameter).getData();
		double[] arrayNewSuperfluidOrderParameter = ((DenseVector) newSuperfluidOrderParameter).getData();

		List<Double> listDifferenceOfSuperfluidOrderParameters = Lists.newArrayListWithCapacity(arrayOldSuperfluidOrderParameter.length);

		for (int i = 0; i < arrayOldSuperfluidOrderParameter.length; i++) {
			listDifferenceOfSuperfluidOrderParameters.add(i, Math.abs(arrayNewSuperfluidOrderParameter[i] - arrayOldSuperfluidOrderParameter[i]));
		}
		Collections.sort(listDifferenceOfSuperfluidOrderParameters, new DoubleComparator());
		double newMaxDifferenceOfSuperfluidOrderParameter = Iterables.getLast(listDifferenceOfSuperfluidOrderParameters);

		if (Math.abs(newMaxDifferenceOfSuperfluidOrderParameter - m_maxDifferenceOfSuperfluidOrderParameter) < m_convergenceJudgeValue) {
			System.out.println("Convergence!!");
			return true;
		}
		m_maxDifferenceOfSuperfluidOrderParameter = newMaxDifferenceOfSuperfluidOrderParameter;

		double[] arrayUpdateSuperfluidOrderParameter = new double[arrayNewSuperfluidOrderParameter.length];
		for (int i = 0; i < arrayOldSuperfluidOrderParameter.length; i++) {
			arrayUpdateSuperfluidOrderParameter[i] = m_weightFactor * arrayOldSuperfluidOrderParameter[i] + (1.0 - m_weightFactor) * arrayNewSuperfluidOrderParameter[i];
		}
		Vector updateSuperfluidOrderParameter = new DenseVector(arrayUpdateSuperfluidOrderParameter);
		newSuperfluidOrderParameter = updateSuperfluidOrderParameter;

		return false;
	}

	/**
	 * 数値を比較するクラス
	 * 
	 * @author y-ok
	 */
	private class DoubleComparator implements Comparator<Double> {
		@Override
		public int compare(Double value1, Double value2) {
			return Double.compare(value1, value2);
		}
	}

	/**
	 * (x, y)における調和型外部ポテンシャルの強さを取得する
	 * 
	 * @param x
	 *            (x-position)
	 * @param y
	 *            (y-position)
	 * @return 外部ポテンシャルの強さ
	 */
	private static double getExternalPotential(double x, double y) {
		return m_potentialStrength * (Math.pow(x, 2) + Math.pow(y, 2));
	} // end of externalPotential

	/**
	 * (x, y)におけるトロイダル型外部ポテンシャルの強さを取得する
	 * 
	 * @param x
	 *            (x-position)
	 * @param y
	 *            (y-position)
	 * @param omega_ho
	 * @param omega_tr
	 * @return 外部ポテンシャルの強さ
	 */
	@SuppressWarnings("unused")
	private static double getExternalPotential(double x, double y, double omega_ho, double omega_tr) {
		double r = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
		return 0.01 * 0.5 * omega_ho * (Math.pow(r, 2)) + omega_tr * Math.exp(-r / 5);
	}

	/**
	 * フェルミ分布関数 Fermi Distribution function
	 * 
	 * @param x
	 *            (エネルギー)
	 * @return xにおけるフェルミ分布
	 */
	private static double fermiDistributionfnc(double x, double t) {
		return 1 / (Math.exp(x / (m_boltzmannfactor * t)) + 1);
	} // end of fermiDistribution

	/**
	 * 行列(M * M)サイズを取得するメソッド
	 * 
	 * @return M 行列サイズ
	 */
	public int getMatrixSize() {
		return m_matrixSize;
	}

	/**
	 * BdGハミルトニアンを取得する
	 * 
	 * @return BdGハミルトニアン
	 */
	public Matrix getBdGHamiltonian() {
		return m_BdGHamiltonian;
	}

	/**
	 * BdGハミルトニアンを設定する
	 * 
	 * @param matrix BdGハミルトニアン
	 */
	public static void setBdGHamiltonian(Matrix matrix) {
		m_BdGHamiltonian = matrix;
	}
}
