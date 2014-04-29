package models

import org.joda.time.DateTime
import reactivemongo.bson._

case class Printer(
  id: Option[BSONObjectID],
  name: String,
  locationId: BSONObjectID,
  createdBy: String,
  creationDate: Option[DateTime],
  updateDate: Option[DateTime])


object Printer {

	implicit object PrinterBSONReader extends BSONDocumentReader[Printer] {
	    def read(doc: BSONDocument): Printer =
	     	Printer(
		        doc.getAs[BSONObjectID]("_id"),
		        doc.getAs[String]("name").get,
		        doc.getAs[BSONObjectID]("locationId").get,
		        doc.getAs[String]("createdBy").get,
		        doc.getAs[BSONDateTime]("creationDate").map(dt => new DateTime(dt.value)),
		        doc.getAs[BSONDateTime]("updateDate").map(dt => new DateTime(dt.value))
		)
	}
  	implicit object PrinterBSONWriter extends BSONDocumentWriter[Printer] {
    	def write(printer: Printer): BSONDocument = BSONDocument(
	        "_id" -> printer.id.getOrElse(BSONObjectID.generate),
	        "name" -> printer.name,
	        "locationId" -> printer.locationId,
	        "createdBy" -> printer.createdBy,
	        "creationDate" -> printer.creationDate.map(date => BSONDateTime(date.getMillis)),
	        "updateDate" -> printer.updateDate.map(date => BSONDateTime(date.getMillis))
	    )
  	}
}
