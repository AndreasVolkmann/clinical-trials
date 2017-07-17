import time


def measure(fun):
    start = time.time()
    fun()
    end = time.time()
    print("Took", end - start)
