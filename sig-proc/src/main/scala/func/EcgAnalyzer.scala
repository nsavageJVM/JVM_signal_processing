package func

import breeze.linalg.DenseVector
import TimeDomainAnalysis.finiteDifferences
import breeze.linalg._
import breeze.stats._
import breeze.signal.{convolve => breezeConvolve, correlate => breezeCorrelate, _}
import scala.math.pow

object EcgAnalyzer {
  //# The raw ECG signal # Sampling rate in HZ # Window size in seconds to use for
  def analyse ( ecg: DenseVector[Double], rate: Double,  ransacWindowSize: Double = 5.0): DenseVector[Double] = {
    val rws = (ransacWindowSize * rate).toInt

    // Square (=signal power) of the first difference of the signal
    val decgPower: DenseVector[Double] = finiteDifferences(ecg) :^ 2.0

    val windows = (0 until (decgPower.length / rws)) map { i =>
      decgPower.slice(i * rws, (i+1) * rws)
    }

    val threshold = mean(windows.map(stddev(_)))
    val maxPower = mean(windows.map(max(_)))

    val energy = lpEnergy(decgPower.toDenseVector, threshold, maxPower, rate)

    energy
  }


  private def lpEnergy(sig: DenseVector[Double], thr: Double, maxPower: Double, rate: Double) = {
    val shEnergy = shannonEnergy(sig, thr, maxPower)
    val meanWindowLen = (rate * 0.125 + 1).toInt
    convolve(shEnergy, DenseVector.fill(meanWindowLen)(1.0 / meanWindowLen))
  }


  private def convolve(a: DenseVector[Double], v: DenseVector[Double]): DenseVector[Double] = {
    breezeConvolve(a, v, overhang = OptOverhang.PreserveLength)
  }
 
  private def shannonEnergy(sig: DenseVector[Double], thr: Double, maxPower: Double) = {
    sig.map { x =>
      if (x < thr) 0.0
      else {
        val t = x / maxPower
        if (t > 1.0) 0.0
        else {
          val tSquared = pow(t, 2)
          val shEnergy = -tSquared * math.log(tSquared)
          if (shEnergy.isNaN) 0.0 else t
        }
      }
    }
  }


}
