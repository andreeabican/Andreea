"""
    This module represents a cluster's computational node.

    Computer Systems Architecture course
    Assignment 1 - Cluster Activity Simulation
    march 2013
"""
from mythread import MyThread
import threading


class Node:
    """
        Class that represents a cluster node with computation and storage functionalities.
    """
    
    def __init__(self, node_ID, block_size, matrix_size, data_store):
        """http://fafc/
            Constructor.

            @param node_ID: a pair of IDs uniquely identifying the node; 
            IDs are integers between 0 and matrix_size/block_size
            @param block_size: the size of the matrix blocks stored in this node's datastore
            @param matrix_size: the size of the matrix
            @param data_store: reference to the node's local data store
        """
        global maxconnections
        global semafor
        self.node_ID = node_ID
        self.block_size = block_size
        self.matrix_size = matrix_size
        self.data_store = data_store
        maxconnections = data_store.get_max_pending_requests(node_ID)
        """ semaphore used to limit the access to datastore """
        self.semafor = threading.Semaphore(maxconnections)
        pass

    def set_nodes(self, nodes):
        """
            Informs the current node of the other nodes in the cluster. 
            Guaranteed to be called before the first call to compute_matrix_block.

            @param nodes: a list containing all the nodes in the cluster
        """
        self.nodes = nodes
        pass
    
    def get_matrix_a_block(self, start_row, start_column, end_row, end_column):
        """ 
            access node's datastore to get the elements [from start_row][start_column]
            to [end_row][end_column] - for matrix a
            
            @return: the matrix with all the values between this limits
        """
        thread = MyThread(self, start_row, start_column, end_row, end_column, 'a')
        thread.start()
        thread.join()
        """ returns the matrix saved in thread's variable - block """
        return thread.block
        pass

    def get_matrix_b_block(self, start_row, start_column, end_row, end_column):
        """ 
            access node's datastore to get the elements [from start_row][start_column]
            to [end_row][end_column] - for matrix b
            
            @return: the matrix with all the values between this limits
        """
        thread = MyThread(self, start_row, start_column, end_row, end_column, 'b')
        thread.start()
        thread.join()
        return thread.block
        pass

    def mult_matrices(self, A, B, num_rows, num_columns):
        """ 
            multiplies the matrices A and B
            
            @param A: first matrix
            @param B: second matrix
            @param num_rows: number of rows for matrix A
            @param num_columns: number of columns for matrix B 
            
            @return: the result of A*B
        """
        C = [[0 for col in range(num_columns)] for row in range(num_rows)]
        for i in xrange(num_rows):
            for j in xrange(num_columns):
                for k in xrange(0, self.matrix_size):
                    C[i][j] += A[i][k]*B[k][j]
        return C
        pass
    
    def compute_matrix_block(self, start_row, start_column, num_rows, num_columns):
        """
            Computes a given block of the result matrix.
            The method invoked by FEP nodes.

            @param start_row: the index of the first row in the block
            @param start_column: the index of the first column in the block
            @param num_rows: number of rows in the block
            @param num_columns: number of columns in the block

            @return: the block of the result matrix encoded as a row-order list of lists of integers
        """
        A = [[None for i in range(0,self.matrix_size)] for j in range(0,num_rows)]
        B = [[None for i in range(0,num_columns)] for j in range(0,self.matrix_size)]
        id_nod = self.node_ID
        
        """ Determine from which database the block starts """
        
        for i in xrange(start_row, start_row + num_rows):
            for j in xrange(0, self.matrix_size):
                if (A[i-start_row][j] == None):
                    """ 
                        the coordinates of the block where
                        [i][j] element of the matrix is found
                    """
                    row = i/self.block_size
                    column = j/self.block_size
                    """ 
                        we want to take every value we need from the block - in a single access
                        so we determine the limits of the block we want to take from the datastore
                    """
                    end_row = (row+1)*self.block_size
                    end_column = (column + 1)*self.block_size
                    startcol = column*self.block_size
                    if (end_row > start_row + num_rows):
                        end_row = start_row + num_rows
                    end_column = startcol + self.block_size
                    endrow = end_row
                    end_row = (end_row - 1) % self.block_size + 1
                    end_column = (end_column - 1) % self.block_size + 1
                    start_r = i % self.block_size
                    start_c = j % self.block_size
                    """ 
                         decide which node has the block and get the part of the block
                         that we need
                    """
                    if (id_nod[0] == row and id_nod[1] == column):
                        block_a = self.get_matrix_a_block(start_r, start_c, end_row, end_column)
                    else:
                        node_number = (self.matrix_size/self.block_size)*row + column
                        called_node = self.nodes[node_number]
                        block_a = called_node.get_matrix_a_block(start_r, start_c, end_row, end_column)
                    
                    """ add the elements taken from the node to our matrix """
                    for a in range(i, endrow):
                        for b in range(j, j + self.block_size):
                            aux = block_a[a - i][b - j]
                            A[a - start_row][b] = aux
        
        for i in xrange(0, self.matrix_size):
            for j in xrange(start_column, start_column + num_columns):
                if (B[i][j-start_column] == None):
                    """ 
                        the coordinates of the block where
                        [i][j] element of the matrix is found
                    """
                    row = i/self.block_size
                    column = j/self.block_size
                    """ 
                        we want to take every value we need from the block - in a single access
                        so we determine the limits of the block we want to take from the datastore
                    """
                    end_row = (row+1)*self.block_size
                    end_column = (column + 1)*self.block_size
                    if (end_column > start_column + num_columns):
                        end_column = start_column + num_columns
                    end_row = (end_row - 1) % self.block_size + 1
                    endcol = end_column
                    end_column = (end_column - 1) % self.block_size + 1
                    start_r = i % self.block_size
                    start_c = j % self.block_size
                    """ 
                         decide which node has the block and get the part of the block
                         that we need
                    """
                    if (id_nod[0] == row and id_nod[1] == column):
                        block_b = self.get_matrix_b_block(start_r, start_c, end_row, end_column)
                    else:
                        node_number = (self.matrix_size/self.block_size)*row + column
                        called_node = self.nodes[node_number]
                        block_b = called_node.get_matrix_b_block(start_r, start_c, end_row, end_column)
                    
                    """ add the elements taken from the node to our matrix """
                    for a in range(i, i+self.block_size):
                        for b in range(j, endcol):
                            aux = block_b[a - i][b - j]
                            B[a][b - start_column] = aux
        
        C = self.mult_matrices(A, B, num_rows, num_columns)
        return C
    pass

    def shutdown(self):
        """
            Instructs the node to shutdown (terminate all threads).
        """
        pass


