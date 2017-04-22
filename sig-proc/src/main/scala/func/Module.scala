package func;

case class EcgPoint(num: Long, ecg: Double)

object EcgPoint {

  def parseEcg(s: String) = {
    val Array(long, dbl) = s.split(",")
    EcgPoint(long.toLong, dbl.toDouble)
  }

}



import breeze.linalg._
import breeze.math.Ring

import scala.reflect.ClassTag

object FiniteDifferences {

  def forwardDifference[R: Ring](domian: DenseVector[R])(implicit ct: ClassTag[R]): DenseVector[R] = {

    val delayedIndices = 0 to domian.length - 2
    val indices = 1 to domian.length -1
    domian(indices) - domian(delayedIndices)


  }

}

