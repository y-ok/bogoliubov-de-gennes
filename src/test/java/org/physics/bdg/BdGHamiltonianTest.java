package org.physics.bdg;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

import org.junit.Test;
import org.physics.bdg.BdGHamiltonian.OrderParameters;
import org.physics.bdg.BdGHamiltonian.SpinState;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@SuppressWarnings("unused")
public class BdGHamiltonianTest {
	
	private final double DELTA = 1.0e-6;

	/**
	 * コンストラクタのテストです
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_BdGHamiltonian_N001() throws Exception {

		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(3);
		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);

		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);

		assertThat((int) getPrivateField(hamiltonian, "m_latticeSize"), is(3));
		assertThat((int) getPrivateField(hamiltonian, "m_matrixSize"), is(18));
		assertThat((double) getPrivateField(hamiltonian, "m_HoppingEnergy"), is(1.0));
		assertThat((double) getPrivateField(hamiltonian, "m_potentialStrength"), is(0.06));
		assertThat((double) getPrivateField(hamiltonian, "m_boltzmannfactor"), is(1000.0));
		assertThat((double) getPrivateField(hamiltonian, "m_pairPotential"), is(1.0));
		assertThat((double) getPrivateField(hamiltonian, "m_upParticleNumber"), is(1.0));
		assertThat((double) getPrivateField(hamiltonian, "m_downParticleNumber"), is(1.0));
		assertThat((double) getPrivateField(hamiltonian, "m_upChemicalPotential"), is(0.3));
		assertThat((double) getPrivateField(hamiltonian, "m_downChemicalPotential"), is(0.3));
		assertThat((double) getPrivateField(hamiltonian, "m_interparticleInteractionStrength"), is(-6.0));
	}

	/**
	 * 粒子数、超流動パラメータ初期値を取得するメソッドのテストです
	 * 
	 * @throws Exception 入出力関係
	 */
	@Test
	public void test_getInitOrderParameter_N001() throws Exception {
		
		ParameterFile parameterFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
		when(parameterFile.getParamInfo().getLatticeSize()).thenReturn(3);
		when(parameterFile.getParamInfo().getPairPotential()).thenReturn(1.0);
		when(parameterFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
		when(parameterFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
		
		BdGHamiltonian hamiltonian = new BdGHamiltonian(parameterFile, null);
		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
		
		// 超流動パラメータ初期値
		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
		double[] arraySuperfluidOrderParameter = ((DenseVector) superfluidOrderParameter).getData();
		
		int matrixSize = (int)getPrivateField(hamiltonian, "m_matrixSize");
		double[] actualSuperfluidOrderParameter = new double[matrixSize];
		for (int i = 0; i < actualSuperfluidOrderParameter.length; i++) {
			actualSuperfluidOrderParameter[i] = 1.0;
		}
		assertThat(arraySuperfluidOrderParameter, is(actualSuperfluidOrderParameter));
		
		// アップスピン粒子数初期値
		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
		double[] arrayUpSpinParticleNumber = ((DenseVector) upSpinParticleNumber).getData();
		
		double[] actualUpSpinParticlueNumber = new double[matrixSize];
		for (int i = 0; i < actualUpSpinParticlueNumber.length; i++) {
			actualUpSpinParticlueNumber[i] = 1.0;
		}
		assertThat(arrayUpSpinParticleNumber, is(actualUpSpinParticlueNumber));
		
		// ダウンスピン粒子数初期値
		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
		double[] arrayDownSpinParticleNumber = ((DenseVector) downSpinParticleNumber).getData();
		
		double[] actualDownSpinParticleNumber = new double[matrixSize];
		for (int i = 0; i < actualDownSpinParticleNumber.length; i++) {
			actualDownSpinParticleNumber[i] = 1.0;
		}
		assertThat(arrayDownSpinParticleNumber, is(actualDownSpinParticleNumber));
	}
	
	/**
	 * BdGハミルトニアンを作成するメソッドのテストです</br>
	 * 8 * 8 行列
	 * @throws IOException 
	 */
//	@Test
//	public void test_makeBdGHamiltonian_N001() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(2);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		double[] arrayBdGHamiltonian = ((DenseMatrix) bdGHamiltonian).getData();
//		
//		double[] actualBdGHamiltonian = {
//				-6.27,	-2,	-2,	0,	1,	0,	0,	0,	
//				-2,	-6.27,	0,	-2,	0,	1,	0,	0,	
//				-2,	0,	-6.27,	-2,	0,	0,	1,	0,	
//				0,	-2,	-2,	-6.27,	0,	0,	0,	1,	
//				1,	0,	0,	0,	6.27,	2,	2,	0,	
//				0,	1,	0,	0,	2,	6.27,	0,	2,	
//				0,	0,	1,	0,	2,	0,	6.27,	2,	
//				0,	0,	0,	1,	0,	2,	2,	6.27
//		};
//		assertThat(actualBdGHamiltonian, is(arrayBdGHamiltonian));
//	}
//	
//	/**
//	 * BdGハミルトニアンを作成するメソッドのテストです</br>
//	 * 18 * 18 行列
//	 * @throws IOException 
//	 */
//	@Test
//	public void test_makeBdGHamiltonian_N002() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(3);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		double[] arrayBdGHamiltonian = ((DenseMatrix) bdGHamiltonian).getData();
//				
//		double[] actualBdGHamiltonian = {
//				-6.18,	-1,	-1,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	
//				-1,	-6.24,	-1,	0,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	
//				-1,	-1,	-6.18,	0,	0,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	
//				-1,	0,	0,	-6.24,	-1,	-1,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	
//				0,	-1,	0,	-1,	-6.3,	-1,	0,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	
//				0,	0,	-1,	-1,	-1,	-6.24,	0,	0,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	
//				-1,	0,	0,	-1,	0,	0,	-6.18,	-1,	-1,	0,	0,	0,	0,	0,	0,	1,	0,	0,	
//				0,	-1,	0,	0,	-1,	0,	-1,	-6.24,	-1,	0,	0,	0,	0,	0,	0,	0,	1,	0,	
//				0,	0,	-1,	0,	0,	-1,	-1,	-1,	-6.18,	0,	0,	0,	0,	0,	0,	0,	0,	1,	
//				1,	0,	0,	0,	0,	0,	0,	0,	0,	6.18,	1,	1,	1,	0,	0,	1,	0,	0,	
//				0,	1,	0,	0,	0,	0,	0,	0,	0,	1,	6.24,	1,	0,	1,	0,	0,	1,	0,	
//				0,	0,	1,	0,	0,	0,	0,	0,	0,	1,	1,	6.18,	0,	0,	1,	0,	0,	1,	
//				0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	0,	0,	6.24,	1,	1,	1,	0,	0,	
//				0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	0,	1,	6.3,	1,	0,	1,	0,	
//				0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	1,	1,	6.24,	0,	0,	1,	
//				0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	6.18,	1,	1,	
//				0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	1,	6.24,	1,	
//				0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	1,	1,	6.18
//		};
//		assertThat(actualBdGHamiltonian, is(arrayBdGHamiltonian));
//	}
//	
//	/**
//	 * 超流動パラメータを算出するメソッドのテストです</br>
//	 * 8 * 8行列
//	 * @throws IOException 
//	 */
//	@Test
//	public void test_calcSuperfluidOrderParameter_N001() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(2);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		
//		// BdGハミルトニアン対角化
//		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
//		int info = solver.solveEigenValueEq(bdGHamiltonian);
//				
//		Multimap<Double, Vector> mapEigenValueVector = ArrayListMultimap.create();
//		mapEigenValueVector = solver.getEigenValueAndEigenVectorMap();
//		
//		// 超流動パラメータ算出
//		superfluidOrderParameter = hamiltonian.calcSuperfluidOrderParameter(mapEigenValueVector);
//		double[] arraySuperfluidOrderParameter = ((DenseVector) superfluidOrderParameter).getData();
//		Double[] tmpArraySuperfluidOrderParameter = new Double[arraySuperfluidOrderParameter.length];
//		for (int i = 0; i < arraySuperfluidOrderParameter.length; i++) {
//			tmpArraySuperfluidOrderParameter[i] = arraySuperfluidOrderParameter[i];
//		}
//		
//		List<Double> listSuperfluidOrderParameter = Lists.newLinkedList();
//		listSuperfluidOrderParameter = getRidOf(tmpArraySuperfluidOrderParameter, 0.0);
//		
//		double[] actualSuperfluidOrderParameter = {
//				0.611291,	0.611291,	0.611291,	0.611291
//		};
//		Arrays.sort(actualSuperfluidOrderParameter);
//
//		Double[] newArraySuperfluidOrderParameter = (Double[])listSuperfluidOrderParameter.toArray(new Double[0]);
//		for (int i = 0; i < newArraySuperfluidOrderParameter.length; i++) {
//			newArraySuperfluidOrderParameter[i] = Math.abs(newArraySuperfluidOrderParameter[i]);
//		}
//		Arrays.sort(newArraySuperfluidOrderParameter);
//
//		assertThat(info, is(0));
//		for (int i = 0; i < newArraySuperfluidOrderParameter.length; i++) {
//			assertEquals(actualSuperfluidOrderParameter[i], newArraySuperfluidOrderParameter[i], DELTA);
//		}
//	}
//	
//	/**
//	 * 超流動パラメータを算出するメソッドのテストです</br>
//	 * 18 * 18行列
//	 * @throws IOException 
//	 */
//	@Test
//	public void test_calcSuperfluidOrderParameter_N002() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(3);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		
//		// BdGハミルトニアン対角化
//		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
//		int info = solver.solveEigenValueEq(bdGHamiltonian);
//		
//		Multimap<Double, Vector> mapEigenValueVector = ArrayListMultimap.create();
//		mapEigenValueVector = solver.getEigenValueAndEigenVectorMap();
//		
//		// 超流動パラメータ算出
//		superfluidOrderParameter = hamiltonian.calcSuperfluidOrderParameter(mapEigenValueVector);
//		double[] arraySuperfluidOrderParameter = ((DenseVector) superfluidOrderParameter).getData();
//		Double[] tmpArraySuperfluidOrderParameter = new Double[arraySuperfluidOrderParameter.length];
//		for (int i = 0; i < arraySuperfluidOrderParameter.length; i++) {
//			tmpArraySuperfluidOrderParameter[i] = arraySuperfluidOrderParameter[i];
//		}
//		
//		List<Double> listSuperfluidOrderParameter = Lists.newLinkedList();
//		listSuperfluidOrderParameter = getRidOf(tmpArraySuperfluidOrderParameter, 0.0);
//
//		double[] actualSuperfluidOrderParameter = {
//				0.526497,	0.521016,	0.526497,	0.521016,	0.515642,	0.521016,	0.526497,	0.521016,	0.526497
//		};
//		Arrays.sort(actualSuperfluidOrderParameter);
//		
//		Double[] newArraySuperfluidOrderParameter = (Double[])listSuperfluidOrderParameter.toArray(new Double[0]);
//		for (int i = 0; i < newArraySuperfluidOrderParameter.length; i++) {
//			newArraySuperfluidOrderParameter[i] = Math.abs(newArraySuperfluidOrderParameter[i]);
//		}
//		Arrays.sort(newArraySuperfluidOrderParameter);
//		
//		assertThat(info, is(0));
//		for (int i = 0; i < newArraySuperfluidOrderParameter.length; i++) {
//			assertEquals(actualSuperfluidOrderParameter[i], newArraySuperfluidOrderParameter[i], DELTA);
//		}
//	}
//	
//	/**
//	 * アップスピン&ダウンスピン粒子数を算出するメソッドのテストです</br>
//	 * (8 * 8)行列
//	 * @throws IOException 
//	 */
//	@Test
//	public void test_calcAverageParticleNum_N001() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(2);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		
//		// BdGハミルトニアン対角化
//		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
//		int info = solver.solveEigenValueEq(bdGHamiltonian);
//		assertThat(info, is(0));
//				
//		Multimap<Double, Vector> mapEigenValueVector = ArrayListMultimap.create();
//		mapEigenValueVector = solver.getEigenValueAndEigenVectorMap();
//		
//		// アップスピン&ダウンスピン粒子数算出
//		Map<SpinState, Vector> mapParticleNum = hamiltonian.calcAverageParticleNum(mapEigenValueVector);
//		
//		// アップスピン粒子数取得
//		upSpinParticleNumber = mapParticleNum.get(SpinState.UP_STATE);
//		double[] arrayUpSpinParticleNumber = ((DenseVector) upSpinParticleNumber).getData();
//		Double[] tmpArrayUpSpinParticleNumber = new Double[arrayUpSpinParticleNumber.length];
//		for (int i = 0; i < arrayUpSpinParticleNumber.length; i++) {
//			tmpArrayUpSpinParticleNumber[i] = arrayUpSpinParticleNumber[i];
//		}
//		
//		List<Double> listUpSpinParticleNumber = Lists.newLinkedList();
//		listUpSpinParticleNumber = getRidOf(tmpArrayUpSpinParticleNumber, 0.0);
//		
//		Double[] newArrayUpSpinParticleNumber = (Double[])listUpSpinParticleNumber.toArray(new Double[0]);
//		for (int i = 0; i < newArrayUpSpinParticleNumber.length; i++) {
//			newArrayUpSpinParticleNumber[i] = Math.abs(newArrayUpSpinParticleNumber[i]);
//		}
//		Arrays.sort(newArrayUpSpinParticleNumber);
//		
//		double[] actualUpSpinParticleNumber = {
//				0.985683,	0.985683,	0.985683,	0.985683
//		};
//		Arrays.sort(actualUpSpinParticleNumber);
//		
//		for (int i = 0; i < newArrayUpSpinParticleNumber.length; i++) {
//			assertEquals(newArrayUpSpinParticleNumber[i], actualUpSpinParticleNumber[i], DELTA);
//		}
//		
//		// ダウンスピン粒子数取得
//		downSpinParticleNumber = mapParticleNum.get(SpinState.DOWN_STATE);
//		double[] arrayDownSpinParticleNumber = ((DenseVector) downSpinParticleNumber).getData();
//		Double[] tmpArrayDownSpinParticleNumber = new Double[arrayDownSpinParticleNumber.length];
//		for (int i = 0; i < arrayDownSpinParticleNumber.length; i++) {
//			tmpArrayDownSpinParticleNumber[i] = arrayDownSpinParticleNumber[i];
//		}
//		List<Double> listDownSpinParticleNumber = Lists.newLinkedList();
//		listDownSpinParticleNumber = getRidOf(tmpArrayDownSpinParticleNumber, 0.0);
//		
//		Double[] newArrayDownSpinParticleNumber = (Double[])listDownSpinParticleNumber.toArray(new Double[0]);
//		for (int i = 0; i < newArrayDownSpinParticleNumber.length; i++) {
//			newArrayDownSpinParticleNumber[i] = Math.abs(newArrayDownSpinParticleNumber[i]);
//		}
//		Arrays.sort(newArrayDownSpinParticleNumber);
//		
//		double[] actualDownSpinParticleNumber = {
//				0.985683,	0.985683,	0.985683,	0.985683
//		};
//		Arrays.sort(actualDownSpinParticleNumber);
//		
//		for (int i = 0; i < newArrayDownSpinParticleNumber.length; i++) {
//			assertEquals(newArrayDownSpinParticleNumber[i], actualDownSpinParticleNumber[i], DELTA);
//		}
//	}
//	
//	/**
//	 * アップスピン&ダウンスピン粒子数を算出するメソッドのテストです</br>
//	 * (18 * 18)行列
//	 * @throws IOException 
//	 */
//	@Test
//	public void test_calcAverageParticleNum_N002() throws IOException {
//		
//		ParameterFile paramFile = mock(ParameterFile.class, RETURNS_DEEP_STUBS);
//		when(paramFile.getParamInfo().getLatticeSize()).thenReturn(3);
//		when(paramFile.getParamInfo().getHoppingEnergy()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getExternalPotentialCoefficient()).thenReturn(0.06);
//		when(paramFile.getParamInfo().getBoltzmannConst()).thenReturn(1000.0);
//		when(paramFile.getParamInfo().getPairPotential()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getUpSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getDownSpinParticleNum()).thenReturn(1.0);
//		when(paramFile.getParamInfo().getChmicalPotential()).thenReturn(0.3);
//		when(paramFile.getParamInfo().getMagneticField()).thenReturn(0.0);
//		when(paramFile.getParamInfo().getInterparticleInteraction()).thenReturn(-6.0);
//
//		BdGHamiltonian hamiltonian = new BdGHamiltonian(paramFile, null);
//		OrderParameters orderParameters = hamiltonian.getInitOrderParameter();
//		
//		// 超流動パラメータ
//		Vector superfluidOrderParameter = orderParameters.getInitVecPairPotentialEnergy();
//		// アップスピン粒子数
//		Vector upSpinParticleNumber = orderParameters.getInitVecUpParticleNumber();
//		// ダウンスピン粒子数
//		Vector downSpinParticleNumber = orderParameters.getInitVecDownParticleNumber();
//		
//		// BdGハミルトニアン作成
//		hamiltonian.makeBdGHamiltonian(superfluidOrderParameter, upSpinParticleNumber, downSpinParticleNumber);
//
//		// BdGハミルトニアン取得
//		Matrix bdGHamiltonian = hamiltonian.getBdGHamiltonian();
//		
//		// BdGハミルトニアン対角化
//		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
//		int info = solver.solveEigenValueEq(bdGHamiltonian);
//		assertThat(info, is(0));
//				
//		Multimap<Double, Vector> mapEigenValueVector = ArrayListMultimap.create();
//		mapEigenValueVector = solver.getEigenValueAndEigenVectorMap();
//		
//		// アップスピン&ダウンスピン粒子数算出
//		Map<SpinState, Vector> mapParticleNum = hamiltonian.calcAverageParticleNum(mapEigenValueVector);
//		
//		// アップスピン粒子数取得
//		upSpinParticleNumber = mapParticleNum.get(SpinState.UP_STATE);
//		double[] arrayUpSpinParticleNumber = ((DenseVector) upSpinParticleNumber).getData();
//		Double[] tmpArrayUpSpinParticleNumber = new Double[arrayUpSpinParticleNumber.length];
//		for (int i = 0; i < arrayUpSpinParticleNumber.length; i++) {
//			tmpArrayUpSpinParticleNumber[i] = arrayUpSpinParticleNumber[i];
//		}
//		
//		List<Double> listUpSpinParticleNumber = Lists.newLinkedList();
//		listUpSpinParticleNumber = getRidOf(tmpArrayUpSpinParticleNumber, 0.0);
//		
//		Double[] newArrayUpSpinParticleNumber = (Double[])listUpSpinParticleNumber.toArray(new Double[0]);
//		for (int i = 0; i < newArrayUpSpinParticleNumber.length; i++) {
//			newArrayUpSpinParticleNumber[i] = Math.abs(newArrayUpSpinParticleNumber[i]);
//		}
//		Arrays.sort(newArrayUpSpinParticleNumber);
//		
//		double[] actualUpSpinParticleNumber = {
//				0.991526,	0.991705,	0.991526,	0.991705,	0.99188,	0.991705,	0.991526,	0.991705,	0.991526
//		};
//		Arrays.sort(actualUpSpinParticleNumber);
//		
//		for (int i = 0; i < newArrayUpSpinParticleNumber.length; i++) {
//			assertEquals(newArrayUpSpinParticleNumber[i], actualUpSpinParticleNumber[i], DELTA);
//		}
//		
//		// ダウンスピン粒子数取得
//		downSpinParticleNumber = mapParticleNum.get(SpinState.DOWN_STATE);
//		double[] arrayDownSpinParticleNumber = ((DenseVector) downSpinParticleNumber).getData();
//		Double[] tmpArrayDownSpinParticleNumber = new Double[arrayDownSpinParticleNumber.length];
//		for (int i = 0; i < arrayDownSpinParticleNumber.length; i++) {
//			tmpArrayDownSpinParticleNumber[i] = arrayDownSpinParticleNumber[i];
//		}
//		List<Double> listDownSpinParticleNumber = Lists.newLinkedList();
//		listDownSpinParticleNumber = getRidOf(tmpArrayDownSpinParticleNumber, 0.0);
//		
//		Double[] newArrayDownSpinParticleNumber = (Double[])listDownSpinParticleNumber.toArray(new Double[0]);
//		for (int i = 0; i < newArrayDownSpinParticleNumber.length; i++) {
//			newArrayDownSpinParticleNumber[i] = Math.abs(newArrayDownSpinParticleNumber[i]);
//		}
//		Arrays.sort(newArrayDownSpinParticleNumber);
//		
//		double[] actualDownSpinParticleNumber = {
//				0.991526,	0.991705,	0.991526,	0.991705,	0.99188,	0.991705,	0.991526,	0.991705,	0.991526
//		};
//		Arrays.sort(actualDownSpinParticleNumber);
//		
//		for (int i = 0; i < newArrayDownSpinParticleNumber.length; i++) {
//			assertEquals(newArrayDownSpinParticleNumber[i], actualDownSpinParticleNumber[i], DELTA);
//		}
//	}

	/**
	 * private変数の値を取得する
	 * 
	 * @param target_object
	 * @param field_name
	 * @return private変数の値
	 * @throws Exception
	 */
	public static Object getPrivateField(Object target_object, String field_name) throws Exception {
		Class<? extends Object> c = target_object.getClass();
		Field fld = c.getDeclaredField(field_name);
		fld.setAccessible(true);
		return fld.get(target_object);
	}
	
	/**
	 * 指定した値を除去する
	 * 
	 * @param array
	 * @param value
	 * @return valueを除去したリスト
	 */
	private static <T> List<T> getRidOf(T[] array, T value) {
		List<T> list = Lists.newArrayList();
		for (T valueOfArray : array) {
			list.add(valueOfArray);
		}
		list.removeAll(Arrays.asList(value));
		return list;
	}
}
