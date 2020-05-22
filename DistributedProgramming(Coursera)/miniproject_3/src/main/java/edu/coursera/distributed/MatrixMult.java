package edu.coursera.distributed;

import edu.coursera.distributed.util.MPI;
import edu.coursera.distributed.util.MPI.MPIException;

/**
 * A wrapper class for a parallel, MPI-based matrix multiply implementation.
 */
public class MatrixMult {
    public static void computeMult(Matrix a, Matrix b, Matrix c,
                                   int startRow, int endRow) {
        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < c.getNCols(); col++) {
                c.set(row, col, 0.0);
                for (int k = 0; k < b.getNRows(); k++) {
                    c.incr(row, col, a.get(row, k) * b.get(k, col));
                }
            }
        }
    }
    public static void parallelMatrixMultiply(Matrix a, Matrix b, Matrix c,
                                              final MPI mpi) throws MPIException {
        final int myrank = mpi.MPI_Comm_rank(mpi.MPI_COMM_WORLD);
        final int size = mpi.MPI_Comm_size(mpi.MPI_COMM_WORLD);
        final int nrows = c.getNRows();
        final int rowChunk = (nrows + size - 1) / size;

        final int startRow = myrank * rowChunk;
        int endRow = (myrank + 1) * rowChunk;
        if (endRow > nrows) endRow = nrows;

        if(myrank == 0) {
            MPI.MPI_Request[] requests = new MPI.MPI_Request[size - 1];
            for (int i = 1; i < size; i++) {
                int rankStartRow = i * rowChunk;
                int rankEndRow = (i + 1) * rowChunk;
                if (rankEndRow > nrows) rankEndRow = nrows;

                requests[i - 1] = mpi.MPI_Isend(a.getValues(),
                        a.getOffsetOfRow(rankStartRow), (rankEndRow - rankStartRow) * a.getNCols(),
                        i, i, mpi.MPI_COMM_WORLD);
            }
            mpi.MPI_Bcast(b.getValues(), 0, b.getNRows() * b.getNCols(), 0, mpi.MPI_COMM_WORLD);
            mpi.MPI_Waitall(requests);
        } else {
            MPI.MPI_Request req = mpi.MPI_Irecv(a.getValues(),
                    a.getOffsetOfRow(startRow), (endRow-startRow)*a.getNCols(),
                    0, myrank, mpi.MPI_COMM_WORLD);
            mpi.MPI_Bcast(b.getValues(), 0, b.getNRows() * b.getNCols(), 0, mpi.MPI_COMM_WORLD);
            mpi.MPI_Wait(req);
            computeMult(a,b,c,startRow,endRow);
            mpi.MPI_Send(c.getValues(),
                    c.getOffsetOfRow(startRow), (endRow - startRow) * c.getNCols(),
                    0, myrank, mpi.MPI_COMM_WORLD);
        }
        if (myrank == 0) {
            MPI.MPI_Request[] requests = new MPI.MPI_Request[size - 1];
            for (int i = 1; i < size; i++) {
                int rankStartRow = i * rowChunk;
                int rankEndRow = (i + 1) * rowChunk;
                if (rankEndRow > nrows) rankEndRow = nrows;

                requests[i - 1] = mpi.MPI_Irecv(c.getValues(),
                        c.getOffsetOfRow(rankStartRow), (rankEndRow - rankStartRow) * c.getNCols(),
                        i, i, mpi.MPI_COMM_WORLD);
            }
            computeMult(a,b,c,startRow,endRow);
            mpi.MPI_Waitall(requests);
        }
    }
}
