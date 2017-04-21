package func


import breeze.linalg.DenseVector
import func.util.CsvIo

// https://github.com/scalanlp/breeze/wiki/Linear-Algebra-Cheat-Sheet
// https://darrenjw.wordpress.com/tag/breeze/


object Entrypoint   {

  private var rawData_ : Array[Double] = _
  private var procData_ : Array[Double] = _

   def processData(dataFile : String ): Unit  = {

     val url = getClass.getResource(dataFile)

     val ecgRecords = CsvIo.fromCsv[EcgRecord](url.toURI.getPath )(EcgRecord.parseEcg)

     val data = DenseVector(ecgRecords.map(_.ecg).toArray)

     rawData_ = data.toArray

     procData_ =  EcgAnalyzer.analyse(data, 50 , 5.0).toArray

  }

  def rawData = rawData_

  def processData = procData_

}








