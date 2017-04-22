package func

import breeze.linalg.DenseVector
import FiniteDifferences.forwardDifference
import breeze.linalg._
import breeze.stats._
import breeze.signal.{convolve => breezeConvolve, correlate => breezeCorrelate, _}
import scala.math.pow

object EcgAnalyzer {

  def analyse (point: DenseVector[Double], rate: Double, sampleSize: Double = 5.0): DenseVector[Double] = {

    val windowSize = (sampleSize * rate).toInt

    val deltaPowerSignal: DenseVector[Double] = forwardDifference(point) :^ 2.0

    val slidingSample = (0 until (deltaPowerSignal.length / windowSize)) map { i =>
      deltaPowerSignal.slice(i * windowSize, (i+1) * windowSize)
    }

    val powerSignalThreshold = mean(slidingSample.map(stddev(_)))
    val powerSignalMax = mean(slidingSample.map(max(_)))

    val energy = powerSignalFilter(deltaPowerSignal.toDenseVector, powerSignalThreshold, powerSignalMax, rate)

    energy
  }


  private def powerSignalFilter(sig: DenseVector[Double], cuttOff: Double, maxPower: Double, rate: Double): DenseVector[Double] = {
    val shannon_energy = normalisedShannonEnergy(sig, cuttOff, maxPower)
    val inverseDeltaFunctionShape = (rate * 0.2 ).toInt
    filter(shannon_energy, DenseVector.fill(inverseDeltaFunctionShape) (1.0 / inverseDeltaFunctionShape))
  }


  private def filter(shannon_energy: DenseVector[Double], deltaFunction: DenseVector[Double]): DenseVector[Double] = {
    breezeConvolve(shannon_energy, deltaFunction, overhang = OptOverhang.PreserveLength)
  }

  // http://stackoverflow.com/questions/33859255/average-normalised-shannon-energy-for-phonocardiography-signals
  private def normalisedShannonEnergy(sample: DenseVector[Double], cuttOff: Double, max: Double): DenseVector[Double] = {
    sample.map { signal =>
      if (signal < cuttOff) 0.0

      else {

        val _norm = signal / max

        if (_norm > 1.0) 0.0
        else {
          val _normSquared = pow(_norm, 2)
          val shennon_energy = -_normSquared * math.log(_normSquared)
          if (shennon_energy.isNaN) 0.0 else _norm
        }
      }
    }
  }


}
