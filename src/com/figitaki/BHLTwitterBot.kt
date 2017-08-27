package com.figitaki

import twitter4j.*
import com.flickr4java.flickr.*
import com.flickr4java.flickr.photos.Photo
import com.vdurmont.emoji.EmojiParser
import java.io.BufferedInputStream
import java.lang.System.exit
import java.net.URL
import java.util.concurrent.ThreadLocalRandom
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

fun randomPhoto(flickr: Flickr): Photo? = try {
    val user = flickr.peopleInterface.findByUsername("biodivlibrary")
    val photos = flickr.peopleInterface.getPublicPhotos(user.id, 10, 1)
    val randIndex = ThreadLocalRandom.current().nextInt(0, photos.total)
    flickr.peopleInterface.getPublicPhotos(user.id, 1, randIndex)[0]
} catch (fe : FlickrException) {
    fe.printStackTrace()
    println("Encountered an error with flickr: ${fe.message}")
    null
}

fun tweetPhoto(twitter: Twitter, photo: Photo) = try {
    val emojis = arrayOf(
            ":evergreen_tree:",
            ":seedling:",
            ":hibiscus:",
            ":bird:",
            ":bug:",
            ":rabbit2:",
            ":earth_asia:",
            ":earth_africa:"
    )
    val randIndex = ThreadLocalRandom.current().nextInt(0, emojis.size)
    val escapedString = EmojiParser.parseToUnicode(emojis[randIndex % emojis.size])
    val statusText = "$escapedString ${photo.url}"
    val statusUpdate = StatusUpdate(statusText)

    val urlConnection = URL(photo.largeUrl).openConnection()
    val inputStream = BufferedInputStream(urlConnection.getInputStream())
    statusUpdate.setMedia("image.${photo.originalFormat}", inputStream)

    val status = twitter.updateStatus(statusUpdate)
    println("Successfully updated the status to [${status.text}]")
} catch (te : TwitterException) {
    te.printStackTrace()
    println("Failed to tweet: ${te.message}")
    exit(-1)
}

fun main(args: Array<String>) {
    val apikey = "6936713b7b5cf9107a5e449a87731a82"
    val secret = "86d2cefa25a9b436"

    val flickr = Flickr(apikey, secret, REST())

    val cb = ConfigurationBuilder()
    cb.setDebugEnabled(true)
            .setOAuthConsumerKey("JhJFKkMqh5VQwxPyj599wIjzc")
            .setOAuthConsumerSecret("8wuxmEQQ2Zc5rXbJKoQssCji5YRrtAoNp9sUPKhoLpjEWcxq2X")
            .setOAuthAccessToken("901707183947038720-738HefJegmLzcyZRNkQts0SKxS1RnKS")
            .setOAuthAccessTokenSecret("1NcIF03jOiDLJaN9yWPh39yeOWMpJmGstLKafwlG11Do2")
    val tf = TwitterFactory(cb.build())
    val twitter = tf.instance

    while (true) {
        val photo = randomPhoto(flickr)
        if (photo != null) tweetPhoto(twitter, photo)
        Thread.sleep(2 * 60 * 60 * 1000) // Wait for 2 hours
    }
}
