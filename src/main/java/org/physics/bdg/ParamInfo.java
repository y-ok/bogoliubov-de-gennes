package org.physics.bdg;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * パラメータ情報クラス
 * 
 * @author y-ok
 */
public class ParamInfo {

	private int m_latticeSize;
	private double m_hoppingEnergy;
	private double m_pairPotential;
	private double m_interparticleInteraction;
	private double m_externalPotentialCoefficient;
	private double m_upSpinParticleNum;
	private double m_downSpinParticleNum;
	private int m_iterationNum;
	private double m_boltzmannConst;
	private List<Double> m_temperatureList = Lists.newArrayList();
	private double m_weightFactor;
	private double m_chmicalPotential;
	private double m_magneticField;
	private double m_convergenceJudgeValue;
	private boolean m_useSparseMatrix;
	/**
	 * N * N 格子系のサイズを取得する
	 * 
	 * @return 格子系のサイズ(N)
	 */
	public int getLatticeSize() {
		return m_latticeSize;
	}

	/**
	 * N * N 格子系のサイズを設定する
	 * 
	 * @param m_latticeSize
	 *            格子サイズ
	 */
	public void setLatticeSize(int m_latticeSize) {
		this.m_latticeSize = m_latticeSize;
	}

	/**
	 * ホッピングエネルギー(t)を取得する
	 * 
	 * @return ホッピングエネルギー(t)
	 */
	public double getHoppingEnergy() {
		return m_hoppingEnergy;
	}

	/**
	 * ホッピングエネルギー(t)を設定する
	 * 
	 * @param m_hoppingEnergy
	 *            ホッピングエネルギー
	 */
	public void setHoppingEnergy(double m_hoppingEnergy) {
		this.m_hoppingEnergy = m_hoppingEnergy;
	}

	/**
	 * ペアポテンシャル(超流動パラメータ)を取得する
	 * 
	 * @return ペアポテンシャル(超流動パラメータ)
	 */
	public double getPairPotential() {
		return m_pairPotential;
	}

	/**
	 * ペアポテンシャル(超流動パラメータ)を設定する
	 * 
	 * @param m_pairPotential
	 *            超流動パラメータ
	 */
	public void setPairPotential(double m_pairPotential) {
		this.m_pairPotential = m_pairPotential;
	}

	/**
	 * 粒子間相互作用(U)を取得する
	 * 
	 * @return 粒子間相互作用(U)
	 */
	public double getInterparticleInteraction() {
		return m_interparticleInteraction;
	}

	/**
	 * 粒子間相互作用(U)を設定する
	 * 
	 * @param m_interparticleInteraction
	 *            粒子間相互作用
	 */
	public void setInterparticleInteraction(double m_interparticleInteraction) {
		this.m_interparticleInteraction = m_interparticleInteraction;
	}

	/**
	 * 外部ポテンシャル係数を取得する
	 * 
	 * @return 外部ポテンシャル係数
	 */
	public double getExternalPotentialCoefficient() {
		return m_externalPotentialCoefficient;
	}

	/**
	 * 外部ポテンシャル係数を設定する
	 * 
	 * @param m_externalPotentialCoefficient
	 *            外部ポテンシャル係数
	 */
	public void setExternalPotentialCoefficient(double m_externalPotentialCoefficient) {
		this.m_externalPotentialCoefficient = m_externalPotentialCoefficient;
	}

	/**
	 * アップスピン粒子数を取得する
	 * 
	 * @return アップスピン粒子数
	 */
	public double getUpSpinParticleNum() {
		return m_upSpinParticleNum;
	}

	/**
	 * アップスピン粒子数を設定する。
	 * 
	 * @param m_upSpinParticleNum
	 *            アップスピン粒子数
	 */
	public void setUpSpinParticleNum(double m_upSpinParticleNum) {
		this.m_upSpinParticleNum = m_upSpinParticleNum;
	}

