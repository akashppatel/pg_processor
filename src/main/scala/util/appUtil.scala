package util

/**
  * Created by LENOVO PC on 13-10-2016.
  */
object AppUtil {
  def sleep(time: Long) { Thread.sleep(time) }

  /*
    Capitalize the first letter of every word in a string, while lower-casing everything else:
   */
  def capitalStartingLetterOfEachWord(input : String) : String = {
    input.toLowerCase.split(' ').map(_.capitalize).mkString(" ")
  }

  /**
    * to show string option value
    * @param x
    * @return
    */
  def showStringOption(x: Option[String]) = x match {
    case Some(s) => s
    case None => "?"
  }
}
