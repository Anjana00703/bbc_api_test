package definition

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.must.Matchers._
import requests._
import scala.ApiClient._
import scala.util.{Try, Success, Failure}
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import upickle.default.{read, ReadWriter, macroRW}


class StepDefinitions extends ScalaDsl with EN with Matchers {

  private var response: Response = _
  private var scheduleItems: Seq[ScheduleItem] = _
  private val defaultEndpoint = "/api/RMSTest/ibltest"
  private var startTime: Long = _

  Given("I make a GET request to the API") { () =>
    startTime = System.currentTimeMillis()
    response = getSchedule()
  }

  Given("I make a GET request to the API for date {string}") { (date: String) =>
    startTime = System.currentTimeMillis()
    response = getSchedule(date)
  }

  Then("the HTTP status code of the response is {int}") { (statusCode: Int) =>
    response.statusCode mustBe statusCode
  }

  Then("the response time of the request is below {int} milliseconds") { (maxResponseTime: Int) =>
    val endTime = System.currentTimeMillis()
    val elapsedTime = endTime - startTime
    elapsedTime must be <= maxResponseTime.toLong
  }

  Then("the {string} field is never null or empty for all items") { (field: String) =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)

    scheduleItems.foreach { item =>
      field match {
        case "id" => item.id must not be (null)
          item.id must not be ("")
        case _ => fail(s"Unsupported field: $field")
      }
    }
  }

  Then("the type in episode for every item is always {string}") { (expectedType: String) =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)
    scheduleItems.foreach { item =>
      item.episode.`type` mustBe expectedType
    }
  }

  Then("the title field in episode is never null or empty for any schedule item") { () =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)
    scheduleItems.foreach { item =>
      item.episode.title must not be (null)
      item.episode.title must not be ("")
    }
  }

  Then("only one episode in the list has live field in episode as true") { () =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)
    val liveEpisodes = scheduleItems.count(item => item.episode.live.contains(true))
    liveEpisodes mustBe 1
  }

  Then("the transmission_start date value is before the transmission_end date") { () =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)
    scheduleItems.foreach { item =>
      val startTime = OffsetDateTime.parse(item.transmission_start, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
      val endTime = OffsetDateTime.parse(item.transmission_end, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
      startTime.isBefore(endTime) mustBe true
    }
  }

  Then("in the response headers, verify the Date value") { () =>
    val dateHeaderOption = response.headers.get("Date")

    dateHeaderOption match {
      case Some(dates) =>
        val parsedDates = dates.map { dateString =>
          Try(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.parse(dateString))
        }

        val allDatesParsedSuccessfully = parsedDates.forall(_.isSuccess)

        allDatesParsedSuccessfully mustBe true
        
      case None =>
        fail("Date header is not present in the response.")
    }
  }
  

  Then("the error object had the properties {string} and {string}") { (property1: String, property2: String) =>
    val responseBody = response.text()
    var errorObject: Map[String, String] = Map.empty
    var exception: Throwable = null

    val parsedResponse = Try(upickle.default.read[Map[String, String]](responseBody))

    if (parsedResponse.isSuccess) {
      errorObject = parsedResponse.get
      errorObject.contains(property1) mustBe true
      errorObject.contains(property2) mustBe true

    } else {
      exception = parsedResponse.failed.get
      fail(s"Failed to parse response body as JSON: ${exception.getMessage}")
    }
  }

  
  Then("there should be an item with duration greater than {int}") { (minDuration: Int) =>
    val jsonString = response.text()
    scheduleItems = parseSchedule(jsonString)
    val hasItemWithLongDuration = scheduleItems.exists(_.duration > minDuration)
    hasItemWithLongDuration mustBe true
  }
}