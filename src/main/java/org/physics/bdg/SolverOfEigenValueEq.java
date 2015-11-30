package org.physics.bdg;

import java.util.List;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

//import org.joda.time.DateTime;
import org.netlib.lapack.Dsyevd;
import org.netlib.util.intW;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * 実対称行列固有値方程式ソルバクラス
 * 
 * @author y-ok
 */
public class SolverOfEigenValueEq {

	// 行列サイズ
	private static int m_matrixSize = 0;

	// 固有値と固有ベクトル
	private static Multimap<Double, Vector> eigenMap = ArrayListMultimap.create();

	/**
	 * 実対称行列の固有値方程式を解く
	 * 
	 * @param matrix BdGハミルトニアン
	 * @return 実行結果
	 */
	public int solveEigenValueEq(Matrix matrix) {

		m_matrixSize = matrix.numRows();

		double[] eigenvalue = new double[m_matrixSize];
		double work[] = new double[1 + 5 * m_matrixSize + 5 * m_matrixSize * m_matrixSize];
		int[] iwork = new int[3 + 5 * m_matrixSize];
		intW info = new intW(0);

		double[] arrayMatrix = ((DenseMatrix) matrix).getData();
		/**
		 * use Lapack to find the eigenvalues and eigenvectors
		 * http://icl.cs.utk.edu/projectsfiles/f2j/javadoc/org/netlib/lapack/Dsyevd.html
		 */
//		DateTime starTime = new DateTime();
//		long startMillisTime = starTime.getMillis();

		Dsyevd.dsyevd("V", "U", m_matrixSize, arrayMatrix, 0, m_matrixSize, eigenvalue, 0, work, 0, work.length, iwork, 0, iwork.length, info);

//		DateTime endTime = new DateTime();
//		long endMillisTime = endTime.getMillis();

//		System.out.println((endMillisTime - startMillisTime) + "ms");

		List<Vector> eigenVectorList = Lists.newLinkedList();
		makeEigenVectorlist(arrayMatrix, eigenVectorList);

		Multimap<Double, Vector> tmpEigenMap = ArrayListMultimap.create();

		// 固有値に対する固有ベクトルペア作成
		for (int i = 0; i < eigenVectorList.size(); i++) {
			tmpEigenMap.put(eigenvalue[i], eigenVectorList.get(i));
		}
		eigenMap = tmpEigenMap;

		return info.val;
	}

	/**
	 * 固有値ベクトル一覧を作成する
	 * 
	 * @param matrix
	 * @param eigenVectorlist
	 */
	private static void makeEigenVectorlist(double[] matrix, List<Vector> eigenVectorlist) {

		double[] arrayEigenVector = new double[m_matrixSize];

		for (int i = 0; i < m_matrixSize; i++) {
			Vector eigenVector = new DenseVector(arrayEigenVector);
			for (int j = 0; j < m_matrixSize; j++) {
				eigenVector.set(j, matrix[i * m_matrixSize + j]);
			}
			eigenVectorlist.add(eigenVector);
		}
	}

	/**
	 * 固有値と固有ベクトルのペアを返す
	 * 
	 * @return eigenMap
	 */
	public Multimap<Double, Vector> getEigenValueAndEigenVectorMap() {
		return eigenMap;
	}
}
