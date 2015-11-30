package org.physics.bdg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.io.MatrixInfo;
import no.uib.cipr.matrix.io.MatrixSize;
import no.uib.cipr.matrix.io.MatrixVectorWriter;

/**
 * 疎行列情報ファイルIFクラス
 * 
 * @author y-ok
 *
 */
public class SparseMatrixFileIF {

	// 疎行列
	private static Matrix m_sparseMatrix;

	// パス情報
	private PathInfo m_pathInfo;

	/**
	 * 疎行列データクラス
	 */
	private class sparseMatrixData {
		int x, y;
		double value;
	};

	/**
	 * コンストラクタ
	 */
	public SparseMatrixFileIF() {
	}

	/**
	 * コンストラクタ
	 * 
	 * @param pathInfo
	 */
	public SparseMatrixFileIF(PathInfo pathInfo) {
		m_pathInfo = pathInfo;
	}

	/**
	 * 疎行列を取得する
	 * 
	 * @return 疎行列
	 */
	public static Matrix getSparseMatrix() {
		return m_sparseMatrix;
	}

	/**
	 * 疎行列を設定する
	 * 
	 * @param matrix
	 *            疎行列
	 */
	public void setSparseMatrix(Matrix matrix) {
		SparseMatrixFileIF.m_sparseMatrix = matrix;
	}

	/**
	 * 疎行列情報ファイルを作成する
	 * 
	 * @throws IOException
	 *             入出力関係
	 */
	public void writeSparseMatrixData() throws IOException {
		String strSparseFilePath = m_pathInfo.getStrPwd() + "/" + m_pathInfo.getSparsePath();

		MatrixVectorWriter writer = new MatrixVectorWriter(new BufferedWriter(new FileWriter(new File(strSparseFilePath))));
		MatrixInfo mi = new MatrixInfo(true, MatrixInfo.MatrixField.Real, MatrixInfo.MatrixSymmetry.Symmetric);
		writer.printMatrixInfo(mi);

		List<sparseMatrixData> data = Lists.newLinkedList();

		for (int i = 0; i < m_sparseMatrix.numRows(); i++) {
			for (int j = 0; j < m_sparseMatrix.numColumns(); j++) {

				// 非ゼロ要素抽出
				if (!Double.toString(m_sparseMatrix.get(i, j)).equals("0.0")) {
					sparseMatrixData sparseMatrixData = new sparseMatrixData();
					sparseMatrixData.x = i;
					sparseMatrixData.y = j;
					sparseMatrixData.value = m_sparseMatrix.get(i, j);
					data.add(sparseMatrixData);
				}
			}
		}
		writer.printMatrixSize(new MatrixSize(Iterables.getLast(data).x + 1, Iterables.getLast(data).y + 1, data.size()), mi);

		int[] col = new int[data.size()];
		int[] row = new int[data.size()];
		double[] val = new double[data.size()];

		for (int i = 0; i < data.size(); i++) {
			col[i] = data.get(i).x;
			row[i] = data.get(i).y;
			val[i] = data.get(i).value;
		}
		writer.printCoordinate(col, row, val, 1);
		writer.close();
	}
}