	/**
	 * ダウンスピン粒子数を取得する
	 * 
	 * @return ダウンスピン粒子数
	 */
	public double getDownSpinParticleNum() {
		return m_downSpinParticleNum;
	}

	/**
	 * ダウンスピン粒子数を設定する
	 * 
	 * @param m_downSpinParticleNum
	 *            ダウンスピン粒子数
	 */
	public void setDownSpinParticleNum(double m_downSpinParticleNum) {
		this.m_downSpinParticleNum = m_downSpinParticleNum;
	}

	/**
	 * 反復計算回数を取得する
	 * 
	 * @return 反復計算回数
	 */
	public int getIterationNum() {
		return m_iterationNum;
	}

	/**
	 * 反復計算回数を設定する
	 * 
	 * @param m_iterationNum
	 *            反復計算回数
	 */
	public void setIterationNum(int m_iterationNum) {
		this.m_iterationNum = m_iterationNum;
	}

	/**
	 * Boltzmann系数を取得する
	 * 
	 * @return Boltzmann係数
	 */
	public double getBoltzmannConst() {
		return m_boltzmannConst;
	}

	/**
	 * Boltzmann係数を設定する
	 * 
	 * @param m_boltzmannConst
	 *            Boltzmann係数
	 */
	public void setBoltzmannConst(double m_boltzmannConst) {
		this.m_boltzmannConst = m_boltzmannConst;
	}

	/**
	 * 収束計算時使用する重み因子を取得する
	 * 
	 * @return 収束計算時使用する重み因子
	 */
	public double getWeightFactor() {
		return m_weightFactor;
	}

	/**
	 * 収束計算時使用する重み因子を設定する
	 * 
	 * @param m_weightFactor
	 *            重み因子
	 */
	public void setWeightFactor(double m_weightFactor) {
		this.m_weightFactor = m_weightFactor;
	}

	/**
	 * 化学ポテンシャルを取得する
	 * 
	 * @return 化学ポテンシャル
	 */
	public double getChmicalPotential() {
		return m_chmicalPotential;
	}

	/**
	 * 化学ポテンシャルを設定する
	 * 
	 * @param m_chmicalPotential
	 *            化学ポテンシャル
	 */
	public void setChmicalPotential(double m_chmicalPotential) {
		this.m_chmicalPotential = m_chmicalPotential;
	}

	/**
	 * 磁場を取得する
	 * 
	 * @return 磁場
	 */
	public double getMagneticField() {
		return m_magneticField;
	}

	/**
	 * 磁場を設定する
	 * 
	 * @param m_magneticField
	 *            磁場
	 */
	public void setMagneticField(double m_magneticField) {
		this.m_magneticField = m_magneticField;
	}

	/**
	 * 収束判定値を取得する
	 * 
	 * @return 収束判定値
	 */
	public double getConvergenceJudgeValue() {
		return m_convergenceJudgeValue;
	}

	/**
	 * 収束判定値を設定する
	 * 
	 * @param m_convergenceJudgeValue
	 *            収束判定値
	 */
	public void setConvergenceJudgeValue(double m_convergenceJudgeValue) {
		this.m_convergenceJudgeValue = m_convergenceJudgeValue;
	}

	/**
	 * 疎行列処理フラグを取得する
	 * 
	 * @return 疎行列処理フラグ
	 */
	public boolean getUseSparseMatrix() {
		return m_useSparseMatrix;
	}

	/**
	 * 疎行列処理フラグを設定する
	 * 
	 * @param m_useSparseMatrix
	 *            疎行列処理フラグ
	 */
	public void setUseSparseMatrix(boolean m_useSparseMatrix) {
		this.m_useSparseMatrix = m_useSparseMatrix;
	}

	/**
	 * 温度リストを取得する
	 * 
	 * @return 温度リスト
	 */
	public List<Double> getTemperature() {
		return m_temperatureList;
	}

	/**
	 * 温度リストを設定する
	 * 
	 * @param m_temperatureList
	 *            温度リスト
	 */
	public void setTemperature(List<Double> m_temperatureList) {
	}
}
