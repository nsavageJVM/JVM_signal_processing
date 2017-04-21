package func;

case class EcgRecord(num: Long, ecg: Double)

object EcgRecord {

  def parseEcg(s: String) = {
    val Array(long, dbl) = s.split(",")
    EcgRecord(long.toLong, dbl.toDouble)
  }

}



import breeze.linalg._
import breeze.math.Ring

import scala.reflect.ClassTag

object TimeDomainAnalysis {

  def finiteDifferences[R: Ring](sample: DenseVector[R])(implicit ct: ClassTag[R]): DenseVector[R] = {

    val delayedIndices = 0 to sample.length - 2
    val indices = 1 to sample.length -1
    sample(indices) - sample(delayedIndices)


  }

}

