package org.physics.bdg;

import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;

/**
 * BdG数値計算結果クラス
 * 
 * @author y-ok
 *
 */
public class BdGResultInfo {

	// 全粒子数
	private double m_totalParticleNumber;

	// アップスピン粒子数
	private Vector m_vecUpSpinParticleNumber;

	// ダウンスピン粒子数
	private Vector m_vecDownSpinParticleNumber;

	// 超流動パラメータ
	private Vector m_vecPairPotentialEnergy;

	/**
	 * アップスピン粒子数を取得する
	 * 
	 * @return スピン粒子数
	 */
	public Vector getVecUpSpinParticleNumber() {
		return m_vecUpSpinParticleNumber;
	}

	/**
	 * アップスピン粒子を設定する
	 * 
	 * @param m_vecUpSpinParticleNumber
	 *            アップスピン粒子数
	 */
	public void setVecUpSpinParticleNumber(Vector m_vecUpSpinParticleNumber) {
		this.m_vecUpSpinParticleNumber = m_vecUpSpinParticleNumber;
	}

	/**
	 * ダウンスピン粒子を取得する
	 * 
	 * @return ダウンスピン粒子数
	 */
	public Vector getVecDownSpinParticleNumber() {
		return m_vecDownSpinParticleNumber;
	}

	/**
	 * ダウンスピン粒子を設定する
	 * 
	 * @param m_vecDownSpinParticleNumber
	 *            ダウンスピン粒子数
	 */
	public void setVecDownSpinParticleNumber(Vector m_vecDownSpinParticleNumber) {
		this.m_vecDownSpinParticleNumber = m_vecDownSpinParticleNumber;
	}

	/**
	 * 超流動パラメータを取得する
	 * 
	 * @return 超流動パラメータ
	 */
	public Vector getVecPairPotentialEnergy() {
		return m_vecPairPotentialEnergy;
	}

	/**
	 * 超流動パラメータを設定する
	 * 
	 * @param m_vecPairPotentialEnergy
	 *            超流動パラメータ
	 */
	public void setVecPairPotentialEnergy(Vector m_vecPairPotentialEnergy) {
		this.m_vecPairPotentialEnergy = m_vecPairPotentialEnergy;
	}

	/**
	 * 全粒子数を取得する
	 * 
	 * @return 全粒子数
	 */
	public double getTotalParticleNumber() {
		calcTotalParticleNumber();
		return m_totalParticleNumber;
	}

	/**
	 * 全粒子数を算出する
	 */
	private void calcTotalParticleNumber() {

		double upSpinParticleNumber = 0.0;
		for (VectorEntry e : m_vecUpSpinParticleNumber) {
			upSpinParticleNumber += e.get();
		}

		double downSpinParticleNumber = 0.0;
		for (VectorEntry e : m_vecDownSpinParticleNumber) {
			downSpinParticleNumber += e.get();
		}

		// 全粒子数
		m_totalParticleNumber = upSpinParticleNumber + downSpinParticleNumber;
	}

}
