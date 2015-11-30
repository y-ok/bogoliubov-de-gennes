package org.physics.bdg;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

import org.junit.Test;

import com.google.common.collect.Multimap;

public class SolverOfEigenValueEqTest {
	
	static final double DELTA = 1.0e-6;
		
	@Test
	public void test_solveEigenValueEq_N001() {
		
		double[][] arrayMatrix = {
				{-6.18,	-1,	-1,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0},
				{-1,	-6.24,	-1,	0,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0},
				{-1,	-1,	-6.18,	0,	0,	-1,	0,	0,	-1,	0,	0,	1,	0,	0,	0,	0,	0,	0},
				{-1,	0,	0,	-6.24,	-1,	-1,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0},
				{0,	-1,	0,	-1,	-6.3,	-1,	0,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0},
				{0,	0,	-1,	-1,	-1,	-6.24,	0,	0,	-1,	0,	0,	0,	0,	0,	1,	0,	0,	0},
				{-1,	0,	0,	-1,	0,	0,	-6.18,	-1,	-1,	0,	0,	0,	0,	0,	0,	1,	0,	0},
				{0,	-1,	0,	0,	-1,	0,	-1,	-6.24,	-1,	0,	0,	0,	0,	0,	0,	0,	1,	0},
				{0,	0,	-1,	0,	0,	-1,	-1,	-1,	-6.18,	0,	0,	0,	0,	0,	0,	0,	0,	1},
				{1,	0,	0,	0,	0,	0,	0,	0,	0,	6.18,	1,	1,	1,	0,	0,	1,	0,	0},
				{0,	1,	0,	0,	0,	0,	0,	0,	0,	1,	6.24,	1,	0,	1,	0,	0,	1,	0},
				{0,	0,	1,	0,	0,	0,	0,	0,	0,	1,	1,	6.18,	0,	0,	1,	0,	0,	1},
				{0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	0,	0,	6.24,	1,	1,	1,	0,	0},
				{0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	0,	1,	6.3,	1,	0,	1,	0},
				{0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	1,	1,	1,	6.24,	0,	0,	1},
				{0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	0,	6.18,	1,	1},
				{0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	0,	1,	6.24,	1},
				{0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0,	1,	1,	1,	6.18}
		};
		Matrix matrix = new DenseMatrix(arrayMatrix);
		
		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
		int info = solver.solveEigenValueEq(matrix);
		assertThat(info, is(0));
		
		Multimap<Double, Vector> mapEigen = solver.getEigenValueAndEigenVectorMap();
		assertThat(18, is(mapEigen.size()));
		
		// 固有値
//		System.out.println("固有値");
//		List<Double> listEigenValue = Lists.newLinkedList(mapEigen.keys());
//		for (double value : listEigenValue) {
//			System.out.print(value + "\t");
//		}
//		System.out.println("");
		
		// 固有ベクトル
//		System.out.println("固有ベクトル");
//		List<Vector> listEigenVector = Lists.newLinkedList(mapEigen.values());
//		for (Vector vecValue : listEigenVector) {
//			for (VectorEntry e : vecValue) {
//				System.out.print(e.get() + "\t");
//			}
//			System.out.println("");
//		}
	}
	
	/**
	 * 固有ベクトル一覧を作成するメソッドのテストです。
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
//	@Test
//	public void test_makeEigenVectorlist_N001() 
//			throws IllegalAccessException, 
//			IllegalArgumentException, 
//			InvocationTargetException, 
//			NoSuchMethodException, 
//			SecurityException 
//	{	
//		Method method = SolverOfEigenValueEq.class.getDeclaredMethod("makeEigenVectorlist", double[].class, List.class);
//		method.setAccessible(true);
//		
//		final int row = 3;
//		final int col = 3;
//		
//		double[][] arrayMatrix = new double[row][col];
//		Matrix matrix = new DenseMatrix(arrayMatrix);
//		matrix.set(0, 0, 2);
//		matrix.set(0, 1, 1);
//		matrix.set(0, 2, 1);
//		matrix.set(1, 0, 1);
//		matrix.set(1, 1, 2);
//		matrix.set(1, 2, 1);
//		matrix.set(2, 0, 1);
//		matrix.set(2, 1, 1);
//		matrix.set(2, 2, 2);
//		
//		double[] array = ((DenseMatrix) matrix).getData();
//		List<Vector> eigenVectorList = Lists.newLinkedList();
//		
//		SolverOfEigenValueEq solver = new SolverOfEigenValueEq();
//		method.invoke(solver, array, eigenVectorList);
//		
//		assertEquals(matrix.get(0,0), eigenVectorList.get(0).get(0), DELTA);
//		assertEquals(matrix.get(0,1), eigenVectorList.get(0).get(1), DELTA);
//		assertEquals(matrix.get(0,2), eigenVectorList.get(0).get(2), DELTA);
//		
//		assertEquals(matrix.get(1,0), eigenVectorList.get(1).get(0), DELTA);
//		assertEquals(matrix.get(1,1), eigenVectorList.get(1).get(1), DELTA);
//		assertEquals(matrix.get(1,2), eigenVectorList.get(1).get(2), DELTA);
//		
//		assertEquals(matrix.get(2,0), eigenVectorList.get(2).get(0), DELTA);
//		assertEquals(matrix.get(2,1), eigenVectorList.get(2).get(1), DELTA);
//		assertEquals(matrix.get(2,2), eigenVectorList.get(2).get(2), DELTA);
//	}
}
