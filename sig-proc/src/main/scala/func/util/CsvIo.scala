package func.util

import scala.io.{BufferedSource, Source}
import scala.util.{Success, Try}
import breeze.linalg.DenseVector

import scala.reflect._

object CsvIo {


  def fromCsv[T: ClassTag](path: String)(parse: String => T): DenseVector[T] = {

    val src = Source.fromFile(path)

    fromCsvSource(src)(parse)
  }


  private def fromCsvSource[T: ClassTag](src: BufferedSource)(parse: String => T): DenseVector[T] = {
    val rawLines = src.getLines().toArray
    val lines = {
      // check whether first line is legitimate record - it could be a header
      Try(parse(rawLines.head)) match {
        case Success(record) => rawLines
        case _ => rawLines.tail
      }
    }

    val parsedLines = lines.map(parse)
    DenseVector(parsedLines)
  }
}
