package hw_3_linreg

import breeze.linalg._
import breeze.stats._
import breeze.numerics._
import breeze.linalg.Vector.castOps
import java.io._


object CSVReader {
  def readCSV(): (DenseMatrix[Double], DenseVector[Double], DenseMatrix[Double],
    DenseVector[Double], DenseMatrix[Double], DenseVector[Double]) = {
    val fileTrain = csvread(new File ("train.csv"), separator = ',', skipLines = 0)
    val fileTest = csvread(new File ("test.csv"), separator = ',', skipLines = 0)

    val trainValSplitRatio = 0.8
    val rowSplitTrainVal = (fileTrain.rows * trainValSplitRatio).toInt
    val colsData = fileTrain.cols - 1

    val dataTrain = fileTrain (0 until rowSplitTrainVal, ::)
    val dataVal = fileTrain (rowSplitTrainVal to -1, ::)

    val xTrain = DenseMatrix.horzcat(DenseMatrix.ones[Double](dataTrain.rows, 1), dataTrain(::, 0 until colsData))
    val yTrain = dataTrain (::, - 1)
    val xVal = DenseMatrix.horzcat(DenseMatrix.ones[Double](dataVal.rows, 1), dataVal (::, 0 until colsData))
    val yVal = dataVal (::, - 1)
    val xTest = DenseMatrix.horzcat(DenseMatrix.ones[Double](fileTest.rows, 1), fileTest (::, 0 until colsData))
    val yTest = fileTest (::, - 1)

    return (xTrain, yTrain, xVal, yVal, xTest, yTest)
  }
}


object LinearRegression {

  var weights: DenseVector[Double] = DenseVector[Double]()

  def fit(xTrain:DenseMatrix[Double], yTrain:DenseVector[Double]): Unit = {
    println("Start fit!")
    val xTx = xTrain.t * xTrain
    val xTx_inv = pinv(xTx)
    val xTx_inv_xT = xTx_inv * xTrain.t
    val w = xTx_inv_xT * yTrain
    weights = w
    println("Fit complete!")
  }

  def predict(xTest:DenseMatrix[Double]): DenseVector[Double] = {
    val yPred = xTest * weights
    return yPred
  }

  def validation(xVal:DenseMatrix[Double], yVal:DenseVector[Double]): Unit = {
    val yValPred = predict(xVal)
    saveMetricsToFile(getMetrics(yVal, yValPred), "validationMetrics.txt")
  }

  def getMetrics(yTrue:DenseVector[Double], yPred:DenseVector[Double]): String = {
    val mae = mean(yTrue - yPred)
    val rmse = sqrt(sum(pow((yTrue - yPred), 2))/yTrue.length)

    println(f"Mean absolute error: ${mae}")
    println(f"Root mean square error: ${rmse}")
    return  f"Mean absolute error: ${mae} \nRoot mean square error: ${rmse}"
  }

  def saveMetricsToFile(metrics:String, valFilePath:String): Unit = {
    new PrintWriter(valFilePath) { write(metrics); close}
  }

  def savePredictionsToFile(yPred:DenseVector[Double], valFilePath:String): Unit = {
    val predFile = new PrintWriter(valFilePath)
    for (y <- yPred) {
      predFile.write(y + "\n")
    }
    predFile.close()
  }

  def printWeights(): Unit = {
    val bias = weights(0)
    val w = weights(1 to -1)
    println(s"Weights is ${w}, and bias is ${bias}")
  }
}



object Main {
  def main(args: Array[String]): Unit = {
    val (xTrain, yTrain, xVal, yVal, xTest, yTest) = CSVReader.readCSV()
    val lr = LinearRegression
    lr.fit(xTrain, yTrain)
    lr.validation(xVal, yVal)
    lr.printWeights()
    val yPred = lr.predict(xTest)
    lr.savePredictionsToFile(yPred, "predictions.txt")

  }
}