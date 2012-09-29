import scala.util.Random
import java.io._
import java.sql.DriverManager
import scalikejdbc._
import twitter4j._
import twitter4j.auth._

object Config { 
  val consumerKey = "xxxxxxxxxxxxxxxxxxxxxxxxx"
  val consumerSecret = "xxxxxxxxxxxxxxxxxxxxxxxxx"
  val accessToken = "xxxxxxxxxxxxxxxxxxxxxxxxx"
  val accessTokenSecret = "xxxxxxxxxxxxxxxxxxxxxxxxx"
  val objName = "/ochita"
  var dataFile = System.getProperty("java.io.tmpdir") + objName
}

object Ochita { 
  def main(args: Array[String]): Unit = {	
	if (args.size == 0) {
	  println("USAGE: scala -cp ochita-assembly-1.0.jar Ochita YOUR-APPNAME [data store dir]")
	  java.lang.System.exit(-1)
	}
	if (args.size == 2) { 
	  Config.dataFile = args(1) + Config.objName
	}
	println("[dataFile = "+ Config.dataFile +"]")

	val history = new HistoryHandler(args(0))
	val (total, today) = history.get()
	val msg = new MsgBuilder(args(0))
	val tweet = msg.get(total, today)
	println(tweet)

	val twitter = new TweetHandler()
	twitter.tweet(tweet)
  }
}

class HistoryHandler(appName: String) { 
  Class.forName("org.h2.Driver")

  def get(): (Option[Long], Option[Long]) = {
	using(DB(DriverManager.getConnection("jdbc:h2:"+ Config.dataFile, "sa", ""))) { db => 

	  db autoCommit { implicit session =>
		SQL("""
			CREATE TABLE IF NOT EXISTS `ochitaLog` (
			  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  `appName` varchar(255) DEFAULT NULL,
			  `createDate` datetime DEFAULT NULL,
			  PRIMARY KEY (`id`)
			)
			""").execute.apply()
		SQL("""
			CREATE INDEX IF NOT EXISTS `appIdx` 
			  ON `ochitaLog` (`appName`,`createDate`)
			""").execute.apply()
	    SQL("INSERT INTO ochitaLog VALUES (NULL, ?, NOW())")
		   .bind(appName).update.apply()

        val total = SQL("SELECT count(*) AS cnt FROM ochitaLog WHERE appName = ?")
		  .bind(appName).map(rs => rs.long("cnt").asInstanceOf[Long]).single.apply()

        val today = SQL("SELECT count(*) AS cnt FROM ochitaLog WHERE appName = ? AND FORMATDATETIME(NOW(), 'yyyyMMdd') = FORMATDATETIME(`createDate`, 'yyyyMMdd') ")
		  .bind(appName).map(rs => rs.long("cnt").asInstanceOf[Long]).single.apply()

		return (total, today)
	   }
																				    }
  }
}


class MsgBuilder(appName: String) { 
  val prefix = Seq(
	"またか...", "なんだと...", "ふざけんなぁごらぁ", "いい加減にしろー",
	"オマエは何を考えてるんだ", "俺の時間を返せー", "おいこらマテ"
  )
  val suffix = Seq(
	"落ちました", "逝っちゃいました", "お亡くなりになりました",
	"勝手に出て行きました", "ストライキに入りました", "実家に帰りました",
	"戻らない散歩に出たようです", "台風で飛んで行きました"
  )

  def get(total:Option[Long], today:Option[Long]) : String = {
	return "%s %sが%s 本日%d回目/通算%d回 #ochita" format(prefix(Random.nextInt(prefix.size)), appName, suffix(Random.nextInt(suffix.size)), today.get, total.get)
  }
}


class TweetHandler() { 
  val twitter: Twitter = new TwitterFactory().getInstance();
  twitter.setOAuthConsumer(Config.consumerKey, Config.consumerSecret)
  twitter.setOAuthAccessToken(new AccessToken(Config.accessToken, Config.accessTokenSecret));

  def tweet(msg: String) : Unit = { 
	twitter.updateStatus(msg)
  }
}
