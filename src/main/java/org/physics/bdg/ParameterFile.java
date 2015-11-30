package org.physics.bdg;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;

/**
 * パラメータファイルクラス
 * 
 * @author y-ok
 */
public class ParameterFile {

	private final static String PARAMETER = "parameter";
	private final static String LATTICE_SIZE = "lattice-size";
	private final static String HOPPING_ENERGY = "hopping-energy";
	private final static String PAIR_POTENTIAL = "pair-potential";
	private final static String INTERPARTICLE_INTERACTION = "interparticle-interaction";
	private final static String EXTERNAL_POTENTIAL = "external-potential";
	private final static String PARTICLE_NUMBER = "particle-number";
	private final static String UP_SPIN = "up-spin";
	private final static String DOWN_SPIN = "down-spin";
	private final static String ITERATION_NUMBER = "iteration-number";
	private final static String STRENGTH = "strength";
	private final static String INIT_VALUE = "init-value";
	private final static String COEFFICIENT = "coefficient";
	private final static String BOLTZMANNCONST = "boltzmannConst";
	private final static String TEMPERATURE = "temperature";
	private final static String WEIGHT_FACTOR = "weight-factor";
	private final static String CHEMICAL_POTENTIAL = "chemical-potential";
	private final static String MAGNETIC_FIELD = "magnetic-field";
	private final static String CONVERGENCE_JUDGE_VALUE = "convergenceJudgeValue";
	private final static String USE_SPARSE_MATRIX = "useSparseMatrix";

	private static ParamInfo paramInfo = new ParamInfo();

	/**
	 * パラメータ設定情報を取得する
	 * 
	 * @return パラメータ設定情報
	 */
	public ParamInfo getParamInfo() {
		return paramInfo;
	}

	private Document document;

	/**
	 * パラメータ設定ファイルの読み込み
	 * 
	 * @param strParameterSettingXmlFile パラメータ設定ファイルパス
	 */
	public void readParameterSetting(String strParameterSettingXmlFile) {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(strParameterSettingXmlFile);

			String strLatticeSizeXPath = "/" + PARAMETER + "/" + LATTICE_SIZE;
			int latticeSize = new Integer(getParameterValue(strLatticeSizeXPath)).intValue();
			paramInfo.setLatticeSize(latticeSize);

			String strHoppingEnergyXPath = "/" + PARAMETER + "/" + HOPPING_ENERGY + "/" + STRENGTH;
			double hoppingEnergy = new Double(getParameterValue(strHoppingEnergyXPath)).doubleValue();
			paramInfo.setHoppingEnergy(hoppingEnergy);

			String strPairPotentialXPath = "/" + PARAMETER + "/" + PAIR_POTENTIAL + "/" + INIT_VALUE;
			double pairPotential = new Double(getParameterValue(strPairPotentialXPath)).doubleValue();
			paramInfo.setPairPotential(pairPotential);

			String strInterparticleInteractionXPath = "/" + PARAMETER + "/" + INTERPARTICLE_INTERACTION + "/" + STRENGTH;
			double interparticleInteraction = new Double(getParameterValue(strInterparticleInteractionXPath)).doubleValue();
			paramInfo.setInterparticleInteraction(interparticleInteraction);

			String strExternalPotentialXPath = "/" + PARAMETER + "/" + EXTERNAL_POTENTIAL + "/" + COEFFICIENT;
			double externalPotential = new Double(getParameterValue(strExternalPotentialXPath)).doubleValue();
			paramInfo.setExternalPotentialCoefficient(externalPotential);

			String strUpSpinParticleNumXPath = "/" + PARAMETER + "/" + PARTICLE_NUMBER + "/" + UP_SPIN + "/" + INIT_VALUE;
			int upSpinParticleNum = new Integer(getParameterValue(strUpSpinParticleNumXPath)).intValue();
			paramInfo.setUpSpinParticleNum(upSpinParticleNum);

			String strDownSpinParticleNumXPath = "/" + PARAMETER + "/" + PARTICLE_NUMBER + "/" + DOWN_SPIN + "/" + INIT_VALUE;
			int downSpinParticleNum = new Integer(getParameterValue(strDownSpinParticleNumXPath)).intValue();
			paramInfo.setDownSpinParticleNum(downSpinParticleNum);

			String strIterationNumXPath = "/" + PARAMETER + "/" + ITERATION_NUMBER;
			int iterationNum = new Integer(getParameterValue(strIterationNumXPath)).intValue();
			paramInfo.setIterationNum(iterationNum);

			String strBoltzmannConstXPath = "/" + PARAMETER + "/" + BOLTZMANNCONST;
			double boltzmannConst = new Double(getParameterValue(strBoltzmannConstXPath)).doubleValue();
			paramInfo.setBoltzmannConst(boltzmannConst);
			
			String strTemperatureXPath = "/" + PARAMETER + "/" + TEMPERATURE;
			List<Double> temperaturelist = Lists.newArrayList();
			String[] temperature = getParameterValue(strTemperatureXPath).split(",");
			for (String strtemperature : temperature) {
				temperaturelist.add(new Double(strtemperature));
			}
			paramInfo.setTemperature(temperaturelist);
			
			String strWeightFactorXPath = "/" + PARAMETER + "/" + WEIGHT_FACTOR;
			double weightFactor = new Double(getParameterValue(strWeightFactorXPath)).doubleValue();
			paramInfo.setWeightFactor(weightFactor);

			String strChemicalPotentialXPath = "/" + PARAMETER + "/" + CHEMICAL_POTENTIAL;
			double chemicalPotential = new Double(getParameterValue(strChemicalPotentialXPath)).doubleValue();
			paramInfo.setChmicalPotential(chemicalPotential);

			String strMagneticFieldXPath = "/" + PARAMETER + "/" + MAGNETIC_FIELD;
			double magneticField = new Double(getParameterValue(strMagneticFieldXPath)).doubleValue();
			paramInfo.setMagneticField(magneticField);

			String strConvergenceJudgeValueXPath = "/" + PARAMETER + "/" + CONVERGENCE_JUDGE_VALUE;
			double convergenceJudgeValue = new Double(getParameterValue(strConvergenceJudgeValueXPath)).doubleValue();
			paramInfo.setConvergenceJudgeValue(convergenceJudgeValue);

			String strUseSparseMatrixXPath = "/" + PARAMETER + "/" + USE_SPARSE_MATRIX;
			boolean useSparseMatrix = new Boolean(getParameterValue(strUseSparseMatrixXPath));
			paramInfo.setUseSparseMatrix(useSparseMatrix);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * パラメータ値を取得する
	 * 
	 * @param xPath
	 * @return パラメータ値
	 */
	String getParameterValue(String xPath) {
		return document.valueOf(xPath);
	}

}
