from threading import Thread

class MyThread(Thread):
    
    def __init__(self, node, start_row, start_column, end_row, end_column, tip):
        """
            @param  node: the node that started the thread
            @param start_row: the start row for the block we need from the datastore
            @param start_column: the start column from the block we need from the datastore
            @param end_row: the last row from the block we need from the datastore
            @param end_column: the last column from the block we need from the datastore
            @param tip: 'a' or 'b' - gives the name of the matrix from which we take the data
        """
         
        Thread.__init__(self)
        self.node = node
        self.start_row = start_row
        self.start_column = start_column
        self.end_row = end_row
        self.end_column = end_column
        self.tip = tip
        self.block = [[0 for i in range(end_column - start_column)] for j in range(end_row - start_row)]
        pass
    
    def run(self):
        """ register the thread """
        self.node.data_store.register_thread(self.node)
        """ take the block from the matrix indicated by tip variable """
        if (self.tip == 'a'):
            for i in range(self.start_row, self.end_row):
                for j in range(self.start_column, self.end_column):
                    self.node.semafor.acquire()
                    element = self.node.data_store.get_element_from_a(self.node, i, j)
                    self.node.semafor.release()
                    self.block[i-self.start_row][j-self.start_column] = element
        else:
            for i in range(self.start_row, self.end_row):
                for j in range(self.start_column, self.end_column):
                    self.node.semafor.acquire()
                    element = self.node.data_store.get_element_from_b(self.node, i, j)
                    self.node.semafor.release()
                    self.block[i-self.start_row][j-self.start_column] = element
        pass
        
        
    
    